package com.auto.click.model

import android.graphics.drawable.Drawable

/**
 * @Author Roger
 * @Date 2022/9/9 14:31
 * @Description 获取到已安装的app信息
 */
class AppInfo {
    //logo
    var icon: Drawable? = null

    //    包名
    var packageName: String? = null

    //    应用名称
    var label: String? = null

    //签名MD5信息
    var sign: String? = null
}