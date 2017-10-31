package com.buzzvil.buzzscreen.sample.locker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.buzzvil.buzzscreen.sample.locker.tutorial.LockTutorialFragment;
import com.buzzvil.buzzscreen.sdk.model.object.Campaign;
import com.buzzvil.buzzscreen.sdk.ui.lock.BaseLockerActivity;
import com.buzzvil.buzzscreen.sdk.ui.lock.widget.OnSwipeListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
				if (bundle.getInt("id") <= 0) {
					Toast.makeText(getBaseContext(), "No ads", Toast.LENGTH_SHORT).show();
				}
				printBundle(bundle);
			}

			@Override
			public void onClick(Bundle bundle) {
				printBundle(bundle);
			}
		});

		getSlider().setOnSwipeListener(new OnSwipeListener() {
			@Override
			public void onLeft() {
				landing();
			}

			@Override
			public void onRight() {
				unlock();
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
		Date now = cal.getTime();
		// set time
		String time = new SimpleDateFormat("h:mm", Locale.getDefault()).format(now);
		tvTime.setText(time);

		// set am pm
		String am_pm = new SimpleDateFormat("aa", Locale.US).format(now);
		tvAmPm.setText(am_pm);

		// set date
		String dateTemplate;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			dateTemplate = DateFormat.getBestDateTimePattern(Locale.getDefault(), "E d MMM");
		} else {
			Locale locale = Locale.getDefault();
			if (locale.equals(Locale.KOREA)) {
				dateTemplate = "MMM d일 EEEE";
			} else if (locale.equals(Locale.JAPAN)) {
				dateTemplate = "MMM d日 EEEE";
			} else if (locale.equals(Locale.US)) {
				dateTemplate = "E, MMM d";
			} else {
				dateTemplate = "E, d MMM";
			}
		}
		String date = new SimpleDateFormat(dateTemplate, Locale.getDefault()).format(now);
		tvDate.setText(date);
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
					.replace(getRootView().getId(), tutorialFragment)
					.commitAllowingStateLoss();
		}
	}
}
