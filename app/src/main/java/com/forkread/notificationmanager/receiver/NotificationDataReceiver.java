package com.forkread.notificationmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.forkread.notificationmanager.database.DatabaseContracts;

/**
 * Created by dipesh on 04/08/15.
 */
public class NotificationDataReceiver extends BroadcastReceiver {
    public static final String INTENT_NOTIFICATION_DATA = "INTENT_NOTIFICATION_DATA";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (INTENT_NOTIFICATION_DATA.equalsIgnoreCase(intent.getAction())) {
            Bundle extra = intent.getExtras();

            ContentValues cv = new ContentValues();
            cv.put(DatabaseContracts.Notifications.NOTIFICATION_APP_NAME, extra.getString("packageName"));
            cv.put(DatabaseContracts.Notifications.NOTIFICATION_TEXT, extra.getString("text", ""));
            cv.put(DatabaseContracts.Notifications.NOTIFICATION_TICKER_TEXT, extra.getString("ticker", ""));
            cv.put(DatabaseContracts.Notifications.NOTIFICATION_TITLE, extra.getString("title", ""));
            cv.put(DatabaseContracts.Notifications.NOTIFICATION_TIMESTAMP, System.currentTimeMillis());
            context.getContentResolver().insert(DatabaseContracts.Notifications.CONTENT_URI, cv);
        }
    }
}
