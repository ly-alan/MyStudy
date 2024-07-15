package com.roger.main.youtube.lib;

import android.content.Context;

import java.io.File;

import dalvik.system.DexClassLoader;

public class DexHotPlugin {
    private static ClassLoader sClassLoader;

    public DexHotPlugin() {
    }

    public static void init(Context context, String dexFilePath) {
        try {
            File dexFile = new File(dexFilePath);
            if (!dexFile.exists()) {
                return;
            }

            ClassLoader classLoader = new DexClassLoader(dexFilePath, context.getDir("dex", 0).getAbsolutePath(), (String) null, context.getClassLoader());
            sClassLoader = classLoader;
        } catch (Exception var4) {
        }

    }

    public static <T> T create(Class<T> exceptClass, String className) {
        if (sClassLoader == null) {
            return null;
        } else {
            try {
                Class newClass = sClassLoader.loadClass(className);
                T t = (T) newClass.newInstance();
                return t;
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }
}
