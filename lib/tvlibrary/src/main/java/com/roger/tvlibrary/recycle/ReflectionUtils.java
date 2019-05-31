package com.roger.tvlibrary.recycle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by roger on 2016/5/7.
 */
class ReflectionUtils {
    /**
     * 将反射时的 "检查异常" 转换为 "运行时异常"
     */
    public static IllegalArgumentException convertToUncheckedException(Exception ex) {
        if (ex instanceof IllegalAccessException || ex instanceof IllegalArgumentException
                || ex instanceof NoSuchMethodException) {
            throw new IllegalArgumentException("反射异常", ex);
        } else {
            throw new IllegalArgumentException(ex);
        }
    }


    /**
     * 循环向上转型, 获取对象的 DeclaredMethod
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                //superClass.getMethod(methodName, parameterTypes);
//                if (parameterTypes == null) {
//                    return superClass.getDeclaredMethod(methodName);
//                } else {
//                    return superClass.getDeclaredMethod(methodName, parameterTypes);
//                }
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                //Method 不在当前类定义, 继续向上转型
            }
            //..
        }

        return null;
    }

    /**
     * 使 filed 变为可访问
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     */
    public static Field getDeclaredField(Object object, String filedName) {

        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                //Field 不在当前类定义, 继续向上转型
            }
        }
        return null;
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes,
                                      Object[] parameters) throws InvocationTargetException {

        Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {

        }

        return null;
    }

    /**
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {

        }
    }

    /**
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        if (field == null)
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        Object result = null;

        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {

        }

        return result;
    }

    /**
     * 获取静态属性
     */
    public static Object getStaticFieldValue(String className, String fieldName) {
        Object value = null;
        try {
            Class clazz = Class.forName(className);
            Field field = clazz.getField(fieldName);
            value = field.get(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getIntFromR(String className, String fieldName) {
        try {
            Class clazz = Class.forName(className);
            for (Class c : clazz.getClasses()) {
                if (c.getSimpleName().equals("id")) {
                    return (Integer) c.getDeclaredField(fieldName).get(c);
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    public static void setStaticFinalFieldValue(Object instance, String className, String fieldName, Object fieldValue) {
        try {
            Class clazz = Class.forName(className);
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, fieldValue);

            /*Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);*/
            /*Field slotField = Field.class.getDeclaredField("slot");
            slotField.setAccessible(true);
            slotField.set(field, 3);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接调用对象方法, 而忽略修饰符(private, protected)
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Class<?>[] parameterTypes,
                                            Object[] parameters) throws InvocationTargetException, NoSuchMethodException {
        Method method = clazz.getMethod(methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + clazz + "]");
        }

        // method.setAccessible(true);

        try {
            return method.invoke(null, parameters);
        } catch (IllegalAccessException e) {

        }

        return null;
    }
}
