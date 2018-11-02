package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

public class CheckTimeActivity extends AppCompatActivity {

    public final static String OVER_DAY_TIME = "overDayTimeValue";
    public static final int GRANT_REQUEST_CODE = 1003;
    private TimePicker mTimePicker;
    private int mAllTime;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_time);

        Intent intent = getIntent();

        ImageView imageViewIcon = findViewById(R.id.image_view_check_time_icon);
        TextView textViewAppName = findViewById(R.id.text_view_check_item);

        mTimePicker = findViewById(R.id.time_picker);

        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(0);
        mTimePicker.setMinute(0);

        AppListModel model = intent.getParcelableExtra(CheckTimeAppControllerService.CONTROL_APP_MODEL);

        Drawable icon = model.getIcon(getPackageManager());
        String packageName = model.getName();

        imageViewIcon.setImageDrawable(icon);
        textViewAppName.setText(packageName);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void save_button(View view) {

        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        mAllTime = (hour * 60) + minute;


        if (mAllTime == 0) {
            Toast.makeText(this, "하루 동안 사용 할 시간을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isGrant()) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("권한 설정");
            builder.setMessage("앱 제어기 앱을 사용하기 위해서는 '사용자 접근 권한 설정을 해야 합니다.\n\n접근 권한 설정을 하시겠습니까?");
            builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), GRANT_REQUEST_CODE);
                }
            });
            builder.setNegativeButton("거부", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(CheckTimeActivity.this, "접근 권한 거부", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();

        } else {
            startIntent();
        }
    }

    private void startIntent() {
        Intent intent = new Intent();

        intent.putExtra(OVER_DAY_TIME, mAllTime);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!isGrant()) {
            Toast.makeText(CheckTimeActivity.this, "접근 권한 거부", Toast.LENGTH_SHORT).show();
        } else {
            startIntent();
        }
    }

    private boolean isGrant() {
        boolean granted;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);

            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());

            if (mode == AppOpsManager.MODE_DEFAULT) {
                granted = checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED;
            } else {
                granted = (mode == AppOpsManager.MODE_ALLOWED);
            }

            return granted;
        } else {
            return false;
        }
    }
}
