package com.assignment.programming.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.assignment.programming.BuildConfig;
import com.assignment.programming.MainActivity;
import com.assignment.programming.R;
import com.assignment.programming.data.FileModel;

import java.io.File;

public final class NotificationHelper {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "com.assignment.programming.utils.NotificationHelper.CHANNEL_ID";

    private NotificationHelper() {
    }

    public static void createMatchesFoundNotification(Context context, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setContentTitle(text);
        Intent intent = new Intent(context, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);
        nm.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void createResultFileNotification(Context context, FileModel fileModel) {
        Uri uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(fileModel.getPath())
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder.setContentTitle(context.getString(R.string.programming_assignment_result_file));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "text/plain");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);
        nm.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        context.getString(R.string.programming_assignment_channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}
