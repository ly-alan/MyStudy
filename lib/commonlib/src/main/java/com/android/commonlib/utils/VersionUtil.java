package com.android.commonlib.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.commonlib.base.BaseApplication;

/**
 * VersionUtil apk版本相关信息
 */
public class VersionUtil {

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageInfo info = getPackageInfo();
        if (info != null) {
            return info.versionCode;
        }
        return 0;
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName() {
        PackageInfo info = getPackageInfo();
        if (info != null) {
            return info.versionName;
        }
        return "";
    }

    /**
     *
     * @return
     */
    private static PackageInfo getPackageInfo() {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        if (packageManager == null) {
            return null;
        }
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

}
