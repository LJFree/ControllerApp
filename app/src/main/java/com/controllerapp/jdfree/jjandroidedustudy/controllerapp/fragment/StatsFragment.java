package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment;


import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    private TextView mNamH;
    private ImageView mImageView;
    private TextView mAppNameTextView;
    private TextView mNamM;
    private TextView mNamS;
    private TextView mSH;
    private TextView mSM;
    private TextView mSS;
    private TextView mAH;
    private TextView mAM;
    private TextView mAS;

    public StatsFragment() {
        // Required empty public constructor
    }

    public void setModel(AppListModel model, Drawable icon) {


        DecimalFormat format = new DecimalFormat("00");

        String appName = model.getName();
        Drawable iicon = icon;
        int allTime = model.getAllDayTime() * 60;
        int sTime = model.getStartDayTime();
        int nTime = allTime - sTime;

        mImageView.setImageDrawable(iicon);
        mAppNameTextView.setText(appName);

        int nH = nTime / 3600;
        int nM = (nTime - (nH * 3600)) / 60;
        int nS = (nTime - (nH * 3600) - (nM * 60));

        int sH = sTime / 3600;
        int sM = (sTime - (sH * 3600)) / 60;
        int sS = (sTime - (sH * 3600) - (sM * 60));

        int aH = allTime / 3600;
        int aM = (allTime - (aH * 3600)) / 60;
        int aS = (allTime - (aH * 3600) - (aM * 60));


        mNamH.setText(format.format(nH));
        mNamM.setText(format.format(nM));
        mNamS.setText(format.format(nS));

        mSH.setText(format.format(sH));
        mSM.setText(format.format(sM));
        mSS.setText(format.format(sS));

        mAH.setText(format.format(aH));
        mAM.setText(format.format(aM));
        mAS.setText(format.format(aS));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mImageView = view.findViewById(R.id.image_view_fragment_stats_icon);
        mAppNameTextView = view.findViewById(R.id.text_view_fragment_stats_item);

        mNamH = view.findViewById(R.id.text_view_nam_h);
        mNamM = view.findViewById(R.id.text_view_nam_m);
        mNamS = view.findViewById(R.id.text_view_nam_s);

        mSH = view.findViewById(R.id.text_view_s_h);
        mSM = view.findViewById(R.id.text_view_s_m);
        mSS = view.findViewById(R.id.text_view_s_s);

        mAH = view.findViewById(R.id.text_view_a_h);
        mAM = view.findViewById(R.id.text_view_a_m);
        mAS = view.findViewById(R.id.text_view_a_s);

        return view;
    }
}
