package com.forkread.notificationmanager.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import com.forkread.notificationmanager.Utils;
import com.forkread.notificationmanager.receiver.NotificationDataReceiver;
import com.forkread.notificationmanager.NotificationManagerApplication;

import java.util.Calendar;

/**
 * Created by dipesh on 02/08/15.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class ForkReadNotificationListenerService extends NotificationListenerService {
    private Context mContext;
    private SharedPreferences mPreferences;
    private BroadcastReceiver mReceiver;

    @Override

    public void onCreate() {
        super.onCreate();
        mContext = NotificationManagerApplication.getInstance();
        mPreferences = mContext.getSharedPreferences(Utils.PREF_KEY, Context.MODE_MULTI_PROCESS);
    }

    @Override
    public synchronized void onNotificationPosted(StatusBarNotification sbn) {
        Calendar calendar = Calendar.getInstance();
        String pack = sbn.getPackageName();
        boolean cancelNotifications = false;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (mPreferences.getBoolean(Utils.PREF_KEY_NIGHT_MODE_ENABLED, false)) {
            if (hour == 23 || hour <= 7) {
                cancelNotifications = true;
            }
        }
        if (!cancelNotifications) {
            if (mPreferences.getBoolean(Utils.PREF_KEY_CUSTOM_SETTING_ENABLED, false)) {
                int selectedStartHour = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_START_TIME_HOUR, 0);
                int selectedStartMinute = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_START_TIME_MINUTE, 0);
                int selectedEndHour = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_END_TIME_HOUR, 0);
                int selectedEndMinute = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_END_TIME_MINUTE, 0);

                if (hour == selectedStartHour && minute >= selectedStartMinute
                        && hour < selectedEndHour) {
                    cancelNotifications = true;
                }
                else if (hour > selectedStartHour && hour < selectedEndHour) {
                    cancelNotifications = true;
                }
                else if (hour > selectedStartHour && hour == selectedEndHour
                        && minute <= selectedEndMinute) {
                    cancelNotifications = true;
                }
            }
        }
        if (cancelNotifications) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                cancelNotification(pack, sbn.getTag(), sbn.getId());
            } else {
                cancelNotification(sbn.getKey());
            }
        }
        String ticker = "";
        if (sbn.getNotification().tickerText != null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text", "").toString();


        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(ticker)) {
            // Sending data to the db
            Intent intent = new Intent(NotificationDataReceiver.INTENT_NOTIFICATION_DATA);
            intent.putExtra("title", title);
            intent.putExtra("packageName", pack);
            intent.putExtra("ticker", ticker);
            intent.putExtra("text", text);
            mContext.sendBroadcast(intent);
        }
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg", "Notification Removed");
    }
}
