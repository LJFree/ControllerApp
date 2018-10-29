package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CheckTimeAppControllerService extends Service implements Runnable {

    private boolean isCheck;
    private static List<AppListModel> mList;
    private MediaPlayer mPlayer;
    private Thread mThread;
    public static final String TAG = "abcasdf";
    private TimeInfoNotification mNotification;

    //    private int RETURN_VALUE = START_REDELIVER_INTENT;
    private int RETURN_VALUE = START_STICKY;

    int time = 0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if ("startForeground".equals(intent.getAction())) {
        mNotification = new TimeInfoNotification(this);
        mNotification.show();

//        } else if (mThread == null) {
        isCheck = true;

        mThread = new Thread(this);
        mThread.start();
        mPlayer = MediaPlayer.create(this, R.raw.song);
        mPlayer.setLooping(true);
        mPlayer.start();
        if (intent != null) {
            mList = intent.getParcelableArrayListExtra(MainActivity.CHECK_CONTROLLER);
        }
//        }
        return RETURN_VALUE;
    }

    @Override
    public void onDestroy() {
        RETURN_VALUE = START_NOT_STICKY;

        mNotification.hide();

        isCheck = false;
        mPlayer.stop();
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
        this.stopSelf();
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        try {
//            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            while (isCheck) {
                Thread.sleep(1000);

                if (mList != null) {

                    String packageName = null;
                    UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                    final long endTime = System.currentTimeMillis();
                    final long beginTime = endTime - 1000;
                    final UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);

                    while (usageEvents.hasNextEvent()) {
                        UsageEvents.Event event = new UsageEvents.Event();
                        usageEvents.getNextEvent(event);
                        if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                            packageName = event.getPackageName();

                            for (AppListModel list : mList) {
//                                Log.e(TAG, "all: " + packageName);
                                if (packageName.equals(list.getPackageName())) {
                                    time = Integer.parseInt(list.getAllDayTime());
                                    startActivity(mainIntent);

                                    currentOk(list);
                                }
                            }
                        }
                    }
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            isCheck = false;
//            mThread.stop();
//            mThread.destroy();
//            this.stopSelf();
        }
    }

    private void currentOk(AppListModel model) {

        time--;

        Log.e(TAG, "currentOk: " + time + model.getPackageName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
