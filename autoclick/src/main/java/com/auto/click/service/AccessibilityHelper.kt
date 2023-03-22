package com.auto.click.service

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

/**
 * @Author Roger
 * @Date 2022/9/9 9:42
 * @Description
 */
object AccessibilityHelper {

    fun jumpToAccessibilitySetting(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "jumpToSetting: " + e.message)
        }
    }

    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    fun checkAccessibilityOpen(cxt: Context, service: Class<*>): Boolean {
        try {
            val enable = Settings.Secure.getInt(cxt.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
            if (enable != 1) return false
            val services =
                Settings.Secure.getString(cxt.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (!TextUtils.isEmpty(services)) {
                val split = TextUtils.SimpleStringSplitter(':')
                split.setString(services)
                // 遍历所有已开启的辅助服务名
                while (split.hasNext()) {
                    if (split.next().equals(cxt.packageName + "/" + service.name, ignoreCase = true)) {
                        return true
                    }
                }
            }
        } catch (e: Throwable) {
            //若出现异常，则说明该手机设置被厂商篡改了,需要适配
            Log.e(ContentValues.TAG, "isSettingOpen: " + e.message)
        }
        return false
    }

}