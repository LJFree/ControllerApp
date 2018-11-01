package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {


    private List<AppListModel> mData;
    private onClicked clicked;
    private PackageManager mPm;

    public interface onClicked {
        void onDataClicked(AppListModel model, Drawable icon);

        void onAddClicked();

        void onLongClicked(int position);
    }


    public MainRecyclerViewAdapter(List<AppListModel> mData) {
        this.mData = mData;
    }

    public MainRecyclerViewAdapter(List<AppListModel> mData, PackageManager mPm) {
        this.mData = mData;
        this.mPm = mPm;
    }

    public void setPm(PackageManager mPm) {
        this.mPm = mPm;
    }

    public void onSetClicked(onClicked clicked) {
        this.clicked = clicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_icon_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (i != mData.size()) {

            final AppListModel data = mData.get(i);
            final int position = i;

            final Drawable icon = data.getIcon(mPm);

            String name = data.getName();
            int overTime = data.getOverDayTime();
            int allTime = data.getAllDayTime();

            viewHolder.icon.setImageDrawable(icon);

            viewHolder.name.setText(name);
            viewHolder.time.setText(overTime + "/" + allTime + "분");

            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onDataClicked(data, icon);
                }
            });
            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onDataClicked(data, icon);
                }
            });
            viewHolder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onDataClicked(data, icon);
                }
            });

            viewHolder.icon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clicked.onLongClicked(position);
                    return false;
                }
            });
            viewHolder.name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clicked.onLongClicked(position);
                    return false;
                }
            });
            viewHolder.time.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clicked.onLongClicked(position);
                    return false;
                }
            });
        } else {

            viewHolder.icon.clearColorFilter();
            viewHolder.name.clearComposingText();
            viewHolder.time.clearComposingText();

            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onAddClicked();
                }
            });

            viewHolder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onAddClicked();
                }
            });
            viewHolder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicked.onAddClicked();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mData.size());
    }

    public void addItem(int position, AppListModel model) {
        mData.add(position, model);
        notifyItemInserted(position);
        notifyItemRangeChanged(0, mData.size());
    }

    public void changeItem(List<AppListModel> list) {
        mData = list;
//        notifyDataSetChanged();
        notifyItemRangeChanged(0, mData.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView time;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.recycler_icon);
            name = itemView.findViewById(R.id.recycler_name);
            time = itemView.findViewById(R.id.recycler_time);
        }
    }
}
