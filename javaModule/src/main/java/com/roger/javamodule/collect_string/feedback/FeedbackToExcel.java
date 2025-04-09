package com.roger.javamodule.collect_string.feedback;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roger.javamodule.collect_string.ExcelObj;
import com.roger.javamodule.collect_string.ExcelUtils;
import com.roger.javamodule.util.Log;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FeedbackToExcel {
    private static String APP_ID = "watchlist-mobile";
    //要读取的gson文件位置
    private static String ROOT_FILE_PATH = "D:\\work\\vod-appconfig\\src\\main\\resources\\static\\appconfig\\" + APP_ID + "\\feedback\\issues.json";
    //生成的excel文件保存路径和名称
    private static String SAVE_FILE_PATH = "C:\\Users\\admin\\Desktop\\issueIds";
    private static String SAVE_FILE_NAME = APP_ID + ".xlsx";


    public static void main(String[] args) {
        try {
            // 1. 读取 JSON 文件
            String content = new String(Files.readAllBytes(Paths.get(ROOT_FILE_PATH)));

            // 2. 使用 Gson 解析成对象
            Gson gson = new Gson();
            List<ReportMsg> reportMsgs = gson.fromJson(content, new TypeToken<List<ReportMsg>>() {
            }.getType());
            //需要保存的数据
            List<ExcelObj> excelObjList = new ArrayList<>();
            // 3. 访问数据
            for (ReportMsg reportMsg : reportMsgs) {
                Log.d("issues", "------------------------------------");
                for (ReportLog reportLog : reportMsg.getReport_options()) {
                    Log.d("settings", reportLog.getType() + "==" + reportLog.getIssueId() + "==" + reportLog.getIssueDesc());
                    excelObjList.add(new ExcelObj("settings", reportLog.getType(), reportLog.getIssueId(), reportLog.getIssueDesc()));
                }
                for (ReportLog reportLog : reportMsg.getReport_options_playback()) {
                    Log.d("playback", reportLog.getType() + "==" + reportLog.getIssueId() + "==" + reportLog.getIssueDesc());
                    excelObjList.add(new ExcelObj("playback", reportLog.getType(), reportLog.getIssueId(), reportLog.getIssueDesc()));
                }
                for (ReportLog reportLog : reportMsg.getReport_options_subtitle()) {
                    Log.d("subtitle", reportLog.getType() + "==" + reportLog.getIssueId() + "==" + reportLog.getIssueDesc());
                    excelObjList.add(new ExcelObj("subtitle", reportLog.getType(), reportLog.getIssueId(), reportLog.getIssueDesc()));
                }
            }
            //保存数据
            writeDataToExcel(ExcelUtils.createExcelFile(SAVE_FILE_PATH, SAVE_FILE_NAME), excelObjList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入数据
     *
     * @param excelFile
     */
    public static void writeDataToExcel(File excelFile, List<ExcelObj> data) {
        try {
            //1:创建工作簿
            Workbook workbook = ExcelUtils.getWorkbookFromExcel(excelFile);
            //2:写入sheet
            ExcelUtils.writeDataToExcel(data, workbook);
            OutputStream stream = new FileOutputStream(excelFile);
            workbook.write(stream);
//            //最后一步，关闭工作簿
            workbook.close();
        } catch (IOException e) {
            System.out.println("写入excel错误：" + e.toString());
        }
    }
}
