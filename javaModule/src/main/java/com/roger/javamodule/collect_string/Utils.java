package com.roger.javamodule.collect_string;

/**
 * @Author Roger
 * @Date 2021/10/12 17:35
 * @Description
 */
class Utils {

    /**
     * 文件夹名称 value,value-es....
     *
     * @param fileName
     * @return
     */
    public static String getLanguageForFileName(String fileName) {
        if (fileName == null) {
            return "default";
        }
        String[] str = fileName.split("-");
        if (str.length >= 2) {
            return str[str.length - 1];
        }
        return "default";
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

}
