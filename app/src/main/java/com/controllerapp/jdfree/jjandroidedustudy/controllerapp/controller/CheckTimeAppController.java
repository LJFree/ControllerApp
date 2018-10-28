package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CheckTimeAppController extends Service implements Runnable {

    private boolean isCheck;
    private List<AppListModel> mList;
    private MediaPlayer mPlayer;
    private Thread mThread;
    public static final String TAG = "abcasdf";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand: ");
        if (intent != null) {
            mList = intent.getParcelableArrayListExtra(MainActivity.CHECK_CONTROLLER);
        }

        isCheck = true;

        mThread = new Thread(this);
        mPlayer = MediaPlayer.create(this, R.raw.song);
        mPlayer.setLooping(true);
        mPlayer.start();
        mThread.start();
//        abc();


//                putParcelableArrayListExtra

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: " + "exit");
        isCheck = false;
        mPlayer.stop();
//        mThread.interrupt();
//        mThread = null;
        super.onDestroy();
    }

    @Override
    public void run() {
        try {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
            startActivity(launchIntent);

            while (isCheck) {
                Thread.sleep(1000);


                Log.e(TAG, "run: ");
                if (mList != null) {
//                    ActivityManager mgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//                    List<ActivityManager.RunningTaskInfo> tasks = mgr.getRunningTasks(1);


//                    String currentApp = tasks.get(0).topActivity.getPackageName();


                    String currentApp = getApplicationContext().getPackageName();

                    Log.e(TAG, "run: " + currentApp);
//                        PackageManager packageManager = getCon.getPackageManager();
//                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);

                }

//                startActivity(launchIntent);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            isCheck = false;
//            mThread.stop();
//            mThread.destroy();
//            this.stopSelf();
        }
        /*
        Process process = null;

        try {
            Runtime.getRuntime().exec("/system/bin/logcat -c");
<<<<<<< HEAD

            process = Runtime.getRuntime().exec("/system/bin/logcat -b main -s ActivityManager:I");
=======
<<<<<<< HEAD

            process = Runtime.getRuntime().exec("/system/bin/logcat -b main -s ActivityManager:I");


=======

            process = Runtime.getRuntime().exec("/system/bin/logcat -b main -s ActivityManager:I");


>>>>>>> ff548d4d78312d9db7da7b2afe1ceb3f0c3a3cc0
>>>>>>> 8d382062446056acbe96aa13b0515b41858413d0
        } catch (IOException e) {
//            e.printStackTrace();

            Log.e(getPackageName(), e.toString());
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;


            while (isCheck) {
                line = reader.readLine();
                Log.e("dd", "run: " + "ffffffffffffffffffffffffffff" + line);

//                for (int i = 0; i < mList.size(); i++) {
//                    Log.e("dd", "run: " + line);

//                }
            }

        } catch (IOException e) {
            Log.e(getPackageName(), e.toString());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(getPackageName(), e.toString());
            }

            process.destroy();
            process = null;
            mThread.stop();
            mThread.destroy();
            reader = null;
            this.stopSelf();
        }

        */
        /*
        while (isCheck) {

            if (mList == null) {
//                Toast.makeText(this, "mList 받는 값 없음.", Toast.LENGTH_SHORT).show();
//                break;
            }

            for (int i = 0; i < mList.size(); i++) {
                Log.e("fff", "run: " + mList.get(i));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        */
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
