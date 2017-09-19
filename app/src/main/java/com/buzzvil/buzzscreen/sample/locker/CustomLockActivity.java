package com.buzzvil.buzzscreen.sample.locker;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bluejaywireless.myrewards.R;
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
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop()");
		super.onStop();
	}

	@Override
	protected void onCurrentCampaignUpdated(Campaign campaign) {
//		// 현재 보여지고 있는 캠페인이 업데이트 될때 호출된다.
//		// 현재 캠페인에 따라 UI를 변화시키고 싶으면 여기서 작업하면 된다.
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
}
