package com.roger.workmanager;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;


public class UnlockHelper {
    /**
     * 解锁设备并点亮屏幕
     */
    public static void unlockDevice(Activity activity) {
        KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager != null && keyguardManager.isKeyguardLocked()) {
            keyguardManager.requestDismissKeyguard(activity, new KeyguardManager.KeyguardDismissCallback() {
                @Override
                public void onDismissSucceeded() {
                    super.onDismissSucceeded();
                    // 解锁成功
                    Log.d("UnlockHelper", "解锁成功");
                }

                @Override
                public void onDismissError() {
                    super.onDismissError();
                    // 解锁失败
                    Log.e("UnlockHelper", "解锁失败");
                }
            });
        }
    }
}
