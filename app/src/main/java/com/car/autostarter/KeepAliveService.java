package com.car.autostarter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

/**
 * A low-importance foreground service. Accessibility events are still handled only by
 * CarAccessibilityService; this service neither polls nor opens windows.
 */
public final class KeepAliveService extends Service {
    private static final String CHANNEL_ID = "car_autostarter";
    private static final int NOTIFICATION_ID = 1001;

    static void startSafely(Context context) {
        try {
            Intent intent = new Intent(context, KeepAliveService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(intent);
            else context.startService(intent);
        } catch (RuntimeException ignored) {
            // Vendor ROM policy may reject a background start; do not crash the boot path.
        }
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        createChannel();
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.car.autostarter.R.drawable.ic_launcher)
                .setContentTitle("ICCOA 自动启动服务")
                .setContentText("正在后台运行")
                .setOngoing(true)
                .build();
        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "ICCOA 自动启动", NotificationManager.IMPORTANCE_MIN);
        channel.setShowBadge(false);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}
