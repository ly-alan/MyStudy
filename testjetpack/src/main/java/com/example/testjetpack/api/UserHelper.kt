package com.example.testjetpack.api

import com.example.testjetpack.model.UserResponse


object UserHelper {

    private const val BASE_URL = "https://reqres.in/api/"

    // 懒加载创建 UserApi 实例
    private val userApi: UserApi by lazy {
        RetrofitClient.createService(UserApi::class.java, BASE_URL);
    }


    suspend fun login(name: String, email: String, pwd: String): UserResponse {
        val body = mapOf(
            "username" to name,
            "email" to email,
            "password" to pwd
        )
        return userApi.login(body);
    }
}
