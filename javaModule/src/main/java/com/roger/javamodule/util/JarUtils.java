package com.roger.javamodule.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Author Roger
 * @Date 2023/3/17 15:17
 * @Description
 */
public class JarUtils {

    private static String JAR_PATH = "";

    public static final String File_Separator = "\\";


    public static String getJarRootPath() {
        if (JAR_PATH == null || JAR_PATH.length() == 0) {
            JAR_PATH = getJarPath();
        }
        return JAR_PATH;
    }

    public static String getFileParentPath(String file) {
        return getFileParentPath(new File(file));
    }

    public static String getFileParentPath(File file) {
        return file.getParent();
    }

    /**
     * 获取jar的路径
     *
     * @return
     */
    private static String getJarPath() {
        try {
            //获取当前类所在的位置，会定位到com.roger.javamodule.util。但是我们只需要jar的路径
            String root = (new JarUtils()).getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            //解决中文乱码问题
            root = URLDecoder.decode(new File(root).getAbsolutePath(), "UTF-8");
            //找到jar的路径
            String path = root;
            //按这个文件的位置目录一级一级往上找
            while (path.contains(File_Separator)) {
                if (path.endsWith(".jar")) {
                    //找到了jar的位置，返回路径
                    return path.substring(0, path.lastIndexOf(File_Separator));
                } else {
                    path = path.substring(0, path.lastIndexOf(File_Separator));
                }
            }
            return root;
        } catch (UnsupportedEncodingException e) {
            //...
        }
        return "";
    }
}
