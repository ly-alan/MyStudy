package com.android.commonlib.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp 请求时IP重新指向拦截
 */

public class ChangeIpInterceptor implements Interceptor {

    public ChangeIpInterceptor() {
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest = createNewRequest(chain);
        Response response = doRequest(chain, newRequest);
        if (response == null) {
            throw new IOException();
        }
        return response;
    }

    private Request createNewRequest(Chain chain) {
        Request request = chain.request();
        return request;
//        Request.Builder requestBuilder = request.newBuilder();
//        //TODO  添加header
//        requestBuilder.addHeader("Content-Type", "application/json; charset=utf-8");
//        return requestBuilder.build();
    }


    private Response doRequest(Chain chain, Request request) {
        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
        }
        return response;
    }

}
