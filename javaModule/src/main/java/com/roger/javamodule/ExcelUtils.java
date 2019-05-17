package com.roger.javamodule;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class ExcelUtils {

    private static String TYPE_FILE = ".xls";

    /**
     * create xls file
     *
     * @param filePath
     */
    public static void createExcelFile(String filePath) {
        if (isEmpty(filePath)) {
            System.out.println("filePath is null");
            return;
        }
        if (!isXls(filePath)) {
            System.out.println("only create " + TYPE_FILE + " file");
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create xls file
     *
     * @param path
     * @param fileName
     */
    public static void createExcelFile(String path, String fileName) {
        if (isEmpty(path) || isEmpty(fileName)) {
            System.out.println("filePath is null");
            return;
        }
        if (!isXls(fileName)) {
            System.out.println("only create " + TYPE_FILE + " file");
            return;
        }
        File file = new File(path + File.separator + fileName);
        if (file.exists()) {
            return;
        }
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件是否是.xls结尾
     *
     * @param fileName
     * @return
     */
    private static boolean isXls(String fileName) {
        if (fileName.endsWith(TYPE_FILE)) {
            return true;
        }
        return false;
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

}
