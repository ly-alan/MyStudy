package com.auto.click.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver

/**
 * @Author Roger
 * @Date 2022/9/6 10:11
 * @Description
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
}