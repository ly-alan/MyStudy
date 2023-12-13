package com.roger.main.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.commonlib.base.BaseFragment;
import com.roger.main.R;
import com.roger.main.view.DatePickerDialog;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Author Roger
 * @Date 2023/12/6 9:57
 * @Description
 */
public class ExcelFragment extends BaseFragment {

    //数据条数
    private int numSize = 2000000;

    private int sheet_row = 1000000;

    @Override
    public void setView() {
        super.setView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(ContextCompat.getDataDir(getContext()) + File.separator + numSize + "_测试.xlsx");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("liao", file.getAbsolutePath() + " ---- " + file.exists());
                List<String> list = new ArrayList<>();
                for (int i = 0; i < numSize; i++) {
                    list.add(String.valueOf(i));
                }
                Log.d("liao", "create list ");
//                saveFile_1(file, list);
                saveFile_3(file, list, sheet_row);
                Log.d("liao", "save list success + " + Looper.myLooper());
//                saveFile(file, list);
//                Log.d("liao", "save list success 2+ " + Looper.myLooper());
            }
        }).start();

    }

    //    /**
//     * 5w以上就会卡死
//     *
//     * @param file
//     * @param list
//     */
    private void saveFile(File file, List<String> list, String sheetName) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.getSheet(sheetName) != null ? workbook.getSheet(sheetName) : workbook.createSheet(sheetName);
            Cell cell;
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i);
                //一行几个数
                for (int j = 0; j < 5; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(list.get(i));
                }
            }
            OutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
//            Toast.makeText(context, "另存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 5w数据1s，50w数据6s，100w:11s
     * 一个sheet最多导入1048576条数据
     *
     * @param file
     * @param list
     */
    private void saveFile_1(File file, List<String> list, String sheetName) {
        try {
            //直接打开会报错，需要先用上面的方法先读写一遍文件
            List<String> list1 = new ArrayList<>();
            list1.add("标题");
            saveFile(file, list1, sheetName);
            //每一个XSSFWorkbook最多存1048576条数据，
            XSSFWorkbook workbook1 = new XSSFWorkbook(new FileInputStream(file));
            //SXSSFWorkbook事务缓存，可以存百万级别数据，不过一个Sheet最多只能存100w数据,分页也不行，需要先写入厚
            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook1);

            Log.d("liao", "sheetName = " + sheetName);
            Sheet sheet = sxssfWorkbook.getSheet(sheetName) != null ? sxssfWorkbook.getSheet(sheetName) : sxssfWorkbook.createSheet(sheetName);

            Cell cell;
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                //一行几个数
                for (int j = 0; j < 5; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(list.get(i));
                }
                if (i % 100000 == 0) {
                    Log.d("liao", "page = " + sheetName + " --- i = " + i);
                }
            }

            OutputStream outputStream = new FileOutputStream(file);
            sxssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
//            Toast.makeText(context, "另存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("liao", "文件打开失败,需要设备支持xlsx文件 : " + e);
        }
    }

    /**
     * 5w数据1s，50w数据6s，100w，11s，200w:22s
     * 支持份文件导出
     *
     * @param file
     * @param list        数据总量
     * @param fetchedSize 每个sheet保存多少条数据
     */
    private void saveFile_3(File file, List<String> list, int fetchedSize) {
        try {
            //每一个XSSFWorkbook最多存1048576条数据，
            //计算sheet页数
            int sheetSize = list.size() / fetchedSize + (list.size() % fetchedSize == 0 ? 0 : 1);
            for (int page = 0; page < sheetSize; page++) {
                saveFile_1(file, list.subList(page * fetchedSize, Math.min(list.size(), fetchedSize * (page + 1))), "tab_" + page);
            }
//            Toast.makeText(context, "另存成功", Toast.LENGTH_SHORT).show();
        } catch (
                Exception e) {
            Log.e("liao", "文件打开失败,需要设备支持xlsx文件 : " + e);
        }
    }

}
