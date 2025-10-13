package com.example.testjetpack.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testjetpack.api.UserHelper
import com.example.testjetpack.base.BaseViewModel
import com.example.testjetpack.model.UserResponse
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {
    // 改为：
    val loginState: MutableLiveData<UserResponse> = MutableLiveData<UserResponse>()

    fun login(username: String, email: String, password: String) {
//        UserHelper.login(username, email, password)
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe({ response ->
//                loginState.value = response
//            }, { error ->
//                loginState.value = null
//            })
//            ?.addTo(compositeDisposable)

//            ?.let { compositeDisposable.add(it) }  // 使用 let

        viewModelScope.launch {
            try {
                val response = UserHelper.login(username, email, password)
                loginState.value = response;
            } catch (e: Exception) {
                loginState.value = null
                // 或者根据需求处理特定错误
                e.printStackTrace()
            }
        }
    }
}