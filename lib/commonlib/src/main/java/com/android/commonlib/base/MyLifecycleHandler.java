package com.android.commonlib.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * 状态监听
 * Created by yong.liao on 2018/5/23 0023.
 */
public class MyLifecycleHandler implements Application.ActivityLifecycleCallbacks {
    private static int resumed;
    private static int paused;
    private static int started;
    private static int stopped;
    //是否退到后台
    private static boolean isBackground = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
        if (isApplicationVisible() && isBackground && isApplicationInForeground()) {
            //从后台转到前台
            isBackground = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
        if (isApplicationVisible()) {
            isBackground = false;
        } else {
            isBackground = true;
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public static boolean isApplicationVisible() {
        return started > stopped;
    }

    public static boolean isApplicationInForeground() {
        // 当所有 Activity 的状态中处于 resumed 的大于 paused 状态的，即可认为有Activity处于前台状态中
        return resumed > paused;
    }

}
