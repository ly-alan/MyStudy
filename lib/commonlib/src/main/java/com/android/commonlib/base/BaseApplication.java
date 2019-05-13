package com.android.commonlib.base;

import android.app.Application;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yong.liao on 2018/5/23 0023.
 */

public class BaseApplication extends Application {
    /**
     * 单例模式
     */
    private static BaseApplication mInstance;
    /**
     * 当前显示的activity栈
     */
    private Map<String, BaseActivity> activityMap;

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
    }

    /**
     * @return
     */
    public BaseActivity getActivityContext() {
        if (null != activityMap) {
            for (String key : activityMap.keySet()) {
                return getActivityByTag(key);
            }
        }
        return null;
    }

    /**
     * 除tag之外的activity
     *
     * @param tag
     * @return
     */
    public BaseActivity getActivityContextNoTag(String tag) {
        if (null != activityMap) {
            for (String key : activityMap.keySet()) {
                if (!TextUtils.isEmpty(tag) && !tag.equals(key)) {
                    return getActivityByTag(key);
                }
            }
        }
        return null;
    }


    public void addActivity(String tag, BaseActivity activity) {
        if (null == activityMap) {
            activityMap = new HashMap<>();
        }
        activityMap.put(tag, activity);
    }

    public void removeActivity(String tag) {
        if (null != activityMap && activityMap.containsKey(tag)) {
            activityMap.remove(tag);
        }
    }

    public BaseActivity getActivityByTag(String tag) {
        if (null != activityMap && activityMap.containsKey(tag)) {
            return activityMap.get(tag);
        }
        return null;
    }

    public void exit() {
        if (null != activityMap) {
            for (String key : activityMap.keySet()) {
                activityMap.get(key).finish();
            }
            activityMap = null;
            System.exit(0);
        }
    }
}
