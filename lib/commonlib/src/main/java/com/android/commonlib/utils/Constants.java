package com.android.commonlib.utils;

import android.os.Environment;

import java.io.File;

/**
 * 常量保存
 * Created by yong.liao on 2018/3/29 0029.
 */

public class Constants {
    //debug状态会输出log信息
    public static final boolean IS_DEBUG = true;


    //保存应用信息的文件夹
    public static final String FOLDER = "mySpeed";
    // 安装文件下载路径（升级用）
    public static final String APK_CACHE_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + FOLDER + File.separator + "apk" + File.separator;
    // 日志文件保存路径
    public static final String LOG_CACHE_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + FOLDER + File.separator + "log" + File.separator;
    //数据缓存文件保存路径
    public static final String DATA_CACHE_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + FOLDER + File.separator + "data" + File.separator;

    //SharePreference文件
    public static final String SP_NAME = "my_preference";
}
