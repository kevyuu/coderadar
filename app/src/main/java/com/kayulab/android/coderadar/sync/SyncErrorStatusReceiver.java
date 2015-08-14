package com.kayulab.android.coderadar.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by kevinyu on 8/9/15.
 */
public abstract class SyncErrorStatusReceiver extends BroadcastReceiver {

    private static IntentFilter sIntentFilter = new IntentFilter();
    public static final String ACTION_SYNC_ERROR = SyncErrorStatusReceiver.class.getName()+".ACTION_SYNC_ERROR";

    static {
        sIntentFilter.addAction(ACTION_SYNC_ERROR);
    }

    public static IntentFilter getIntentFilter() {
        return sIntentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String intentAction = intent.getAction();
        if (intentAction.equals(ACTION_SYNC_ERROR)) {
            onSyncError();
        }

    }

    public abstract void onSyncError();

}
