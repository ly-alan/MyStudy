package com.roger.javamodule.multi_flavor_package;

import com.roger.javamodule.util.JarUtils;
import com.roger.javamodule.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @Author Roger
 * @Date 2023/3/16 17:12
 * @Description 多渠道打包，对加固后的apk打入渠道
 */
public class MultiFlavor {
    //原apk
    private static String ORIGIN_APK = "";
    //渠道包输出路径
    private static String SAVE_FILE_PATH = "";
//    private static String SAVE_FILE_PATH = "C:\\Users\\ASUS\\Desktop\\test2";

    public static void main(String[] args) {
        System.out.println("start traverse");
        ORIGIN_APK = selectOriginApkPath();
        if (ORIGIN_APK == null || ORIGIN_APK.length() == 0) {
            System.out.println("Select apk is null");
            return;
        }
        SAVE_FILE_PATH = JarUtils.getFileParentPath(ORIGIN_APK)
                + JarUtils.File_Separator +
                "outputApk-" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());

        logs(SAVE_FILE_PATH);

        createFlavorApk(ORIGIN_APK, SAVE_FILE_PATH);
    }


    /**
     * 选择原包路径
     *
     * @return
     */
    private static String selectOriginApkPath() {
        JFileChooser chooser = new JFileChooser(JarUtils.getJarRootPath());
        chooser.setDialogTitle("root:");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//仅选择文件
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                if (f.isFile() && f.getName().endsWith(".apk")) {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return null;
            }
        });
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f != null) {
            return f.getPath();
        }
        return "";
    }

    /**
     * 创建渠道包
     */
    private static void createFlavorApk(String originApk, String outputPath) {
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        //https://github.com/Meituan-Dianping/walle/blob/master/app/channel
        //walle_channel_cli.jar需要和可执行jar在一个目录下，名字不能变。这个是，美团的多渠道打包工具，读取需要对应的库
        String cmd = String.format("java -jar %s batch -f %s %s %s",
                JarUtils.getJarRootPath() + JarUtils.File_Separator + "walle-cli-all.jar",  //walle包位置
                JarUtils.getJarRootPath() + JarUtils.File_Separator + "channel_list.txt",   //渠道文件
                originApk,  //需要加渠道的包
                SAVE_FILE_PATH  //输出目录
        );
//        String cmd = String.format("java -jar %s batch -f %s %s",
//                JarUtils.getJarRootPath() + JarUtils.File_Separator + "walle-cli-all.jar",  //walle包位置
//                JarUtils.getJarRootPath() + JarUtils.File_Separator + "channel_list",   //渠道文件
//                originApk  //需要加渠道的包
//        );
        execute(cmd);
    }


    private static void execute(String cmd) {
        logs("执行渠道包脚本");
        try {
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(cmd);
            pro.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            logs("打渠道包失败");
        }
    }

    private static void logs(String msg) {
        Log.i("MultiFlavor", msg);
    }

}
