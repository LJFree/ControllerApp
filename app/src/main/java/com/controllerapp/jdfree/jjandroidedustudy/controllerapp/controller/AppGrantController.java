package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.Collator;
import java.util.Currency;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppGrantController {


    public static boolean isGrant(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_CONTACTS)) {

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                Toast.makeText(context, "asdfasdf", Toast.LENGTH_SHORT).show();
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
                return false;
            } else {
                Toast.makeText(context, "asdfasdf111111111", Toast.LENGTH_SHORT).show();

//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_CONTACTS, MY_PERMISSIONS_REQUEST_READ_CONTACTS});

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다
            }

        }
        Toast.makeText(context, "asdfasdf111111111222222222", Toast.LENGTH_SHORT).show();
        return true;
    }


    public AppGrantController() {










        /*
        // GET_USAGE_STATS 권한 확인
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) GlobalClass.getInstance().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), GlobalClass.getInstance().getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (GlobalClass.getInstance().checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        L.e(TAG, "===== CheckPhoneState isRooting granted = " + granted);

        if (granted == false) {
            // 권한이 없을 경우 권한 요구 페이지 이동
            Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
            GlobalClass.getInstance().startActivity(intent);
        }

        if (!isRooting) {
            // 기타 프로세스 목록 확인
            UsageStatsManager usage = (UsageStatsManager) GlobalClass.getInstance().getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    runningTask.put(usageStats.getLastTimeUsed(), usageStats);

                    L.e(TAG, "===== CheckPhoneState isRooting packageName = " + usageStats.getPackageName());
                }
            } else {
                L.e(TAG, "===== CheckPhoneState isRooting stats is NULL");
            }
        }
    */
    }
}
