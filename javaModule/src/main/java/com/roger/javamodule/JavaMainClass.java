package com.roger.javamodule;


import java.math.BigDecimal;
import java.math.BigInteger;

public class JavaMainClass {

    public static void main(String[] args) {
        System.out.println("1706775624 -》 " + new BigInteger("1706775624",10).toString(36));
        System.out.println("s865i0 -》 " + new BigInteger("s865i0",36).toString(10));
//        ExcelUtils.createExcelFile("C:\\Users\\ASUS\\Desktop\\final\\a.xls");
//        ExcelUtils.createExcelFile("C:\\Users\\ASUS\\Desktop\\final","b.xls");
    }
}
