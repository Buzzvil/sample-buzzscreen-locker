# Buzzscreen SDK for slidejoy publishers

- Buzzvil's library for integrating BuzzScreen with Android apps.
- Requires Android version 4.1 (API level 16) or newer.

### 1. Add library

- Add to build.gradle:

```groovy
allprojects {
    repositories {
        jcenter()
        maven { url 'https://dl.bintray.com/buzzvil/maven/' }
    }
}


dependencies {
    compile("com.buzzvil.buzzscreen:buzzscreen-sdk:+")
}

```


### 2. Application class

[SampleApplication](https://github.com/Buzzvil/sample-buzzscreen-locker/blob/master/app/src/main/java/com/buzzvil/buzzscreen/sample/locker/SampleApplication.java)

#### 2.1. Init SDK

1. Add `BuzzScreen.getInstance().init("APP_KEY", this, CustomLockActivity.class)` to onCreate in your Application class.
2. Don't use multi threading or call from outside of onCreate() because SDK needs the application context at the first time.

```java
public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		BuzzScreen.getInstance().init("APP_KEY", this, CustomLockActivity.class);
	}
}
```

#### 2.2. Register callback for impression tracking (Optional)

```java
// An activity class which inherited the SimpleLockerActivity
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setOnTrackingListener(new OnTrackingListener() {
		@Override
		public void onImpression(Bundle bundle) {
			// ref)https://docs.google.com/spreadsheets/d/12pWkgO9i-FnH7-6AS2inlWiLeRAfK5SP8U5ATXQX3sU/edit?usp=sharing
		}

		@Override
		public void onClick(Bundle bundle) {
			// ref)https://docs.google.com/spreadsheets/d/12pWkgO9i-FnH7-6AS2inlWiLeRAfK5SP8U5ATXQX3sU/edit?usp=sharing
		}
	});
}
```

### 3. Activity class

[SampleActivity](https://github.com/Buzzvil/sample-buzzscreen-locker/blob/master/app/src/main/java/com/buzzvil/buzzscreen/sample/locker/SampleActivity.java)


1. Provide UserProfile and BuzzOptions to SDK.
2. Call activate(), then SDK will start a service and show lockscreen.

#### 3.1. Set UserProfile

- UserID should be provided.
- Birth Year / Birthday / Gender / Hmac

```java
UserProfile profile = new UserProfile.Builder("TempUser_" + random.nextInt(100))
	.setBirthYear(1970 + random.nextInt(20))
	.setGender(random.nextBoolean() ? UserProfile.USER_GENDER_MALE : UserProfile.USER_GENDER_FEMALE)
	.setHmac("ABCDE")
	.setBirthday("1984-06-07")
	.build();
						
BuzzScreen.getInstance().setUserProfile(profile);
```

#### 3.2. Set BuzzOptions

- Use News
- Use Notification and Shortcuts
- Default lock images resources / Use default lock only
	
```java
BuzzOptions.DefaultCampaign defaultCampaign1 = new BuzzOptions.DefaultCampaign();
defaultCampaign1.setCreativeHeight(790);
defaultCampaign1.setCreativeWidth(640);
defaultCampaign1.setClickUrl("about:blank");
defaultCampaign1.setResourceId(R.drawable.default_lock_sdk);

BuzzOptions.DefaultCampaign defaultCampaign2 = new BuzzOptions.DefaultCampaign();
defaultCampaign2.setCreativeHeight(790);
defaultCampaign2.setCreativeWidth(640);
defaultCampaign2.setClickUrl("about:blank");
defaultCampaign2.setResourceId(R.drawable.default_lock_sample);

BuzzScreen.getInstance().setOptions(
        new BuzzOptions.Builder().useNews(switchNews.isChecked())
                .setDefaultCampaign(defaultCampaign1)
                .addDefaultCampaign(defaultCampaign2)
                .setDebugMode(1).build());
						
```

#### 3.3. Start lock screen service

```java
BuzzScreen.getInstance().activate();
```

#### 3.4. Stop lock screen service

```java
BuzzScreen.getInstance().deactivate();
```

### 4. Proguard
