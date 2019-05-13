package com.android.commonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.commonlib.base.BaseApplication;
import com.google.gson.Gson;


/**
 * 对Sharedpreferences功能的封装
 */
public class SPUtils {


    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        //添加保存数据
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        //添加保存数据
        sp.edit().putString(key, value).apply();

    }

    public static String getString(String key, String defValue) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);

    }

    public static void putInt(String key, int value) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        //添加保存数据
        sp.edit().putInt(key, value).apply();

    }

    public static int getInt(String key, int defValue) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);

    }

    public static void putLong(String key, long value) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        //添加保存数据
        sp.edit().putLong(key, value).apply();

    }

    public static long getLong(String key, Long defValue) {
        SharedPreferences sp = BaseApplication.getInstance().getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    /**
     * 添加对象
     *
     * @param key
     * @param t
     */
    public static <T> void putModel(String key, T t) {
        if (!TextUtils.isEmpty(key)) {
            Gson gson = new Gson();
            if (t != null) {
                putString(key, gson.toJson(t));
            } else {
                putString(key, "");
            }
        }
    }

    /**
     * 获取对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public static <T> T getModel(String key, Class<T> clazz) {
        String value = null;
        if (!TextUtils.isEmpty(key)) {
            value = getString(key, "");
        }
        Gson gson = new Gson();
        return TextUtils.isEmpty(value) ? null : gson.fromJson(value, clazz);// fastjson
    }

}
