package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppListModel implements Parcelable {

    private String name;
    private String allDayTime;
    private String packageName;

    //////////////////////////////////////////////////////////////////////////////////////////
    public AppListModel() {
    }

    public ApplicationInfo getAppInfo(PackageManager pm) {
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return pi.applicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public ApplicationInfo getAppInfo(PackageManager pm, String packageName) {
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

    public String getName(PackageManager pm) {
        return String.valueOf(getAppInfo(pm).loadLabel(pm));
    }

    public String getPackName(PackageManager pm) {
        return getAppInfo(pm).packageName;
    }

    public AppListModel(String name, String packageName) {
        this.name = name;
        this.packageName = packageName;
    }
//////////////////////////////////////////////////////////////////////////////////////////

    public AppListModel(String name, String packageName, String allDayTime) {
        this.name = name;
        this.allDayTime = allDayTime;
        this.packageName = packageName;
    }


    protected AppListModel(Parcel in) {
        name = in.readString();
        allDayTime = in.readString();
        packageName = in.readString();
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

    public String getAllDayTime() {
        return allDayTime;
    }

    public void setAllDayTime(String allDayTime) {
        this.allDayTime = allDayTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppListModel model = (AppListModel) o;

        if (name != null ? !name.equals(model.name) : model.name != null) return false;
        if (allDayTime != null ? !allDayTime.equals(model.allDayTime) : model.allDayTime != null)
            return false;
        return packageName != null ? packageName.equals(model.packageName) : model.packageName == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (allDayTime != null ? allDayTime.hashCode() : 0);
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(",").append(packageName);
        sb.append(",").append(allDayTime);
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
        dest.writeString(allDayTime);
        dest.writeString(packageName);
    }
}
