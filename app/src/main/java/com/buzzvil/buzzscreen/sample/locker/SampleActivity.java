package com.buzzvil.buzzscreen.sample.locker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.buzzvil.buzzscreen.sdk.BuzzIntent;
import com.buzzvil.buzzscreen.sdk.BuzzOptions;
import com.buzzvil.buzzscreen.sdk.BuzzScreen;
import com.buzzvil.buzzscreen.sdk.UserProfile;
import com.buzzvil.buzzscreen.sdk.service.LockerServiceNotificationConfig;

import java.util.Random;

public class SampleActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

	private View buttonStart, buttonStop, buttonLockscreen;
	private Switch switchNews;
	Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);

		buttonStart = findViewById(R.id.buttonStart);
		buttonStop = findViewById(R.id.buttonStop);
		buttonLockscreen = findViewById(R.id.buttonLockscreen);
		switchNews = (Switch) findViewById(R.id.switchNews);

		buttonStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserProfile profile = new UserProfile.Builder("10000")
						//.setHmac("DnxyJVcRxxxx/SPEoLSVuRxtV4wRUj8SiQXoswRu3n8=")        // Optional
						.setBirthYear(1980)
						//.setBirthday("11-11-1980")                    // Optional
						.setGender(UserProfile.USER_GENDER_MALE)
						.build();
				// In order to apply options.
				onCheckedChanged(null, false);

				BuzzScreen.getInstance().setUserProfile(profile);
				BuzzScreen.getInstance().activate();
			}
		});

		buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BuzzScreen.getInstance().deactivate();
			}
		});

		buttonLockscreen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendBroadcast(new Intent(BuzzIntent.START_LOCK));
			}
		});

		switchNews.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		BuzzOptions.DefaultCampaign defaultCampaign1 = new BuzzOptions.DefaultCampaign();
		defaultCampaign1.setClickUrl("about:blank");
		defaultCampaign1.setResourceId(R.drawable.default_lock_sdk);

		BuzzOptions.DefaultCampaign defaultCampaign2 = new BuzzOptions.DefaultCampaign();
		defaultCampaign2.setClickUrl("about:blank");
		defaultCampaign2.setResourceId(R.drawable.default_lock_sample);

		BuzzScreen.getInstance().setOptions(
				new BuzzOptions.Builder().useNews(switchNews.isChecked())
						.setDefaultCampaign(defaultCampaign1)
						.addDefaultCampaign(defaultCampaign2)
						.setDebugMode(0).build());
		// BuzzScreen.runInBackground(true);
	}
}
