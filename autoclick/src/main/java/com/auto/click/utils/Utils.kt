package com.auto.click.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.auto.click.model.AppInfo
import java.security.MessageDigest

/**
 * @Author Roger
 * @Date 2022/9/6 11:03
 * @Description
 */
//object生命为单利类，可以通过类名.方法名使用
//真正的静态函数
//@JvmStatic注解
//如果给单例类(object)和伴生类中(companion object)的方法加上@JvmStatic注解，就会成为真正的静态方法，在kotlin和java文件中都可以调用。
//注意：@JvmStatic只能加在单例类和伴生类中的对象上。如果加在一个普通方法上，就会报错。
object Utils {

    /**
     * 获取手机已安装应用列表
     * @param ctx
     * @param isFilterSystem 是否过滤系统应用
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.P)
    fun getAllAppInfo(ctx: Context, isFilterSystem: Boolean): ArrayList<AppInfo> {
        val appBeanList: ArrayList<AppInfo> = ArrayList()
        var bean = AppInfo()
        val packageManager: PackageManager = ctx.getPackageManager()
        val list: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_SIGNING_CERTIFICATES)
        for (p in list) {
            bean = AppInfo()
            bean.icon = p.applicationInfo.loadIcon(packageManager);
            bean.label = (packageManager.getApplicationLabel(p.applicationInfo).toString())
            bean.packageName = (p.applicationInfo.packageName)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    bean.sign = md5(p.signingInfo.apkContentsSigners[0].toByteArray())
                } else {

                }
            } catch (e: Exception) {

            }
            val flags: Int = p.applicationInfo.flags
            // 判断是否是属于系统的apk
            if (flags and ApplicationInfo.FLAG_SYSTEM !== 0 && isFilterSystem) {
//                bean.setSystem(true);
                continue
            }
            appBeanList.add(bean)
        }
        return appBeanList
    }


    fun md5(bytes: ByteArray?): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(bytes)
            val b = md.digest()
            var i: Int
            val sb = StringBuilder()
            for (value in b) {
                i = value.toInt()
                if (i < 0) i += 256
                if (i < 16) sb.append("0")
                sb.append(Integer.toHexString(i))
            }
            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}