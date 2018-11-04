package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.AppListRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
import java.util.List;

public class AllAppListControlActivity extends AppCompatActivity implements AppListRecyclerViewAdapter.ClickedListener {

    private List<ResolveInfo> mAppInfoList;
    private AppListRecyclerViewAdapter mAdapter;
    private static final int REQUEST_CODE = 1001;
    private AppListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_app_list_control);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_app_list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        PackageManager pm = getPackageManager();
        Intent intent1 = new Intent(Intent.ACTION_MAIN, null);  // 실행 가능한 패키지 데이터 받기
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        mAppInfoList = pm.queryIntentActivities(intent1, 0);

        Intent intent = getIntent();
        List<AppListModel> nonDataList = intent.getParcelableArrayListExtra(MainActivity.NOT_APP_LIST); // 데이터 받기

        if (nonDataList == null) {
            nonDataList = new ArrayList<>();
        }

        // 제어
        nonDataList.add(new AppListModel("설정", "com.android.settings"));
        nonDataList.add(new AppListModel("연락처", "com.samsung.android.contacts"));
        nonDataList.add(new AppListModel("락엔락", getPackageName()));

        List<ResolveInfo> tempList = mAppInfoList;  // 실행 가능한 앱 리스트 저장

        for (AppListModel nonData : nonDataList) {  // 제어되고 있는 앱 추려내기
            for (int i = mAppInfoList.size() - 1; i >= 0; i--) {
                if (nonData.getPackageName().equals(mAppInfoList.get(i).activityInfo.packageName)) {
                    tempList.remove(i);
                }
            }
        }
        mAppInfoList = tempList;

        /*  정렬
        Collections.sort(mAppInfoList, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                String appName1 = String.valueOf(o1.loadLabel(getPackageManager()));
                String appName2 = String.valueOf(o2.loadLabel(getPackageManager()));

                return appName1.compareToIgnoreCase(appName2);
            }
        });
        */

        mAdapter = new AppListRecyclerViewAdapter(mAppInfoList, pm);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickedListener(this);

        EditText editText = findViewById(R.id.search_EditView);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                    onSearchButtonClicked();    // 검색

                    // 키보드 비활성화
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }

                return false;
            }
        });
    }


    // 리사이클러뷰 클릭 이벤트
    @Override
    public void onClicked(AppListModel model) {

        this.model = model;

        Intent intent = new Intent(this, CheckTimeActivity.class);
        intent.putExtra(CheckTimeAppControllerService.CONTROL_APP_MODEL, model);
        startActivityForResult(intent, REQUEST_CODE);
    }


    // 검색 버튼 클릭 이벤트
    private void onSearchButtonClicked() {
        TextView textView = findViewById(R.id.search_EditView);
        String textString = textView.getText().toString();

        if (textString.trim().length() == 0) {
            textView.setText("");
            mAdapter.setData(mAppInfoList);
        } else {

            List<ResolveInfo> searchList = new ArrayList<>();

            for (ResolveInfo resolveInfo : mAppInfoList) {  // 검색 필터링
                String appName = String.valueOf(resolveInfo.loadLabel(getPackageManager()));
                String name = resolveInfo.activityInfo.name;
                String packageName = resolveInfo.activityInfo.packageName;

                if (appName.toLowerCase().trim().contains(textString.toLowerCase().trim())
                        || name.toLowerCase().trim().contains(textString.toLowerCase().trim())
                        || packageName.toLowerCase().trim().contains(textString.toLowerCase().trim())) {

                    searchList.add(0, resolveInfo);
                }
            }

            if (searchList.size() == 0) {   // 내용이 없을 경우 초기화
                mAdapter.setData(mAppInfoList);
                textView.setText("");
                Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
            } else {    // 있을 경우 필터링 데이터만 나오게 하기
                mAdapter.setData(searchList);
                Toast.makeText(this, "검색 완료", Toast.LENGTH_SHORT).show();
            }

        }
        // 데이터 수정됐다고 알림
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            int time = data.getIntExtra(CheckTimeActivity.OVER_DAY_TIME, 0);

            Intent intent = new Intent();

            // 데이터 저장
            model.setAllDayTime(time);
            model.setOverDayTime(time);
            model.setStartDayTime(0);

            intent.putExtra(MainActivity.SELECT_ALL_APP_INFO, model);   // 데이터 보내기

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
