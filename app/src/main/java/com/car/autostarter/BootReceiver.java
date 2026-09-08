package com.car.autostarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Starts only an invisible foreground keeper; it never launches an Activity. */
public final class BootReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        KeepAliveService.startSafely(context);
    }
}
