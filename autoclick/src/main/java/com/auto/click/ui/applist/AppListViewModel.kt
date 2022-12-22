package com.auto.click.ui.applist

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.auto.click.base.BaseViewModel
import com.auto.click.model.AppInfo
import com.auto.click.utils.Utils
import kotlin.concurrent.thread

/**
 * @Author Roger
 * @Date 2022/9/19 14:54
 * @Description
 */
class AppListViewModel(application: Application) : BaseViewModel(application) {

    var appLiveData = MutableLiveData<List<AppInfo>>();

    init {
        thread {
            appLiveData.postValue(Utils.getAllAppInfo(getApplication(), true))
        }
    }
}