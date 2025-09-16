package com.example.testjetpack.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.testjetpack.api.UserHelper
import com.example.testjetpack.base.BaseViewModel
import com.example.testjetpack.model.UserResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(application: Application) : BaseViewModel(application) {
    // 改为：
    val loginState: MutableLiveData<UserResponse> = MutableLiveData<UserResponse>()

    fun login(username: String, email: String, password: String) {
        UserHelper.login(username, email, password)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ response ->
                loginState.value = response
            }, { error ->
                loginState.value = null
            })
            ?.addTo(compositeDisposable)

//            ?.let { compositeDisposable.add(it) }  // 使用 let
    }
}