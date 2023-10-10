package com.joaoandrade.passwordarchive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.joaoandrade.passwordarchive.View.HomeActivity;
import com.joaoandrade.passwordarchive.View.LoginActivity;
import com.joaoandrade.passwordarchive.View.SettingsActivity;

public class Notificacoes extends ContextWrapper {

    public Notificacoes(Context base){
        super(base);
        createNotificationChannel();
    }
    private final String CHANNEL_ID = "1";
    private int notificationId = 12345678;

    private final CharSequence name = "Channel notifications";
    private final String description = "Channel for notifications of Password Archive";



    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,500,200,340});
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setDescription(description);
            NotificationManager notificationManager =  getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


    @SuppressLint("MissingPermission")
    public void notificacao( String titulo, String descricao) {
        notificationId = notificationId + 1;
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(titulo)
                .setContentText(descricao)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(descricao))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(notificationId, builder.build());
    }


}
