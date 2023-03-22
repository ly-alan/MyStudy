package com.auto.click

import android.app.Application
import com.auto.click.utils.SpUtils

/**
 * @Author Roger
 * @Date 2023/3/22 15:52
 * @Description
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SpUtils.init(this);
    }
}