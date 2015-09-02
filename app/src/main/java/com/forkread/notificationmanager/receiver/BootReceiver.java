package com.forkread.notificationmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.forkread.notificationmanager.service.ForkReadNotificationListenerService;

/**
 * Created by dipesh on 07/08/15.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equalsIgnoreCase(action)
                || Intent.ACTION_REBOOT.equalsIgnoreCase(action)) {
            Intent service = new Intent(context, ForkReadNotificationListenerService.class);
            context.startService(service);
        }
    }
}
