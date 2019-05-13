package com.android.commonlib.utils;

import android.util.Log;

/**
 * log 信息显示
 * Created by yong.liao on 2018/3/29 0029.
 */

public class L {

    public static void v(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String message) {
        if (Constants.IS_DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (Constants.IS_DEBUG) {
            Log.e(tag, msg);
        }
    }
}
