package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.deleteitem.DeleteDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.exit.ExitDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.MainRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainRecyclerViewAdapter.onClicked, DeleteDialogFragment.onDeleteListener {

    private static final int REQUEST_CODE = 1000;
    public static final String NOT_APP_LIST = "notAppList";
    public static final String SELECT_ALL_APP_INFO = "selectAllAppInfo";

    public List<AppListModel> mAppList = new ArrayList<>();
    public MainRecyclerViewAdapter mAdapter = new MainRecyclerViewAdapter(mAppList);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.app_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 5);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setPm(getPackageManager());
        mAdapter.onSetClicked(this);

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

            Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();

//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//            SharedPreferences.Editor editor = preferences.edit();

//            editor.putString("infoApp", info.packageName);

//            editor.apply();

        }
    }

    @Override
    public void deleteListener(int position) {
        mAdapter.removeItem(position);
    }
}
