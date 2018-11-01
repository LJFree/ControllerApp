package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
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
            ImageView imageView = findViewById(R.id.image_view_app_control_icon);
            TextView textView = findViewById(R.id.text_view_app_control_item);

            AppListModel model = intent.getParcelableExtra(CheckTimeAppControllerService.CONTROL_APP_MODEL);

            Drawable appIcon = model.getIcon(getPackageManager());
            String appName = model.getName();


            imageView.setImageDrawable(appIcon);
            textView.setText(appName);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }
}
