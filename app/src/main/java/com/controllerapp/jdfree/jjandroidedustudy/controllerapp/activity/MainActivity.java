package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppController;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.deleteitem.DeleteDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.exit.ExitDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.MainRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.onClicked, DeleteDialogFragment.onDeleteListener {

    private static final int REQUEST_CODE = 1000;
    public static final String NOT_APP_LIST = "notAppList";
    public static final String SELECT_ALL_APP_INFO = "selectAllAppInfo";
    public static final String CHECK_CONTROLLER = "check_control";
    public static final String APP_LIST_SAVE = "mAppListSave";
    public static final String APP_LIST = "mAppList";

    public List<AppListModel> mAppList;
    public MainRecyclerViewAdapter mAdapter;
    private Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntent = new Intent(this, CheckTimeAppController.class);

        mAppList = new ArrayList<>();
        loadList();

        RecyclerView recyclerView = findViewById(R.id.app_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 5);
        mAdapter = new MainRecyclerViewAdapter(mAppList, getPackageManager());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.onSetClicked(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void listSave() {
        SharedPreferences preferences = getSharedPreferences(APP_LIST_SAVE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String controlPackages = "";

        for (AppListModel controlApp : mAppList) {
            controlPackages += controlApp.getPackageName() + "," + controlApp.getAllDayTime() + ";";
        }

        editor.putString(APP_LIST, controlPackages);
        editor.apply();

    }

    private void loadList() {
        SharedPreferences preferences = getSharedPreferences(APP_LIST_SAVE, MODE_PRIVATE);

        String controlPackages = preferences.getString(APP_LIST, null);

        PackageManager pm = getPackageManager();

        if (controlPackages != null && controlPackages.trim().length() != 0) {
            String[] splitRow = controlPackages.split(";");

            for (String row : splitRow) {
                String[] splitCol = row.split(",");

                AppListModel model = new AppListModel();

                try {
                    PackageInfo pi = pm.getPackageInfo(splitCol[0], pm.GET_META_DATA);

                    ApplicationInfo info = pi.applicationInfo;
                    model.setAppInfo(info);
                    model.setIcon(pm);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                model.setAllDayTime(splitCol[1]);

                mAppList.add(model);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new ExitDialogFragment().show(getSupportFragmentManager(), "exit");
    }

    @Override
    public void onClicked(AppListModel model) {

    }

    @Override
    public void onAddClicked() {
        Intent intent = new Intent(this, AllAppListControlActivity.class);

        intent.putParcelableArrayListExtra(NOT_APP_LIST, (ArrayList<? extends Parcelable>) mAppList);

        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onLongClicked(int position) {

        new DeleteDialogFragment().setDeleteListener(this, position).show(getSupportFragmentManager(), "delete");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            AppListModel appModel = data.getParcelableExtra(SELECT_ALL_APP_INFO);

            appModel.setIcon(getPackageManager());

            mAdapter.addItem(0, appModel);

            mIntent.putParcelableArrayListExtra(CHECK_CONTROLLER, (ArrayList<? extends Parcelable>) mAppList);

            startService(mIntent);

            listSave();
            Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteListener(int position) {
        mAdapter.removeItem(position);
        stopService(mIntent);

        listSave();
        Toast.makeText(this, "앱 제어 삭제 완료", Toast.LENGTH_SHORT).show();
    }


}
