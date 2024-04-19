package com.mirea.kimpm.serviceapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    static public final String RESOURCE_TITLE = "PlayerServiceResourceTitle";
    static public final String RESOURCE_ID = "PlayerServiceResourceId";
    private MediaPlayer mediaPlayer;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StopPlayingProcess();
        StartPlayingProcess(intent.getStringExtra(RESOURCE_TITLE), intent.getIntExtra(RESOURCE_ID, -1));
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    @SuppressLint("ForegroundServiceType")
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        StopPlayingProcess();
    }

    @SuppressLint("ForegroundServiceType")
    private void StartPlayingProcess(String title, int resource_id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Playing....")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(title))
                .setContentTitle("Music Player");
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Student Kim Polina", importance);
        channel.setDescription("MIREA Channel");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        startForeground(1, builder.build());

        mediaPlayer = MediaPlayer.create(this, resource_id);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                StopPlayingProcess();
            }
        });
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }
    private void StopPlayingProcess() {
        if(mediaPlayer != null) {
            stopForeground(true);
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}