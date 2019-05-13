package com.android.commonlib.net;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


/**
 * Created by liaoyong on 2017/9/28.
 */

public interface RetrofitService {

    String PATH_ACTION = "action";

    @GET("/v2/movie/{" + PATH_ACTION + "}")
    Call<String> doGetRequest(@Path(PATH_ACTION) String action, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/v2/movie/{" + PATH_ACTION + "}")
    Call<String> doPostRequest(@Path(PATH_ACTION) String action, @FieldMap Map<String, String> map);

    @Multipart
    @POST
    Call<String> doUploadFile(@Url String url, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Streaming
    @GET
    Observable<ResponseBody> doDownloadFile(@Url String url);

}
