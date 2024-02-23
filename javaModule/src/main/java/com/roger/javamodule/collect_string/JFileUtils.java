package com.roger.javamodule.collect_string;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * @Author Roger
 * @Date 2024/2/22 19:40
 * @Description windows弹窗处理
 */
public class JFileUtils {

    /**
     * 选择文件夹路径
     *
     * @return
     */
    public static String selectFolderPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f != null) {
            return f.getPath();
        }
        return "";
    }
}
