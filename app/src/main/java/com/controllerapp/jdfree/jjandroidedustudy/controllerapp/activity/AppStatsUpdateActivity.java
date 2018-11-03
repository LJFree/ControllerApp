package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.DeleteDialogFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
import java.util.List;

public class AppStatsUpdateActivity extends AppCompatActivity implements DeleteDialogFragment.onDeleteListener {

    private int mPosition;
    private List<AppListModel> mListModel;

    private TimePicker mTimePicker;
    private AppListModel mAppModel;
    private int mATime;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_stats_update);

        ImageView mImageView = findViewById(R.id.image_view_update_time_icon);
        TextView mTextView = findViewById(R.id.text_view_update_item);
        mTimePicker = findViewById(R.id.update_time_picker);

        Intent intent = getIntent();

        mPosition = intent.getIntExtra(MainActivity.APP_LIST_POSITION, 0);  // 업데이트 또는 삭제 할 포지션 데이터 불러오기
        mListModel = intent.getParcelableArrayListExtra(MainActivity.APP_LIST_MODEL);   // 전체 리스트 데이터 불러오기

        mAppModel = mListModel.get(mPosition);  // 모델링 선택 데이터 선택

        mImageView.setImageDrawable(mAppModel.getIcon(getPackageManager()));
        mTextView.setText(mAppModel.getName());

        // 시간 설정
        mATime = mAppModel.getAllDayTime();
        int h = mATime / 60;
        int m = mATime - (h * 60);


        // 시간 설정 초기화
        mTimePicker.setIs24HourView(true);
        mTimePicker.setHour(h);
        mTimePicker.setMinute(m);
    }

    // 수정하기 버튼 이벤트
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateButton(View view) {
        int h = mTimePicker.getHour();
        int m = mTimePicker.getMinute();
        int updateTime = (h * 60) + m;

        if (updateTime == 0) {
            Toast.makeText(this, "수정할 시간이 0분 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mATime == updateTime) {
            Toast.makeText(this, "수정할 시간이 기존 시간과 달라야합니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 수정 값 데이터 변경
            if (mAppModel.getStartDayTime() < 0) {
                mAppModel.setOverDayTime(0);
                mAppModel.setStartDayTime(updateTime * 60);
            } else {
                mAppModel.setAllDayTime(updateTime);
            }
            mAppModel.setOverDayTime(+(updateTime - (mAppModel.getStartDayTime() / 60)));
        }

        mListModel.set(mPosition, mAppModel);

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(MainActivity.APP_LIST_MODEL, (ArrayList<? extends Parcelable>) mListModel);  // 데이터 저장

        setResult(RESULT_OK, intent);   // 데이터 보내기

        finish();

    }

    // 삭제 버튼 이벤트(Fragment 제어)
    public void deleteButton(View view) {
        new DeleteDialogFragment().setDeleteListener(this).show(getSupportFragmentManager(), "delete");
    }

    // 삭제 버튼 이벤트
    @Override
    public void deleteListener() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.APP_LIST_POSITION, mPosition);

        setResult(RESULT_OK, intent);   // 값 저장
        finish();
    }
}
