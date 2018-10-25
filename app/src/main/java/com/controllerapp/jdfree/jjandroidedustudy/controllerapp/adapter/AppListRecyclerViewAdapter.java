package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter;

import android.content.pm.ApplicationInfo;
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

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.List;

public class AppListRecyclerViewAdapter extends RecyclerView.Adapter<AppListRecyclerViewAdapter.ViewHolder> {

    private List<ApplicationInfo> mData;
    private PackageManager mPm;

    private ClickedListener onClickedListener;

    public List<ApplicationInfo> getData() {
        return mData;
    }

    public void setData(List<ApplicationInfo> mData) {
        this.mData = mData;
    }

    public interface ClickedListener {
        void onClicked(AppListModel model);
    }

    public void setOnClickedListener(ClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    public AppListRecyclerViewAdapter(List<ApplicationInfo> mData, PackageManager pm) {
        this.mData = mData;
        this.mPm = pm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_app_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        ApplicationInfo appInfo = mData.get(i);

        Drawable icon = appInfo.loadIcon(mPm);
        String name = String.valueOf(appInfo.loadLabel(mPm));

        viewHolder.imageView.setImageDrawable(icon);
        viewHolder.textViewName.setText(name);

//        Log.d("appName , packName :   ", "onBindViewHolder: " + name + "          " + appInfo.packageName);

        if (onClickedListener != null) {
            final AppListModel model = new AppListModel(appInfo);

            viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickedListener.onClicked(model);
                }
            });

            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickedListener.onClicked(model);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, mData.size());
    }

    public void addItem(int position, ApplicationInfo model) {
        mData.add(position, model);
        notifyItemInserted(position);
//        notifyItemRangeChanged(0, mData.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.app_list_image_view);
            textViewName = itemView.findViewById(R.id.app_list_app_name);
        }
    }
}
