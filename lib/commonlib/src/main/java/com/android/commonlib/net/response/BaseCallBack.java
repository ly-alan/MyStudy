package com.android.commonlib.net.response;

import android.text.TextUtils;

import com.android.commonlib.utils.L;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liaoyong on 2017/9/28.
 */

public class BaseCallBack<T> implements Callback<T> {

    void onStart() {

    }

    void onSuccess(T t) {

    }

    void onFail() {

    }

    void onInterruptResponse() {
        //拦截返回
    }

    void onComplete() {

    }

    @Override
    public void onResponse(Call call, Response response) {
        String result = (String) response.body();
        L.d("requestCallBack", "onResponse --> " + result);
        if (response.code() != 200) {
            //请求失败
            onFail();
            onComplete();
            return;
        }
        if (TextUtils.isEmpty(result)) {
            onFail();
            onComplete();
            return;
        }
        try {
            //TODO 根据数据格式自己解析，此处只是示例
            Gson gson = new Gson();
            //解析返回的json字符串对象
            Type mySuperClass = this.getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
            T object = gson.fromJson(result, type);
            onSuccess(object);
            onComplete();

        } catch (Exception e) {
            onFail();
            onComplete();
        }

    }

    @Override
    public void onFailure(Call call, Throwable t) {
        onFail();
        onComplete();
    }
}
