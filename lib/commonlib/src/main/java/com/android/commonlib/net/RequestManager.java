package com.android.commonlib.net;

import androidx.annotation.NonNull;

import com.android.commonlib.net.dns.HttpDNS;
import com.android.commonlib.net.download.FileDownLoadObserver;
import com.android.commonlib.net.interceptor.ChangeIpInterceptor;
import com.android.commonlib.net.response.RequestCallBack;
import com.android.commonlib.utils.Constants;
import com.android.commonlib.utils.L;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static io.reactivex.schedulers.Schedulers.computation;
import static io.reactivex.schedulers.Schedulers.io;


/**
 * Created by liaoyong on 2017/9/28.
 */

public class RequestManager {
    /**
     * 测试图片
     */
    public static String TEST_IMAGE_URL = "http://devimg.manjd.net/shop/20171030153453.jpg";
    private static final String KEY_TAG = "RequestManager";

    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private ChangeIpInterceptor interceptor;

    public RequestManager() {
        initValue();
    }

    private void initValue() {
        if (retrofit == null || retrofitService == null) {
            interceptor = new ChangeIpInterceptor();
            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(interceptor)//重定向拦截器
                    .retryOnConnectionFailure(false)//允许失败重试
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(HttpDNS.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);
        }
    }

    public void doGetRequest(@NonNull Map<String, String> map, @NonNull RequestCallBack callBack) {
        String action = map.get(RetrofitService.PATH_ACTION);
        map.remove(RetrofitService.PATH_ACTION);
        Call<String> call = retrofitService.doGetRequest(action, map);
        call.enqueue(callBack);
        callBack.onStart();
    }

    public void doPostRequest(@NonNull Map<String, String> map, @NonNull RequestCallBack callBack) {
        String action = map.get(RetrofitService.PATH_ACTION);
        map.remove(RetrofitService.PATH_ACTION);
        Call<String> call = retrofitService.doPostRequest(action, map);
        call.enqueue(callBack);
        callBack.onStart();
    }

    public void doUploadFileRequest(String url, @NonNull String filePath, @NonNull RequestCallBack callBack) {
        //构建要上传的文件
        File file = new File(filePath);
        if (!file.exists()) {
            L.e(KEY_TAG, "file no exit");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        String descriptionString = "This is a description";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        Call<String> call = retrofitService.doUploadFile(url, description, body);
        call.enqueue(callBack);
        callBack.onStart();
    }

    /**
     * 下载升级apk
     *
     * @param destDir  文件保存位置
     * @param fileName 文件保存名称
     * @param url      下载路径
     */
    public void downloadFile(String url, final String destDir, final String fileName,
                             @NonNull final FileDownLoadObserver fileDownLoadObserver) {
        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)//重定向拦截器
                .retryOnConnectionFailure(true)//允许失败重试
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        fileDownLoadObserver.onDownLoadStart();
        retrofit.create(RetrofitService.class).doDownloadFile(url)
                .subscribeOn(io())
                .unsubscribeOn(io())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                        return fileDownLoadObserver.saveFile(responseBody, destDir, fileName);
                    }
                })
                .observeOn(computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull File file) throws Exception {
                        fileDownLoadObserver.onDownLoadSuccess(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        fileDownLoadObserver.onDownLoadFail(throwable);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        fileDownLoadObserver.onComplete();
                    }
                });
    }
}
