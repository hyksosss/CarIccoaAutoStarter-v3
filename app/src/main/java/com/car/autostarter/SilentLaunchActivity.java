package com.car.autostarter;

import android.app.Activity;
import android.os.Bundle;

/** Launcher icon endpoint. Theme.NoDisplay guarantees no visible UI is drawn. */
public final class SilentLaunchActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        KeepAliveService.startSafely(this);
        finish();
    }
}
