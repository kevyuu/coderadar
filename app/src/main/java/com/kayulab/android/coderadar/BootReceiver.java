package com.kayulab.android.coderadar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kayulab.android.coderadar.utility.PreferenceUtil;

/**
 * Created by kevinyu on 5/29/15.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DEBUG","BootReceiver.onReceive()");
        Log.d("DEBUG",intent.getAction());
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            boolean isReminderOn = PreferenceUtil.isReminderOn(context);
            if (!NotificationService.isAlarmOn(context) && isReminderOn) {
                NotificationService.startAlarm(context);
            }

        }
    }
}
