package com.buzzvil.buzzscreen.sample.locker;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.buzzvil.buzzscreen.sdk.BuzzScreen;

/**
 * SampleApplication.java
 *
 * @author Hovan Yoo
 */
public class SampleApplication extends Application {

	static final String TAG = SampleApplication.class.getSimpleName();

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		BuzzScreen.getInstance().init("[YOUR_APP_KEY]", this, CustomLockActivity.class);
	}
}
