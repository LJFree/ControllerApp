package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.adapter;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.R;
import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model.AppListModel;

import java.util.List;

public class AppListRecyclerViewAdapter extends RecyclerView.Adapter<AppListRecyclerViewAdapter.ViewHolder> {

    private List<ResolveInfo> mData;
    private PackageManager mPm;

    private ClickedListener onClickedListener;

    public List<ResolveInfo> getData() {
        return mData;
    }

    public void setData(List<ResolveInfo> mData) {
        this.mData = mData;
    }

    public interface ClickedListener {
        void onClicked(AppListModel model);
    }

    public void setOnClickedListener(ClickedListener onClickedListener) {
        this.onClickedListener = onClickedListener;
    }

    public AppListRecyclerViewAdapter(List<ResolveInfo> data, PackageManager pm) {
        this.mData = data;
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

        ResolveInfo appInfo = mData.get(i);

        Drawable icon = appInfo.loadIcon(mPm);
        String name = String.valueOf(appInfo.loadLabel(mPm));

        String packageName = appInfo.activityInfo.packageName;

        viewHolder.imageView.setImageDrawable(icon);
        viewHolder.textViewName.setText(name);
        viewHolder.textDescription.setText(packageName);

        if (onClickedListener != null) {
            final AppListModel model = new AppListModel(name, packageName);

            viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickedListener.onClicked(model);
                }
            });

            viewHolder.textDescription.setOnClickListener(new View.OnClickListener() {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewName;
        private TextView textDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.app_list_image_view);
            textViewName = itemView.findViewById(R.id.app_list_app_name);
            textDescription = itemView.findViewById(R.id.app_list_description);
        }
    }
}
