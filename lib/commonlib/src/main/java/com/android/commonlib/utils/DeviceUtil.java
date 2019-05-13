package com.android.commonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class DeviceUtil {
    private static final String TAG = "DeviceUtil";
    public static final String CPU_TYPE_DEFAULT = "0";
    public static final String CPU_TYPE_ARM_V5 = "1";
    public static final String CPU_TYPE_ARM_V6 = "2";
    public static final String CPU_TYPE_ARM_V7 = "3";


    /**
     * 获取android id
     */
    public static String getAndroidId(Context c) {
        return Settings.System.getString(c.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取操作系统类型
     *
     * @return
     */
    public static String getOsType() {
        return "android"; // 1为android
    }

    /**
     * //系统类型
     *
     * @return 1 Android 2 ios 3 h5 4 web 5 pc
     */
    public static String getOsTypeInt() {
        return "1";
    }

    public static byte getTerminal() {
        return 1;
    }

    /**
     * 获取应用编号
     *
     * @return
     */
    public static String getAppId() {
        return "100066";
    }

    public static String getAppIdForLoginAndRegisterAndUpdate() {
        return getAppId();
    }

    /**
     * 判断是否安装apk
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查安装app
     *
     * @param context
     * @param packages
     * @return
     */
    public static boolean checkApkExist(Context context, String[] packages) {
        for (int i = 0; i < packages.length; i++) {
            String pname = packages[i];
            if (checkApkExist(context, pname)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取渠道编号
     *
     * @return
     */
    public static String getChannelId(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Object object = (Object) applicationInfo.metaData.get("com.yunva.channelid");
            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取CPU类型
     *
     * @return String CPU类型：{@link #CPU_TYPE_DEFAULT}, {@link #CPU_TYPE_ARM_V5},
     * {@link #CPU_TYPE_ARM_V6}, {@link #CPU_TYPE_ARM_V7},
     */
    public static String getCpuType() {
        String cpuName = getCpuName();
        if (null == cpuName) {
            return CPU_TYPE_DEFAULT;
        } else if (cpuName.contains("ARMv7")) {
            return CPU_TYPE_ARM_V7;
        } else if (cpuName.contains("ARMv6")) {
            return CPU_TYPE_ARM_V6;
        } else if (cpuName.contains("ARMv5")) {
            return CPU_TYPE_ARM_V5;
        }

        return CPU_TYPE_DEFAULT;
    }

    /**
     * 获取cpu名称
     *
     * @return
     */
    public static String getCpuName() {
        FileReader fr = null;
        BufferedReader br = null;
        String[] array = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            array = text.split(":\\s+", 2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (array != null && array.length >= 2) {
            return array[1];
        }
        return null;
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return
     */
    public static String getDisplay(Context context) {
        return null;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getTelephonyModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取手机厂商名称
     *
     * @return
     */
    public static String getManufacturer() {
        try {
            return Build.MANUFACTURER;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取系统版本编号
     *
     * @return
     */
    public static int getSystemVersionCode() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取系统版本名称
     *
     * @return
     */
    public static String getSystemVersionName() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取UUID号
     *
     * @param context
     * @return
     */
    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID uuid;

    public static String getUuid(Context context) {
        if (uuid == null) {
            synchronized (DeviceUtil.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                    }
                }
            }
        }
        return uuid.toString();
    }


    /**
     * 获取手机mac地址
     *
     * @param context
     * @return
     */
    private String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        // 在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        String macAddress = "";
        String str = "";
        try {
            Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader reader = new InputStreamReader(process.getInputStream());
            LineNumberReader numberReader = new LineNumberReader(reader);
            for (; null != str; ) {
                str = numberReader.readLine();
                if (str != null) {
                    macAddress = str.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("".equals(macAddress)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return macAddress;
    }

    public static String loadFileAsString(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            String text = loadReaderAsString(reader);
            reader.close();
            return text;
        } catch (IOException e) {
//            e.printStackTrace();
            return getMacAddress();
        }
    }

    public static String loadReaderAsString(Reader reader) {
        try {
            StringBuffer buf = new StringBuffer();
            char[] buffer = new char[4096];
            int readLength = reader.read(buffer);
            while (readLength >= 0) {
                buf.append(buffer, 0, readLength);
                readLength = reader.read(buffer);
            }
            return buf.toString();
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 方法一
     * 获取设备Mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        String macAddress = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                byte[] bytes = networkInterface.getHardwareAddress();
                if (bytes == null || bytes.length == 0) {
                    continue;
                }
                StringBuffer buffer = new StringBuffer();
                for (byte b : bytes) {
                    buffer.append(String.format("%02X:", b));
                }
                if (buffer.length() > 0) {
                    buffer.deleteCharAt(buffer.length() - 1);
                }
                macAddress = buffer.toString();
                L.d(TAG, "interface name=" + networkInterface.getName() + ", mac address=" + macAddress);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        L.e(TAG, "getMacAddress: " + macAddress);
        return macAddress;
    }

    /**
     * 获取IMSI号
     *
     * @param context
     * @return
     */
    public static String getImsi(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取IMEI号
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getAppName(Context context) {
        try {
            if (context != null) {
                PackageManager pm = context.getPackageManager();//
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                return (String) pi.applicationInfo.loadLabel(pm);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取IP地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return "0.0.0.0";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
