package com.roger.javamodule;

import java.util.logging.Logger;

public class JavaMainClass {

    public static void main(String[] args) {
        System.out.println("log");
        ExcelUtils.createExcelFile("C:\\Users\\ASUS\\Desktop\\final\\a.xls");
        ExcelUtils.createExcelFile("C:\\Users\\ASUS\\Desktop\\final","b.xls");
    }
}
