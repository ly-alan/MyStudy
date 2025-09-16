package com.example.testjetpack.api

import com.example.testjetpack.model.UserResponse
import io.reactivex.rxjava3.core.Flowable


object UserHelper {

    private const val BASE_URL = "https://reqres.in/api"

    // 懒加载创建 UserApi 实例
    private val userApi: UserApi by lazy {
        RetrofitClient.createService(UserApi::class.java, BASE_URL);
    }



    fun login(name: String, email: String, pwd: String): Flowable<UserResponse>? {
        return userApi.login(name, email, pwd);
    }
}
