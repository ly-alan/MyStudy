package com.android.commonlib.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.android.commonlib.base.BaseApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * FileUtils 文件处理工具
 */
public class FileUtils {

    /**
     * 获取文件存储路径
     *
     * @return
     */
    public static String getStoragePath() {
        String storagePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            storagePath = BaseApplication.getInstance().getCacheDir().getAbsolutePath();
        }
        return storagePath;
    }

    /**
     * 判断SD卡是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建文件
     *
     * @param fileName 文件名称
     * @return
     */
    public File createFile(String fileName) {
        File dirFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + fileName + File.separator);
        if (!dirFile.exists()) {
            // 如果不存在，则创建该目录
            dirFile.mkdirs();
        }
        return dirFile;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirPath
     * @return
     */
    public static void createDirs(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            // 如果不存在，则创建该目录
            dirFile.mkdirs();
        }
    }

    /**
     * 判断该路径文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            return f.exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取SD卡的剩余容量,单位是Byte
     *
     * @return
     */
    public static long getSDAvailableSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 取得sdcard文件路径
            File pathFile = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(pathFile.getPath());
            // 获取SDCard上每个block的SIZE
            long nBlocSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();
            // 计算 SDCard 剩余大小Byte
            return nAvailaBlock * nBlocSize;
        }
        return 0;
    }

    /**
     * 获取文件夹大小
     * 1024bytes=1KB,1024KB=1MB.
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        return getFileSize(new File(path));
    }

    /**
     * 获取文件夹大小
     * 1024bytes=1KB,1024KB=1MB.
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (File file1 : files) {
                size += getFileSize(file1);
            }
        } else {
            size += file.length();
        }
        return size;
    }

    /**
     * 删除SD卡下路径文件/文件夹，及其所有的子文件
     *
     * @param path 文件/文件夹 路径
     * @return
     */
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            System.out.print("file is no exist");
            return true;
        }
        boolean ret;
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                deleteFile(file1.getPath());
            }
            ret = file.delete();
        } else {
            ret = file.delete();
        }
        return ret;
    }

    /**
     * 通过文件名类型来删除文件
     *
     * @param path 路径
     * @param type 类型（如.mp3,.txt等）
     * @return
     */
    public static void deleteFileByEnd(String path, String type) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.print("file is no exist");
            return;
        }
        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (file1.isFile() && file1.getName().endsWith(type)) {
                    file1.delete();
                }
            }
        } else {
            if (file.isFile() && file.getName().endsWith(type)) {
                file.delete();
            }
        }
    }

    /**
     * 将数据写入文件中
     *
     * @param fileName
     * @param strContent
     */
    public static void printDataToFile(String path, String fileName, String strContent) {
        if (TextUtils.isEmpty(fileName)) {
            fileName = "data";
        }
        if (TextUtils.isEmpty(path)) {
            path = Constants.DATA_CACHE_PATH;
        }
        //创建保存路径文件夹
        File dir = new File(path);
        dir.mkdirs();
        //组合文件保存路径
        String filePath = new StringBuilder(path).append(fileName).append("-")
                .append(new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault())
                        .format(new Date())).append(".log").toString();
        //创建文件
        File log = new File(filePath);
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入数据
        byte[] b = strContent.getBytes();
        BufferedOutputStream stream = null;
        try {
            FileOutputStream fstream = new FileOutputStream(log);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 将数据写入文件中(如果文件存在，则续存)
     *
     * @param fileName
     * @param strContent
     */
    public synchronized static void printDataToTheFile(String fileName, String strContent) {
            try {
                if (TextUtils.isEmpty(fileName)) {
                    fileName = "data";
                }
                File dir = new File(Constants.DATA_CACHE_PATH);
                dir.mkdirs();
                String filePath = Constants.DATA_CACHE_PATH + fileName + ".txt";
                File log = new File(filePath);
                boolean isExisted = log.exists();
                if (!isExisted) {// 不存在
                    try {
                        log.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {// 存在，添加换行符
                    strContent = "\r\n" + strContent;
                }

                byte[] b = strContent.getBytes();
                BufferedOutputStream stream = null;
                try {
                    FileOutputStream fstream = new FileOutputStream(log,
                            isExisted);
                    stream = new BufferedOutputStream(fstream);
                    stream.write(b);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
