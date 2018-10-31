package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.AppListRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
import java.util.List;

public class AppControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_control);

        Intent intent = getIntent();

        if (intent != null) {
            RecyclerView recyclerView = findViewById(R.id.recycler_app_control);
            TextView textView = findViewById(R.id.text_control_message);

            AppListModel model = intent.getParcelableExtra(CheckTimeAppControllerService.CONTROL_APP_MODEL);

            String packageName = model.getPackageName();
            String appName = model.getName();
            int time = model.getAllDayTime();

            AppListRecyclerViewAdapter adapter = new AppListRecyclerViewAdapter(packageName, getPackageManager());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);

            textView.setText(appName + " 사용 시간 완료 " + time + "분");
        }
    }
}
