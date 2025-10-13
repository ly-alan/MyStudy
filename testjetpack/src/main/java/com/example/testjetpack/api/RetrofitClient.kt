package com.example.testjetpack.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    fun <T> createService(serviceClass: Class<T>, baseUrl: String): T {
        var retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
//            .addConverterFactory(ScalarsConverterFactory.create())  // 字符串转换器
            .addConverterFactory(GsonConverterFactory.create())//gson解析
//            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(createOkHttpClient())
            .build()
        return retrofit.create(serviceClass)
    }


    private fun createOkHttpClient(): OkHttpClient {
        // 自定义拦截器确认请求
        val requestCheckInterceptor = Interceptor { chain ->
            val request = chain.request()
            println("=== 网络请求开始 ===")
            println("URL: ${request.url}")
            println("Method: ${request.method}")
            println("Headers: ${request.headers}")
            println("=== 网络请求结束 ===")

            val response = chain.proceed(request)

            println("=== 网络响应 ===")
            println("Code: ${response.code}")
            println("Message: ${response.message}")
            println("=== 网络响应结束 ===")

            response
        }
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // 显示完整响应体
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(requestCheckInterceptor)  // 先添加自定义拦截器
            .addInterceptor(loggingInterceptor)
            .build()
    }
}