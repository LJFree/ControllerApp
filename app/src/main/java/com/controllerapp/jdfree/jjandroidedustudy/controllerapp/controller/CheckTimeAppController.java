package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller;

import android.app.Service;
import android.content.Intent;
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

        if (intent != null) {
            mList = intent.getParcelableArrayListExtra(MainActivity.CHECK_CONTROLLER);

            if (mList.get(0).getAppInfo() != null){
                Log.e(TAG, "onStartCommand: ffffffffffffffffffffffffffffffffffffffffff" );
            }
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

    private void abc() {
        try {

            while (isCheck) {
                Thread.sleep(1000);

                Log.e(TAG, "run: ");
                if (mList != null) {
<<<<<<< HEAD
=======
                    if(mList.get(0).getAppInfo() != null){
                        Log.e(TAG, "abc: fsdfsdfdsf");
                    }
                    Log.e(TAG, "run: " + mList.size() + mList.get(5).getAppInfo().packageName);
>>>>>>> ff548d4d78312d9db7da7b2afe1ceb3f0c3a3cc0
                }
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.package.address");
                startActivity(launchIntent);

//                Intent i = new Intent();
//                i.setAction(Intent.ACTION_MAIN);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                i.addCategory(Intent.CATEGORY_HOME);
//                startActivity(i);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            isCheck = false;
//            mThread.stop();
//            mThread.destroy();
//            this.stopSelf();
        }
    }

    @Override
    public void run() {
        try {

            while (isCheck) {
                Thread.sleep(1000);

                Log.e(TAG, "run: ");
                if (mList != null) {
                    Log.e(TAG, "run: " + mList.size());
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
        /*
        Process process = null;

        try {
            Runtime.getRuntime().exec("/system/bin/logcat -c");
<<<<<<< HEAD

            process = Runtime.getRuntime().exec("/system/bin/logcat -b main -s ActivityManager:I");


=======

            process = Runtime.getRuntime().exec("/system/bin/logcat -b main -s ActivityManager:I");


>>>>>>> ff548d4d78312d9db7da7b2afe1ceb3f0c3a3cc0
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
