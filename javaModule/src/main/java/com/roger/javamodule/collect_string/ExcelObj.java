package com.roger.javamodule.collect_string;

/**
 * 保存excel的列类
 */
public class ExcelObj {
    public String type;
    public String lang;
    public int issueId;
    public String content;

    public ExcelObj(String type, String lang, int issueId, String content) {
        this.type = type;
        this.lang = lang;
        this.issueId = issueId;
        this.content = content;
    }
}
