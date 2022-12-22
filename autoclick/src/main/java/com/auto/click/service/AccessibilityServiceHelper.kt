package com.auto.click.service

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import com.auto.click.callback.ICallBack

/**
 * @Author Roger
 * @Date 2022/9/9 9:42
 * @Description
 */
object AccessibilityServiceHelper {


    var mCallback: ICallBack? = null;

    fun setCallBack(call: ICallBack) {
        mCallback = call
    }


    fun jumpToAccessibilitySetting(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    fun checkAccessibilityEnabled(context: Context, serviceName: String): Boolean {
        val mAccessibilityManager: AccessibilityManager = context.applicationContext
            .getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val accessibilityServices: List<AccessibilityServiceInfo> =
            mAccessibilityManager.getEnabledAccessibilityServiceList(
                AccessibilityServiceInfo.FEEDBACK_GENERIC
            )
        for (info in accessibilityServices) {
            if (info.id == serviceName) {
                return true
            }
        }
        return false
    }

}