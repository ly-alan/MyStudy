package com.example.testjetpack.api

import com.example.testjetpack.model.UserResponse
import io.reactivex.rxjava3.core.Flowable

import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("/login")
    fun login(
        @Query("username") userName: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Flowable<UserResponse>
}

//    @GET("/api/ping/v1")
//    fun rxPing(@QueryMap params: Map<String?, String?>?): Observable<ResponseStatus?>?
//
//    /**
//     * 查询调试命令
//     */
//    @GET("/api/dbg/cmd/query/v1")
//    fun rxQueryDebugCmd(
//        @Query("appVer") appVer: Int
//    ): Observable<ResponseDataList<DebugCmdDo?>?>?
//
//    @GET("/api/user/qureyPreferences/v1")
//    fun getQueryPreferences(
//        @QueryMap params: Map<String?, String?>?,
//        @Query("type") common: String?
//    ): Observable<ResponseDataSingle<UserPreferencesDo?>?>?

