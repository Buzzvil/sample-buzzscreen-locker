package com.buzzvil.buzzscreen.sample.locker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.buzzvil.buzzscreen.sample.locker.tutorial.LockTutorialFragment;
import com.buzzvil.buzzscreen.sdk.model.object.Campaign;
import com.buzzvil.buzzscreen.sdk.ui.lock.BaseLockerActivity;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * CustomLockActivity.java
 * <p>
 * Created by Ben on 4/11/17.
 */
public class CustomLockActivity extends BaseLockerActivity {
	private static final String TAG = CustomLockActivity.class.getSimpleName();

	public static final String KEY_LOCK_TUTORIAL_SHOWN = "KEY_LOCK_TUTORIAL_SHOWN";

	TextView tvTime;
	TextView tvAmPm;
	TextView tvDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);

		setOnTrackingListener(new OnTrackingListener() {
			@Override
			public void onImpression(Bundle bundle) {
				printBundle(bundle);
			}

			@Override
			public void onClick(Bundle bundle) {
				printBundle(bundle);
			}
		});

		tvTime = (TextView)findViewById(R.id.locker_time);
		tvAmPm = (TextView)findViewById(R.id.locker_am_pm);
		tvDate = (TextView)findViewById(R.id.locker_date);
		tvTime.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));

		startTutorialIfNeeded();
	}

	void printBundle(Bundle bundle) {
		if (bundle.getInt("adId", 0) < 0) {
			Log.d(TAG, "Offline image");
		}
		for (String key : bundle.keySet()) {
			Object value = bundle.get(key);
			Log.d(TAG, String.format("%s %s (%s)", key,
					value.toString(), value.getClass().getName()));
		}
	}

	@Override
	protected void onCurrentCampaignUpdated(Campaign campaign) {
		// 현재 보여지고 있는 캠페인이 업데이트 될때 호출된다.
		// 현재 캠페인에 따라 UI를 변화시키고 싶으면 여기서 작업하면 된다.

		// Called when the currently viewed campaign is updated.
		// If you want to change the UI according to your current campaign, you can work here.
	}

	@Override
	protected void onTimeUpdated(Calendar cal) {
		// set time
		int hour = cal.get(Calendar.HOUR);
		String time = String.format("%d:%02d", hour == 0 ? 12 : hour, cal.get(Calendar.MINUTE));
		tvTime.setText(time);

		// set am pm
		String am_pm = String.format("%s", cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
		tvAmPm.setText(am_pm);

		// set date
		DateFormatSymbols symbols = new DateFormatSymbols();
		String dayName = symbols.getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
		String date = String.format("%d월 %d일", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
		tvDate.setText(String.format("%s %s", date, dayName));
	}

	private void startTutorialIfNeeded() {
		Context context = getApplicationContext();
		final SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		if (pref.getBoolean(KEY_LOCK_TUTORIAL_SHOWN, false) == false) {
			enableCampaigns(false);	// show default(offline) lockscreen

			LockTutorialFragment tutorialFragment = new LockTutorialFragment();
			tutorialFragment.setOnTutorialListener(new LockTutorialFragment.OnTutorialListner() {
				@Override
				public void onFinish() {
					pref.edit().putBoolean(KEY_LOCK_TUTORIAL_SHOWN, true).apply();
				}
			});
			getSupportFragmentManager()
					.beginTransaction()
					.replace(android.R.id.content, tutorialFragment)
					.commitAllowingStateLoss();
		}
	}
}
