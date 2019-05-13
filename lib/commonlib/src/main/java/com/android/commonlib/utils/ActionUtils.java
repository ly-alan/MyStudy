package com.android.commonlib.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 *
 */

public class ActionUtils {

    private static long lastClickTime;

    /**
     * 频繁点击按钮
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(500);
    }

    /**
     * 频繁点击按钮
     *
     * @return
     */
    public static boolean isFastDoubleClick(long interval) {
        if (Math.abs(System.currentTimeMillis() - lastClickTime) < interval) {
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        return false;
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
// 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (TextUtils.isEmpty(cmb.getText())) {
            return "";
        }
        return cmb.getText().toString().trim();
    }

    /**
     * 安装apk
     *
     * @param activity
     * @param filePath apk路径
     */
    public void installedApk(Activity activity, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        if (activity != null) {
            System.out.print("install apk context is null");
            activity.startActivity(intent);
        }
    }
}
