package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
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
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter.AppListRecyclerViewAdapter;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.controller.CheckTimeAppControllerService;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect.AllDayTimeCheckFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect.NowToTimeCheckFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect.SelectTimeCheckFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

public class CheckTimeActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private AllDayTimeCheckFragment mAllDayTimeCheckFragment;
    private NowToTimeCheckFragment mNowToTimeCheckFragment;
    private SelectTimeCheckFragment mSelectTimeCheckFragment;
    public final static String OVER_DAY_TIME = "overDayTimeValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_time);

        Intent intent = getIntent();

        ImageView imageViewIcon = findViewById(R.id.image_view_check_time_icon);
        TextView textViewAppName = findViewById(R.id.text_view_check_item);


        AppListModel model = intent.getParcelableExtra(CheckTimeAppControllerService.CONTROL_APP_MODEL);

        Drawable icon = model.getIcon(getPackageManager());
        String packageName = model.getName();

        imageViewIcon.setImageDrawable(icon);
        textViewAppName.setText(packageName);

        mSpinner = findViewById(R.id.select_time_spinner);

        mAllDayTimeCheckFragment = new AllDayTimeCheckFragment();
        mNowToTimeCheckFragment = new NowToTimeCheckFragment();
        mSelectTimeCheckFragment = new SelectTimeCheckFragment();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.check_time_fragment, mAllDayTimeCheckFragment).commit();
                } else if (position == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.check_time_fragment, mNowToTimeCheckFragment).commit();
                } else if (position == 2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.check_time_fragment, mSelectTimeCheckFragment).commit();
                }

                getSupportFragmentManager().beginTransaction().commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void save_button(View view) {
        int position = mSpinner.getSelectedItemPosition();

        Intent intent = new Intent();

        if (position == 0) {
            EditText overDayTimeTextView = mAllDayTimeCheckFragment.getTimeTextView();

            if (overDayTimeTextView.getText().toString().length() == 0) {
                Toast.makeText(this, "제어 할 시간을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 1);
            intent.putExtra(OVER_DAY_TIME, overDayTimeTextView.getText().toString());

            setResult(RESULT_OK, intent);
            finish();
        } else if (position == 1) {
            Toast.makeText(this, "미지원", Toast.LENGTH_SHORT).show();
        } else if (position == 2) {
            Toast.makeText(this, "미지원", Toast.LENGTH_SHORT).show();
        }

    }
}
