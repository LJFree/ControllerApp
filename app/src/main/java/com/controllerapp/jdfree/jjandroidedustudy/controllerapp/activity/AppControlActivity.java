package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

public class AppControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_control);

        Intent intent = getIntent();

        ImageView imageView = findViewById(R.id.image_view_app_control_icon);
        TextView textView = findViewById(R.id.text_view_app_control_item);

        AppListModel model = intent.getParcelableExtra(CheckTimeAppControllerService.CONTROL_APP_MODEL);    // 데이터 불러오기

        Drawable appIcon = model.getIcon(getPackageManager());  // 아이콘 불러오기
        String appName = model.getName();   // 패키지 이름 불러오기

        imageView.setImageDrawable(appIcon);    // 아이콘 설정
        textView.setText(appName);  // 패키지 이름 설정

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();   // 화면이 안보일 시 종료
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();   // 뒤로가기 시 종료
    }
}
