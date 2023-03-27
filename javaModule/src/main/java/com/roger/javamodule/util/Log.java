package com.roger.javamodule.util;

/**
 * @Author Roger
 * @Date 2023/3/17 16:05
 * @Description
 */
public class Log {

    public static void d(String tag, String msg) {
        System.out.println(tag + "," + msg);
    }

    public static void i(String tag, String msg) {
        System.out.println(tag + "," + msg);
    }

    public static void e(String tag, String msg) {
        System.out.println(tag + "," + msg);
    }

    public static void w(String tag, String msg) {
        System.out.println(tag + "," + msg);
    }

    public static void v(String tag, String msg) {
        System.out.println(tag + "," + msg);
    }


    public static void logs(String msg) {
        System.out.println(msg);
    }

}
