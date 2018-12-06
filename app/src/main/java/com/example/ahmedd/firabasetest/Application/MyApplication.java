package com.example.ahmedd.firabasetest.Application;

import android.app.Application;

import com.example.ahmedd.firabasetest.OneSignal.MyCustomNotificationHelper;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OneSignal;

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyCustomNotificationHelper(this))
                .init();

    }
}
