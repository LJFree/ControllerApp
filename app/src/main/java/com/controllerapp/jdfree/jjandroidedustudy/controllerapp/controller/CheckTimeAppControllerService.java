package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.AppControlActivity;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.Calendar;
import java.util.List;

public class CheckTimeAppControllerService extends Service implements Runnable {

    public static final String CONTROL_APP_MODEL = "ControlAppModel";
    public static final String TIME_OUT = "timeOut";
    private boolean isCheck;
    private List<AppListModel> mList;
    private Thread mThread;
    private TimeInfoNotification mNotification;

    //    private int RETURN_VALUE = START_REDELIVER_INTENT;
    private int RETURN_VALUE = START_STICKY;

    private IBinder mBinder = new CheckTimeAppControlBinder();
    private Intent mAppControlActivityIntent;
    private String currentPackageName;

    private int date;


    public class CheckTimeAppControlBinder extends Binder {
        public CheckTimeAppControllerService getService() {
            return CheckTimeAppControllerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    // 서비스 시작
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        mNotification = new TimeInfoNotification(this);

        startForeground(1, mNotification.getBuilder().build());
        isCheck = true;

        mThread = new Thread(this);
        mThread.start();
        if (intent != null) {
            mList = intent.getParcelableArrayListExtra(MainActivity.CHECK_CONTROLLER);
            currentPackageName = getApplicationContext().getPackageName();
        }
//        }

        return RETURN_VALUE;
    }

    // 서비스 종료
    @Override
    public void onDestroy() {
        if (mNotification != null) {
            RETURN_VALUE = START_NOT_STICKY;
            mNotification.hide();

            isCheck = false;
            if (mThread != null) {
                mThread = null;
            }
            this.stopSelf();
            super.onDestroy();
        }
    }

    // 1초마다 어떤 앱이 실행 되는지 파악
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        try {
            mAppControlActivityIntent = new Intent(this, AppControlActivity.class);

            UsageEvents usageEvents;
            TimeCalculationThread timeCalculation = new TimeCalculationThread();
            timeCalculation.setDaemon(true);

            while (isCheck) {
                Thread.sleep(1000);


                if (mList != null) {
                    initData();

                    String packageName;

                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 1000;

                    UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                    usageEvents = usageStatsManager.queryEvents(beginTime, endTime);

                    // 앱이 교체됐는지 매번 반복하여 파악
                    while (usageEvents.hasNextEvent()) {
                        UsageEvents.Event event = new UsageEvents.Event();
                        usageEvents.getNextEvent(event);

                        if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                            packageName = event.getPackageName();

                            timeCalculation.setPackageName(packageName);
                            // 다른 앱이 활성화 됐으며 제어 하려는 패키지가 일치할 경우
                            // 시간 체크 시작
                            if (!timeCalculation.isTime()) {

                                timeCalculation.setTime(true);
                                timeCalculation.start();
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            isCheck = false;
        }
    }

    // 데이터 초기화(00시 시작)
    private void initData() {
        if (date != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {

            for (int i = 0; i < mList.size(); i++) {
                AppListModel model = mList.get(i);

                model.setStartDayTime(0);
                model.setOverDayTime(model.getAllDayTime());

                mList.set(i, model);
            }
        }
    }


    // 시간 체크 이벤트
    private class TimeCalculationThread extends Thread {

        private boolean isTime = false;
        private String packageName;

        public boolean isTime() {
            return isTime;
        }

        public void setTime(boolean time) {
            isTime = time;
        }

        void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        private TimeCalculationThread() {
        }


        // 켜지는 동안 1초마다 +1초 사용 함으로 설정
        @Override
        public void run() {
            try {

                while (isTime) {
                    for (int i = 0; i < mList.size(); i++) {

                        if (packageName.equals(mList.get(i).getPackageName())) {
                            int timeSecond = mList.get(i).getStartDayTime() + 1;
                            int minute = mList.get(i).getOverDayTime();


                            if (minute <= 0) {

                                if (!packageName.equals(currentPackageName)) {
                                    // 사용하는 앱이 시간 초과할 경우 제어
                                    AppListModel list = mList.get(i);
                                    packageName = currentPackageName;

                                    mAppControlActivityIntent.putExtra(CONTROL_APP_MODEL, list);
                                    mAppControlActivityIntent.putExtra(TIME_OUT, true);
                                    mAppControlActivityIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(mAppControlActivityIntent);
                                }
                            } else {
                                if (timeSecond % 60 == 0) {
                                    minute--;
                                    mList.get(i).setOverDayTime(minute);
                                }

                                mList.get(i).setStartDayTime(timeSecond);
                            }
                            break;
                        }
                    }

                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
            } finally {
                isTime = false;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<AppListModel> getList() {
        return mList;
    }
}
