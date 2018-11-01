package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.AppListRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

public class CheckTimeActivity extends AppCompatActivity {

    public final static String OVER_DAY_TIME = "overDayTimeValue";
    private TimePicker mTimePicker;

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

        Intent intent = new Intent();

        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        int allTime = (hour * 60) + minute;


        if (allTime == 0) {
            Toast.makeText(this, "하루 동안 사용 할 시간을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 1);
        intent.putExtra(OVER_DAY_TIME, allTime);

        setResult(RESULT_OK, intent);
        finish();

    }
}
