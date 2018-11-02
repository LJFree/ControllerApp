package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;

public class TimeInfoNotification {

    private final Context context;

    public TimeInfoNotification(Context context) {
        this.context = context;
    }

    public NotificationCompat.Builder getBuilder() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        // 필수 항목
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("앱 제어 중");
        builder.setContentText("제어 중인 앱을 확인하시려면 클릭하세요!");

        // 액션 정의
//        Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 클릭 이벤트 설정
        builder.setContentIntent(pendingIntent);

        // 큰 아이콘 설정
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
        builder.setLargeIcon(largeIcon);

        builder.setSmallIcon(R.mipmap.logo);

        // 색상 변경
        builder.setColor(Color.RED);

        // 기본 알림음 사운드 설정
//        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(ringtoneUri);

        // 진동설정: 대기시간, 진동시간, 대기시간, 진동시간 ... 반복 패턴
        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        // 알림 매니저
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // 오레오에서 알림 채널을 매니저에 생성해야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널 생성
            NotificationChannel channel = new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("채널 설명");
            channel.enableLights(true);

            // 기기가 지원하면 기기에서 깜빡이는 불빛 색을 지정
//                channel.setLightColor(Color.RED);
//                channel.enableVibration(true);
//                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            manager.createNotificationChannel(channel);
        }

        // 알림 통지
//            manager.notify(1, builder.build());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(1, builder.build());
//            }

        return builder;
    }

    public void hide() {
        NotificationManagerCompat.from(context).cancel(1);
    }
}
