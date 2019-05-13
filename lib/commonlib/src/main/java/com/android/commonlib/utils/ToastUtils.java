package com.android.commonlib.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.commonlib.base.BaseApplication;

/**
 * Toast工具类
 *
 * @author yifan
 */
public class ToastUtils {

    /**
     * Toast
     */
    private static Toast mToast;

    private static void initToast(Context context) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
    }

    private static void setToastText(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 显示Toast
     *
     * @param resID 资源ID
     */
    public static void showToast(final int resID) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initToast(BaseApplication.getInstance());
                if (resID < 0 || mToast == null) {
                    return;
                }
                setToastText(ResUtils.getString(resID));
            }
        });

    }

    /**
     * 显示Toast
     *
     * @param str 文本
     */
    public static void showToast(final String str) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initToast(BaseApplication.getInstance());
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                setToastText(str);
            }
        });
    }
}
