package com.roger.c_annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class TestUtils {

    public static void inject(Object object) {
        try {
            Class clazz = object.getClass();
            Constructor constructor = clazz.getConstructor(object.getClass());
            constructor.newInstance(object);
        } catch (Exception e) {
            System.out.println("inject error");
        }
    }

    //由于我的 ViewBinder 所在工程是一个 Java工程, 所以没法使用 Activity, 故使用 class.isInstance() 来判断是否是 activity
    public static void bind(Object object) {
        if (object == null)
            return;
        try {
//            Class<?> clazz = Class.forName("android.app.Activity");
//            if (clazz != null) {
//                if (clazz.isInstance(object)) {
                    Class<?> activityClass = object.getClass();
                    Class<?> ViewBindingClass = Class.forName(activityClass.getCanonicalName() + "_ViewBinding");
                    Constructor<?> constructor = ViewBindingClass.getConstructor(activityClass);
                    Object ViewBinding = constructor.newInstance(object);
//                }
//            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
