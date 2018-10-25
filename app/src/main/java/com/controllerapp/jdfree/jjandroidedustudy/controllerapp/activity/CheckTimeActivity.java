package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect.AllDayTimeCheckFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect.NowToTimeCheckFragment;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect.SelectTimeCheckFragment;

public class CheckTimeActivity extends AppCompatActivity {

    private Spinner mSpinner;
    private AllDayTimeCheckFragment mAllDayTimeCheckFragment;
    private NowToTimeCheckFragment mNowToTimeCheckFragment;
    private SelectTimeCheckFragment mSelectTimeCheckFragment;
    public final static String ALL_DAY_TIME = "allDayTimeValue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_time);

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
            EditText allDayTimeTextView = mAllDayTimeCheckFragment.getTimeTextView();

            if (allDayTimeTextView.getText().toString().length() == 0) {
                Toast.makeText(this, "제어 할 시간을 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra(ALL_DAY_TIME, allDayTimeTextView.getText().toString());

        } else if (position == 1) {
        } else if (position == 2) {
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
