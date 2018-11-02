package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.StatsFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.deleteitem.DeleteDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.exit.ExitDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.MainRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.onClicked {

    private static final int APP_LIST_REQUEST_CODE = 1000;
    public static final String NOT_APP_LIST = "notAppList";
    public static final String SELECT_ALL_APP_INFO = "selectAllAppInfo";
    public static final String CHECK_CONTROLLER = "check_control";
    public static final String APP_LIST_SAVE = "mAppListSave";
    public static final String APP_LIST = "mAppList";
    public static final String APP_LIST_MODEL = "AppListModel";
    public static final int UPDATE_REQUEST_CODE = 1001;
    public static final String APP_LIST_POSITION = "appListPosition";

    public List<AppListModel> mAppList;
    public MainRecyclerViewAdapter mAdapter;
    private Intent mIntent;

    private CheckTimeAppControllerService mService;
    private boolean mBound;
    private LinearLayout mLinearLayoutFragmentStats;
    private StatsFragment mFragmentStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayoutFragmentStats = findViewById(R.id.liner_layout_fragment_main_stats);
        mLinearLayoutFragmentStats.setFocusableInTouchMode(true);

        mFragmentStats = (StatsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main_stats);

        mIntent = new Intent(this, CheckTimeAppControllerService.class);

        mAppList = new ArrayList<>();
        loadList();

        RecyclerView recyclerView = findViewById(R.id.app_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        mAdapter = new MainRecyclerViewAdapter(mAppList, getPackageManager());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.onSetClicked(this);

    }

    @Override
    public void onDataClicked(AppListModel model, Drawable icon) {
        mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);

        mFragmentStats.setModel(model, icon);

        mLinearLayoutFragmentStats.setVisibility(LinearLayout.VISIBLE);
        mLinearLayoutFragmentStats.requestFocus();
    }

    @Override
    public void onAddClicked() {
        Intent intent = new Intent(this, AllAppListControlActivity.class);

        intent.putParcelableArrayListExtra(NOT_APP_LIST, (ArrayList<? extends Parcelable>) mAppList);

        startActivityForResult(intent, APP_LIST_REQUEST_CODE);
    }

    @Override
    public void onLongClicked(int position) {
        mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);

        AppListModel model = mAppList.get(position);

        Intent intent = new Intent(this, AppStatsUpdateActivity.class);

        intent.putParcelableArrayListExtra(APP_LIST_MODEL, (ArrayList<? extends Parcelable>) mAppList);
        intent.putExtra(APP_LIST_POSITION, position);

        startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_LIST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Intent intent = data;
            AppListModel appModel = intent.getParcelableExtra(SELECT_ALL_APP_INFO);

            mAdapter.addItem(0, appModel);
            goService();

            listSave();
            Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
        } else if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Intent intent = data;

            int p = intent.getIntExtra(MainActivity.APP_LIST_POSITION, -1);

            if (p == -1) {
                mAppList = intent.getParcelableArrayListExtra(MainActivity.APP_LIST_MODEL);

                mAdapter.changeItem(mAppList);

                goService();
            } else {
                deleteListener(p);
            }
        }
    }

    public void deleteListener(int position) {
        mAdapter.removeItem(position);

        if (mAppList.size() == 0) {
            unbindService(mConnection);
            mBound = false;

            stopService(mIntent);
        } else {
            goService();
        }
        listSave();
        Toast.makeText(this, "앱 제어 삭제 완료", Toast.LENGTH_SHORT).show();
    }

    private void goService() {

        stopService(mIntent);

        mIntent.putParcelableArrayListExtra(CHECK_CONTROLLER, (ArrayList<? extends Parcelable>) mAppList);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mIntent);
        } else {
            startService(mIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);

        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        listSave();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            listSave();
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CheckTimeAppControllerService.CheckTimeAppControlBinder binder = (CheckTimeAppControllerService.CheckTimeAppControlBinder) service;

            mService = binder.getService();
            mBound = true;

            if (mService.getList() != null) {
                mAppList = mService.getList();
                mAdapter.changeItem(mAppList);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료
        }
    };


    private void listSave() {
        SharedPreferences preferences = getSharedPreferences(APP_LIST_SAVE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        StringBuilder appListModelStrings = new StringBuilder();

        for (AppListModel controlApp : mAppList) {
            appListModelStrings.append(controlApp.toString());
        }

        editor.putString(APP_LIST, appListModelStrings.toString());
        editor.apply();
    }

    private void loadList() {
        SharedPreferences preferences = getSharedPreferences(APP_LIST_SAVE, MODE_PRIVATE);

        String controlPackages = preferences.getString(APP_LIST, null);

        if (controlPackages != null && controlPackages.trim().length() != 0) {

            String[] splitRow = controlPackages.split(";");

            for (String row : splitRow) {
                String[] splitCol = row.split(",");

                String name = splitCol[0];
                String packageName = splitCol[1];

                int time = Integer.parseInt(splitCol[3]);

                AppListModel model = new AppListModel(name, packageName, time, time);

                mAppList.add(model);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mAppList.size() != 0) {
//            stopService(mIntent);

//            Intent intent = getPackageManager().getLaunchIntentForPackage(this.getPackageName());
//            startActivity(intent);

//            goService();
//        }
    }

    @Override
    public void onBackPressed() {
        if (mLinearLayoutFragmentStats.getVisibility() == LinearLayout.VISIBLE) {
            mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);
        } else {
            new ExitDialogFragment().show(getSupportFragmentManager(), "exit");
        }
    }
}
