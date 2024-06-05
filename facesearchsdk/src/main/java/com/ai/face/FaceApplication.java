package com.ai.face;

import android.app.Application;

import java.io.File;

public class FaceApplication extends Application {

    public static String CACHE_SEARCH_FACE_DIR; //1:N 人脸搜索目录

    @Override
    public void onCreate() {
        super.onCreate();


        //1:N （M：N）人脸搜索目录
        //增删改人脸 参考@FaceImageEditActivity 中的方式，需要使用SDK 中的API 进行操作不能直接插入图片
        CACHE_SEARCH_FACE_DIR = getFilesDir().getPath() + "/faceSearch";

        File file = new File(CACHE_SEARCH_FACE_DIR);//提前建目录方便导入数据演示
        if (!file.exists()) file.mkdirs();
    }

}
