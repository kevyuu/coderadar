package com.kayulab.android.coderadar.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by kevinyu on 8/9/15.
 */
public abstract class SyncStatusReceiver extends BroadcastReceiver {

    private static IntentFilter sIntentFilter = new IntentFilter();
    public static final String ACTION_SYNC_START = SyncStatusReceiver.class.getName()+".ACTION_SYNC_START";
    public static final String ACTION_SYNC_STOP = SyncStatusReceiver.class.getName()+".ACTION_SYNC_STOP";
    public static final String ACTION_SYNC_ERROR = SyncStatusReceiver.class.getName()+".ACTION_SYNC_ERROR";

    static {
        sIntentFilter.addAction(ACTION_SYNC_START);
        sIntentFilter.addAction(ACTION_SYNC_STOP);
        sIntentFilter.addAction(ACTION_SYNC_ERROR);
    }

    public static IntentFilter getIntentFilter() {
        return sIntentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        if (intentAction.equals(ACTION_SYNC_START)) {
            onSyncStart();
        } else if (intentAction.equals(ACTION_SYNC_STOP)) {
            onSyncFinish();
        } else if (intentAction.equals(ACTION_SYNC_ERROR)) {
            onSyncError();
        }

    }

    public abstract void onSyncStart();

    public abstract void onSyncFinish();

    public abstract void onSyncError();

}
