package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllAppListControlActivity extends AppCompatActivity implements AppListRecyclerViewAdapter.ClickedListener {

    //    private List<ApplicationInfo> mAppInfoList;
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
        Intent intent1 = new Intent(Intent.ACTION_MAIN, null);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);


        mAppInfoList = pm.queryIntentActivities(intent1, 0);

        Intent intent = getIntent();

        List<AppListModel> nonDataList = intent.getParcelableArrayListExtra(MainActivity.NOT_APP_LIST);

        // 제어
        nonDataList.add(new AppListModel("설정", "com.android.settings", ""));

        List<ResolveInfo> tempList = mAppInfoList;

        for (AppListModel nonData : nonDataList) {
            for (int i = mAppInfoList.size() - 1; i >= 0; i--) {
                if (nonData.getPackageName().equals(mAppInfoList.get(i).activityInfo.packageName)) {
//                    if (nonData.getPackageName().equals(mAppInfoList.get(i).packageName)) {
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

                    onSearchButtonClicked();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }

                return false;
            }
        });
    }

    @Override
    public void onClicked(AppListModel model) {

        this.model = model;

        Intent intent = new Intent(this, CheckTimeActivity.class);

        startActivityForResult(intent, REQUEST_CODE);
    }

    private void onSearchButtonClicked() {
        TextView textView = findViewById(R.id.search_EditView);
        String textString = textView.getText().toString();

        if (textString.trim().length() == 0) {
            textView.setText("");
            mAdapter.setData(mAppInfoList);
        } else {

            List<ResolveInfo> searchList = new ArrayList<>();

            for (ResolveInfo resolveInfo : mAppInfoList) {
                String appName = String.valueOf(resolveInfo.loadLabel(getPackageManager()));
                String name = resolveInfo.activityInfo.name;
                String packageName = resolveInfo.activityInfo.packageName;

                if (appName.toLowerCase().trim().contains(textString.toLowerCase().trim())
                        || name.toLowerCase().trim().contains(textString.toLowerCase().trim())
                        || packageName.toLowerCase().trim().contains(textString.toLowerCase().trim())) {

                    searchList.add(0, resolveInfo);
                }
            }

            if (searchList.size() == 0) {
                mAdapter.setData(mAppInfoList);
                textView.setText("");
                Toast.makeText(this, "검색 실패", Toast.LENGTH_SHORT).show();
            } else {
                mAdapter.setData(searchList);
                Toast.makeText(this, "검색 완료", Toast.LENGTH_SHORT).show();
            }

        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            String time = data.getStringExtra(CheckTimeActivity.ALL_DAY_TIME);

            Intent intent = new Intent();

            model.setAllDayTime(time);

            intent.putExtra(MainActivity.SELECT_ALL_APP_INFO, model);

            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
