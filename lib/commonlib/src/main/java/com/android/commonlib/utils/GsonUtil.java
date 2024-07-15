package com.android.commonlib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by elegant.wang on 2016/8/17.
 */
public class GsonUtil {
    private static GsonBuilder getGsonBuilder() {
        return new GsonBuilder().serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static Gson getGson() {
        return getGsonBuilder().create();
    }
}
