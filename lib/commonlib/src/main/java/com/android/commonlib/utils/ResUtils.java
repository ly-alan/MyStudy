package com.android.commonlib.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.android.commonlib.base.BaseApplication;

/**
 * ResUtils 获取资源工具类
 * Created by yong.liao on 2018/3/29 0029.
 */
public class ResUtils {

    /**
     * 获取定义的尺寸
     *
     * @param id
     * @return
     */
    public static float getDimension(@DimenRes int id) {
        return BaseApplication.getInstance().getResources().getDimension(id);
    }

    /**
     * 获取定义的尺寸像素值
     *
     * @param id
     * @return
     */
    public static int getDimensionPixelSize(@DimenRes int id) {
        return BaseApplication.getInstance().getResources().getDimensionPixelSize(id);
    }

    /**
     * 获取定义的颜色值
     *
     * @param id
     * @return
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(BaseApplication.getInstance(), id);
    }

    /**
     * 获取定义的颜色值
     *
     * @param id
     * @return
     */
    public static ColorStateList getColorStateList(@ColorRes int id) {
        return BaseApplication.getInstance().getResources().getColorStateList(id);
    }

    /**
     * 获取定义的可绘制对象
     *
     * @param id
     * @return
     */
    public static Drawable getDrawable(@DrawableRes int id) {
        return BaseApplication.getInstance().getResources().getDrawable(id);
    }


    /**
     * 获取定义的字符串
     *
     * @param id
     * @return
     */
    public static String getString(@StringRes int id) {
        return BaseApplication.getInstance().getResources().getString(id);
    }

    /**
     * 获取定义的字符串
     *
     * @param id
     * @return
     */
    public static String getString(@StringRes int id, Object... formatArgs) {
        return BaseApplication.getInstance().getResources().getString(id, formatArgs);
    }

    /**
     * 获取定义的字符串数组
     *
     * @param id
     * @return
     */
    public static String[] getStringArray(@ArrayRes int id) {
        return BaseApplication.getInstance().getResources().getStringArray(id);
    }

    /**
     * 获取自定义的int数组
     *
     * @param id
     * @return
     */
    public static int[] getIntArray(@ArrayRes int id) {
        return BaseApplication.getInstance().getResources().getIntArray(id);
    }

    /**
     * 获取屏幕相关参数
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        return BaseApplication.getInstance().getResources().getDisplayMetrics();
    }
}
