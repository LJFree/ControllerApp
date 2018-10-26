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

import java.util.ArrayList;
import java.util.List;

public class CheckTimeAppController extends Service implements Runnable {

    private boolean isCheck = true;
    private List<AppListModel> mList;
    private MediaPlayer mPlayer;
    private Thread mThread;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        mList = intent.getParcelableArrayListExtra(MainActivity.CHECK_CONTROLLER);

        mThread = new Thread(this);
        mPlayer = MediaPlayer.create(this, R.raw.song);
        mPlayer.setLooping(true);
        mPlayer.start();
        mThread.start();


//                putParcelableArrayListExtra

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isCheck = false;
        mPlayer.stop();
        mThread.interrupt();
        mThread = null;
        super.onDestroy();
    }

    @Override
    public void run() {

        while (isCheck) {
            if (mList == null) {
//                Toast.makeText(this, "mList 받는 값 없음.", Toast.LENGTH_SHORT).show();
//                break;
            }

            for (int i = 0; i < mList.size(); i++) {
//                Log.e("fff", "run: " + mList.get(i));
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
