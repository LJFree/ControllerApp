package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppListModel implements Parcelable {

    private Drawable icon;
    private String allDayTime;
    private ApplicationInfo appInfo;


    /////////////////////////////////////////////////////////////////////////////////
    private PackageManager mPackageManager;

    public void setmPackageManager(PackageManager mPackageManager) {
        this.mPackageManager = mPackageManager;
    }

    public String getPackageName() {
        return appInfo.packageName;
    }

    public String getAppName(PackageManager packageManager) {
        return String.valueOf(appInfo.loadLabel(packageManager));
    }

    public void setIcon(PackageManager packageManager) {
        this.icon = appInfo.loadIcon(packageManager);
    }
    /////////////////////////////////////////////////////////////////////////////////

    public AppListModel(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public AppListModel(Drawable icon, ApplicationInfo appInfo) {
        this.icon = icon;
        this.appInfo = appInfo;
    }

    public AppListModel(Drawable icon, String allDayTime, ApplicationInfo appInfo) {
        this.icon = icon;
        this.allDayTime = allDayTime;
        this.appInfo = appInfo;
    }


    protected AppListModel(Parcel in) {
        allDayTime = in.readString();
        appInfo = in.readParcelable(ApplicationInfo.class.getClassLoader());
    }

    public static final Creator<AppListModel> CREATOR = new Creator<AppListModel>() {
        @Override
        public AppListModel createFromParcel(Parcel in) {
            return new AppListModel(in);
        }

        @Override
        public AppListModel[] newArray(int size) {
            return new AppListModel[size];
        }
    };

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAllDayTime() {
        return allDayTime;
    }

    public void setAllDayTime(String allDayTime) {
        this.allDayTime = allDayTime;
    }

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppListModel that = (AppListModel) o;

        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if (allDayTime != null ? !allDayTime.equals(that.allDayTime) : that.allDayTime != null)
            return false;
        return appInfo != null ? appInfo.equals(that.appInfo) : that.appInfo == null;
    }

    @Override
    public int hashCode() {
        int result = icon != null ? icon.hashCode() : 0;
        result = 31 * result + (allDayTime != null ? allDayTime.hashCode() : 0);
        result = 31 * result + (appInfo != null ? appInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AppListModel{");
        sb.append("icon=").append(icon);
        sb.append(", allDayTime='").append(allDayTime).append('\'');
        sb.append(", appInfo=").append(appInfo);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(allDayTime);
        dest.writeParcelable(appInfo, flags);
    }
}
