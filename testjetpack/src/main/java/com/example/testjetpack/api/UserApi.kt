package com.example.testjetpack.api

import com.example.testjetpack.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("login")
    suspend fun login(
        @Body body: Map<String, String>
    ): UserResponse
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

