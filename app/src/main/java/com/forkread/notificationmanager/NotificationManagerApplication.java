package com.forkread.notificationmanager;

import android.app.Application;

/**
 * Created by dipesh on 02/08/15.
 */
public class NotificationManagerApplication extends Application {

    private static NotificationManagerApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static NotificationManagerApplication getInstance() {
        return sInstance;
    }
}
