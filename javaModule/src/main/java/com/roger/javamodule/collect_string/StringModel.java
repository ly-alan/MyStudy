package com.roger.javamodule.collect_string;

/**
 * @Author Roger
 * @Date 2021/7/21 16:36
 * @Description
 */
public class StringModel {
    //父文件夹的父路径，绝对路径。es:C:\Users\ASUS\Desktop\final\lib_ui/src
    String parentPath;
    //文件名称，es：string.xml,string_pure.xml
    String fileName;
    //xml的父目录文件夹名称,es:value,value-es,value-pt
    String parentName;
    //字符串的名称
    String key;
    //字符串value
    String value;
    //语言
    String language;


    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
