package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.StatsFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.ExitDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.MainRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
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

    private boolean mBound;
    private LinearLayout mLinearLayoutFragmentStats;
    private StatsFragment mFragmentStats;

    private int dataSelectPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayoutFragmentStats = findViewById(R.id.liner_layout_fragment_main_stats);   // 리니어 레이아웃(비활성화 위한)
        mLinearLayoutFragmentStats.setFocusableInTouchMode(true);   // 선택 가능

        mFragmentStats = (StatsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main_stats);    // 앱 관리 정보 프리그먼트

        mIntent = new Intent(this, CheckTimeAppControllerService.class);    // 서비스

        mAppList = new ArrayList<>();   // 제어 관리 앱
        loadList(); // 데이터 불러오기

        RecyclerView recyclerView = findViewById(R.id.app_list_recycler_view);  // 리사이클러뷰

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);  // 리사이클러뷰 레이아웃
        mAdapter = new MainRecyclerViewAdapter(mAppList, getPackageManager());  // 리사이클러뷰 어뎁터


        recyclerView.setLayoutManager(layoutManager);   // 리사이클러뷰 레이아웃 설정
        recyclerView.setAdapter(mAdapter);  // 리사이클러뷰 어뎁터 설정

        mAdapter.onSetClicked(this);    // 리사이클러뷰 이벤트

        if (mAppList.size() != 0) {
            goService();
        }

    }

    // ICon 클릭 이벤트(Data 부분)
    @Override
    public void onDataClicked(int position) {
        mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);    // 리니어 레이아웃(앱 관리 정보 프리그 먼트) 비활성화

        mFragmentStats.setModel(mAppList.get(position), getPackageManager());   // 앱 관리 정보 데이터 수정

        mLinearLayoutFragmentStats.setVisibility(LinearLayout.VISIBLE);     // 리니어 레이아웃(앱 관리 정보 프리그 먼트) 활성화
        mLinearLayoutFragmentStats.requestFocus();    // 리니어 레이아웃(앱 관리 정보 프리그 먼트) 선택

        this.dataSelectPosition = position; // 선택 포지션 저장
    }

    // ICon 클릭 이벤트(맨 뒤 + ICon)
    @Override
    public void onAddClicked() {

        mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);

        Intent intent = new Intent(this, AllAppListControlActivity.class);
        intent.putParcelableArrayListExtra(NOT_APP_LIST, (ArrayList<? extends Parcelable>) mAppList);   // 데이터 전달
        startActivityForResult(intent, APP_LIST_REQUEST_CODE);
    }

    // ICon 긴 클릭 이벤트(데이터 수정, 삭제 관리 페이지로 이동)
    @Override
    public void onLongClicked(int position) {
        mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);

        Intent intent = new Intent(this, AppStatsUpdateActivity.class);

        intent.putParcelableArrayListExtra(APP_LIST_MODEL, (ArrayList<? extends Parcelable>) mAppList); // 데이터 전달
        intent.putExtra(APP_LIST_POSITION, position);

        startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }

    // 다른 페이지에서 반환 받는 이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 데이터 추가 부분
        if (requestCode == APP_LIST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            AppListModel appModel = data.getParcelableExtra(SELECT_ALL_APP_INFO);   // 데이터 받기

            mAdapter.addItem(0, appModel);  // 데이터 추가
            goService();    // 서비스 시작

            listSave(); // 데이터 저장
            Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();


            // 데이터 수정 및 삭제
        } else if (requestCode == UPDATE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            int p = data.getIntExtra(MainActivity.APP_LIST_POSITION, -1);

            if (p == -1) {
                mAppList = data.getParcelableArrayListExtra(MainActivity.APP_LIST_MODEL);   // 데이터 받기

                mAdapter.changeItem(mAppList);  // 데이터 수정

                goService();    // 서비스 재 시작
            } else {
                deleteListener(p);  // 데이터 삭제
            }
        }
    }

    // 삭제 시 이벤트
    public void deleteListener(int position) {
        mAdapter.removeItem(position);  // 데이터 삭제


        if (mAppList.size() == 0) {
            // 데이터가 없으면 서비스 종료
            mBound = false;
            stopService(mIntent);
        } else {
            // 데이터가 있으면 서비스 재시작
            goService();
        }

        // 데이터 저장
        listSave();
        Toast.makeText(this, "앱 제어 삭제 완료", Toast.LENGTH_SHORT).show();
    }


    // 서비스 재시작
    private void goService() {
        // 서비스 종료
        stopService(mIntent);

        mIntent.putParcelableArrayListExtra(CHECK_CONTROLLER, (ArrayList<? extends Parcelable>) mAppList);  // 데이터 보내기

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mIntent);    // 포그라운드 시작
        } else {
            startService(mIntent);  // 서비스 시작
        }
    }

    // 화면 보일 때 이벤트
    @Override
    protected void onStart() {
        super.onStart();
        // bind 서비스 시작
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        listSave(); // 데이터 저장
    }

    // 화면이 안보일 때 이벤트
    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection); // bind 서비스 중지
            mBound = false;
        }
        listSave(); // 데이터 저장
    }

    // bind 서비스 이벤트
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CheckTimeAppControllerService.CheckTimeAppControlBinder binder = (CheckTimeAppControllerService.CheckTimeAppControlBinder) service;

            CheckTimeAppControllerService mService = binder.getService();
            mBound = true;

            if (mService.getList() != null && mService.getList().size() != 0) {
                // 내용이 있다면 데이터 새로고침

                mAppList = mService.getList();  //
                mAdapter.changeItem(mAppList);

                // 리니어 레이아웃이 활성화 됐다면 자동 새로고침
                if (mLinearLayoutFragmentStats.getVisibility() == LinearLayout.VISIBLE) {
                    AppListModel model = mAppList.get(dataSelectPosition);

                    mFragmentStats.setModel(model, getPackageManager());
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료
        }
    };


    // 데이터 SharedPreferences 로 저장
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

    // 데이터 불러오기
    private void loadList() {
        SharedPreferences preferences = getSharedPreferences(APP_LIST_SAVE, MODE_PRIVATE);

        String controlPackages = preferences.getString(APP_LIST, null);

        if (controlPackages != null && controlPackages.trim().length() != 0) {

            String[] splitRow = controlPackages.split(";");

            for (String row : splitRow) {
                String[] splitCol = row.split(",");

                String name = splitCol[0];
                String packageName = splitCol[1];
                int allDayTime = Integer.parseInt(splitCol[2]);
                int overDayTime = Integer.parseInt(splitCol[3]);
                int startDayTime = Integer.parseInt(splitCol[4]);

                AppListModel model = new AppListModel(name, packageName, allDayTime, overDayTime, startDayTime);

                mAppList.add(model);
            }
        }
    }

    // 종료 이벤트
    @Override
    protected void onDestroy() {
        super.onDestroy();
        listSave();
    }

    // 뒤로가기 버튼 클릭 이벤트
    @Override
    public void onBackPressed() {
        if (mLinearLayoutFragmentStats.getVisibility() == LinearLayout.VISIBLE) {
            mLinearLayoutFragmentStats.setVisibility(LinearLayout.GONE);
        } else {
            new ExitDialogFragment().show(getSupportFragmentManager(), "exit");
        }
    }
}
