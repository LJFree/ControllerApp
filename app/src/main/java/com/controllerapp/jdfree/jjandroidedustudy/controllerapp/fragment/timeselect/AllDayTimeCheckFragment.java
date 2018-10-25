package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.timeselect;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllDayTimeCheckFragment extends Fragment {

    private EditText timeTextView;

    public EditText getTimeTextView() {
        return timeTextView;
    }

    public void setTimeTextView(EditText timeTextView) {
        this.timeTextView = timeTextView;
    }

    public AllDayTimeCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_day_time_check, container, false);

        timeTextView = view.findViewById(R.id.all_time_edit_view);

        return view;
    }

}
