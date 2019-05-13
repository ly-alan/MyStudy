package com.android.commonlib.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


import com.android.commonlib.base.BaseApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络监听和判断
 */

public class NetUtils {
    // 上网类型
    /**
     * 没有网络
     */
    public static final byte NETWORK_TYPE_NONE = 0;
    /**
     * wifi网络
     */
    public static final byte NETWORK_TYPE_WIFI =1;
    /**
     * 从原快速网络剥离 4G网络
     */
    public static final byte NETWORK_TYPE_4G = 2;

    public static NetworkType type = null;
    public static List<NetworkListener> listener = new ArrayList<NetworkListener>();

    /**
     * 网络变化监听
     */
    public interface NetworkListener {
        /**
         * @param oldType 变化前
         * @param newType 变化后
         */
        void onNetworkChanged(NetworkType oldType, NetworkType newType);
    }

    public enum NetworkType {
        WIFI_FAST, MOBILE, NONE,
    }

    private NetUtils() {

    }

    public final synchronized NetworkType getNetworkType() {
        return type;
    }

    /**
     * 设置监听
     *
     * @param l
     */
    public static void setListener(NetworkListener l) {
        listener.add(l);
    }

    public static void clearListener(NetworkListener l) {
        listener.remove(l);
    }

    /**
     * 注册网络变化监听广播
     */
    public static void registerConnectivityBroadcastReceiver() {
        type = checkType(getNetworkInfo());
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateNetwork();
            }
        };
        BaseApplication.getInstance().registerReceiver(receiver, filter);
    }

    /**
     * Is device connected to network (WiFi or mobile).<br>
     * <br>
     * <b>Hint</b>: A connection to WiFi does not guarantee Internet access.
     *
     * @return {@code true} if device is connected to mobile network or WiFi
     */
    public static boolean isConnected() {
        final ConnectivityManager conManager = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * Is device connected to WiFi?<br>
     * <br>
     * <b>Hint</b>: A connection to WiFi does not guarantee Internet access.
     *
     * @return {@code true} if device is connected to an access point
     */
    public static boolean isWiFiConnected() {
        ConnectivityManager mConnectivity = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        //判断网络连接类型，只有在3G或wifi里进行一些数据更新。
        int netType = -1;
        if (info != null) {
            netType = info.getType();
        }
        if (netType == ConnectivityManager.TYPE_WIFI) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * Is device connected to mobile network?
     *
     * @return {@code true} if device is connected to mobile network
     */
    public static boolean isMobileNetworkConnected() {
        ConnectivityManager mConnectivity = (ConnectivityManager) BaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        //判断网络连接类型，只有在3G或wifi里进行一些数据更新。
        int netType = -1;
        if (info != null) {
            netType = info.getType();
        }
        if (netType == ConnectivityManager.TYPE_MOBILE) {
            return info.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    private static final String IP_DEFAULT = "0.0.0.0";

    /**
     * ip地址
     *
     * @return
     */
    public static String getIPAddress() {
        return DeviceUtil.getIPAddress(BaseApplication.getInstance());
    }

    /**
     * 调用网络变化监听发生变化
     */
    private static void updateNetwork() {
        NetworkInfo networkInfo = getNetworkInfo();
        NetworkType t = type;
        type = checkType(networkInfo);
        if (type != t && listener != null) {
            for (NetworkListener nl : listener) {
                nl.onNetworkChanged(t, type);
            }
        }
    }

    private static synchronized NetworkInfo getNetworkInfo() {
        Context context = BaseApplication.getInstance();
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo();
    }

    private static NetworkType checkType(NetworkInfo info) {
        if (info == null || !info.isConnected()) {
            return NetworkType.NONE;
        }
        int type = info.getType();
        if ((type == ConnectivityManager.TYPE_WIFI)
                || (type == ConnectivityManager.TYPE_ETHERNET)) {
            return NetworkType.WIFI_FAST;
        }

        if (type == ConnectivityManager.TYPE_MOBILE) {
            return NetworkType.MOBILE;
        }
        return NetworkType.NONE;
    }

    /**
     * 获取网络状态 2 WIFI, 14G
     *
     * @return
     */
    public static int getNetWorkType() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI;
            } else if (nType == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_TYPE_4G;
            } else  {
                return NETWORK_TYPE_WIFI;
            }
        }
        return NETWORK_TYPE_WIFI;
    }
}
