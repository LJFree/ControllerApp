package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppListModel implements Parcelable {

    private String name;
    private String packageName;
    private int allDayTime;
    private int overDayTime;
    private int startDayTime;

    public ApplicationInfo getAppInfo(PackageManager pm) {
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return pi.applicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public Drawable getIcon(PackageManager pm) {
        return getAppInfo(pm).loadIcon(pm);
    }

    public AppListModel(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
    }

    public AppListModel(String name, String packageName, int allDayTime, int overDayTime) {
        this.name = name;
        this.packageName = packageName;
        this.allDayTime = allDayTime;
        this.overDayTime = overDayTime;
    }

    protected AppListModel(Parcel in) {
        name = in.readString();
        packageName = in.readString();
        allDayTime = in.readInt();
        overDayTime = in.readInt();
        startDayTime = in.readInt();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getAllDayTime() {
        return allDayTime;
    }

    public void setAllDayTime(int allDayTime) {
        this.allDayTime = allDayTime;
    }

    public int getOverDayTime() {
        return overDayTime;
    }

    public void setOverDayTime(int overDayTime) {
        this.overDayTime = overDayTime;
    }

    public int getStartDayTime() {
        return startDayTime;
    }

    public void setStartDayTime(int startDayTime) {
        this.startDayTime = startDayTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppListModel model = (AppListModel) o;

        if (allDayTime != model.allDayTime) return false;
        if (overDayTime != model.overDayTime) return false;
        if (startDayTime != model.startDayTime) return false;
        if (name != null ? !name.equals(model.name) : model.name != null) return false;
        return packageName != null ? packageName.equals(model.packageName) : model.packageName == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + allDayTime;
        result = 31 * result + overDayTime;
        result = 31 * result + startDayTime;
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(name).append(',');
        sb.append(packageName).append(',');
        sb.append(allDayTime).append(',');
        sb.append(overDayTime).append(',');
        sb.append(startDayTime);
        sb.append(';');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeInt(allDayTime);
        dest.writeInt(overDayTime);
        dest.writeInt(startDayTime);
    }
}
