package com.car.autostarter;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;

/** Watches only foreground-window transitions; no polling is used. */
public final class CarAccessibilityService extends AccessibilityService {
    private static final long START_DELAY_MS = 100L;
    private static final String PKG_360 = "com.baony.avm360";
    private static final String PKG_LAUNCHER = "com.android.launcher";
    private static final ComponentName ICCOA = new ComponentName(
            "com.ucarhu.demo", "com.ucarhu.demo.UCarDemoActivity");
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isFrom360;
    private Runnable pendingStart;

    @Override public void onServiceConnected() {
        super.onServiceConnected();
        KeepAliveService.startSafely(this);
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getPackageName() == null) return;
        String pkg = event.getPackageName().toString();
        if (PKG_360.equals(pkg)) {
            isFrom360 = true;
            cancelPendingStart();
        } else if (PKG_LAUNCHER.equals(pkg) && isFrom360) {
            isFrom360 = false;
            scheduleIccoaStart();
        }
        // Other foreground packages intentionally do not change state.
    }

    private void scheduleIccoaStart() {
        cancelPendingStart();
        pendingStart = () -> {
            Intent intent = new Intent().setComponent(ICCOA).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try { startActivity(intent); } catch (Exception ignored) { }
        };
        handler.postDelayed(pendingStart, START_DELAY_MS);
    }
    private void cancelPendingStart() {
        if (pendingStart != null) handler.removeCallbacks(pendingStart);
        pendingStart = null;
    }
    @Override public void onInterrupt() { cancelPendingStart(); }
}
