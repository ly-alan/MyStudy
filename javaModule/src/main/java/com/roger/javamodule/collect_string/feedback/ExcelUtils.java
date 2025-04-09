//package com.roger.javamodule.collect_string.feedback;
//
//
//import com.roger.javamodule.util.Log;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @Author Roger
// * @Date 2023/11/3 14:42
// * @Description excel文件的基本操作类
// */
//
//public class ExcelUtils {
//
//    public static final String XLS = ".xls";
//    public static final String XLSX = ".xlsx";
//    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//
//    //下载人合下载备注
//    public static String downloadName = "";
//    public static String downloadDescribe = "";
//
//
//    private static int content_row_start_line = 0;//内容开始行
//
//    /**
//     * 是否为ecxel文件
//     *
//     * @param fileName
//     * @return
//     */
//    public static boolean isExcelFile(String fileName) {
//        if (fileName == null) {
//            return false;
//        }
//        if (fileName.endsWith(XLS) || fileName.endsWith(XLSX)) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * create xls file
//     */
//    public static File createExcelFile(String path, String fileName) {
//        String filePath = path + File.separator + fileName;
//        if (TextUtils.isEmpty(filePath)) {
//            System.out.println("filePath is null");
//            return null;
//        }
//        if (!isExcelFile(filePath)) {
//            System.out.println("only create excel file");
//            return null;
//        }
//        File file = new File(filePath);
//        if (file.exists()) {
//            return file;
//        } else {
//            file.getParentFile().mkdirs();
//        }
//        return file;
//    }
//
//
//    //    /**
////     * 5w以上就会卡死
////     *
////     * @param file
////     * @param list
////     */
//    private static void saveTitle(File file, List<String> list, String sheetName) {
//        try {
//            XSSFWorkbook workbook = new XSSFWorkbook();
//            XSSFSheet sheet = workbook.getSheet(sheetName) != null ? workbook.getSheet(sheetName) : workbook.createSheet(sheetName);
//            Cell cell;
//            content_row_start_line = 0;//每次下载问加你都从第0行开始
//            Row rowTitle = sheet.createRow(content_row_start_line);
//            content_row_start_line++;
//            //第一行标题
//            cell = rowTitle.createCell(0);
//            cell.setCellValue("工程名称：");
//            cell = rowTitle.createCell(1);
//            cell.setCellValue(CommonPreference.getInstance().getString(AttrConstants.INFO_TITLE, ResUtils.getString(R.string.factory_name)));
//            //设置各列的宽度,宽度需要*256，源码里面会除以256
//            sheet.setColumnWidth(0, 10 * 256);
//            sheet.setColumnWidth(1, 11 * 256);
//            sheet.setColumnWidth(2, 12 * 256);
//            sheet.setColumnWidth(3, 12 * 256);
//            sheet.setColumnWidth(4, 12 * 256);
//            sheet.setColumnWidth(5, 12 * 256);
//            sheet.setColumnWidth(6, 12 * 256);
//            //合并标题的行
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));//合并单元格
//            //第二行写分组
//            Row row = sheet.createRow(content_row_start_line);
//            content_row_start_line++;
//            //一行几个数
//            cell = row.createCell(0);
//            cell.setCellValue("序号");
//            cell = row.createCell(1);
//            cell.setCellValue("日期");
//            cell = row.createCell(2);
//            cell.setCellValue("时间");
//            cell = row.createCell(3);
//            cell.setCellValue("温度（℃）");
//            cell = row.createCell(4);
//            cell.setCellValue("湿度（%RH）");
//            cell = row.createCell(5);
//            cell.setCellValue("记录人");
//            cell = row.createCell(6);
//            cell.setCellValue("备注");
//
//            OutputStream outputStream = new FileOutputStream(file);
//            workbook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
////            Toast.makeText(context, "另存成功", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 5w数据1s，50w数据6s，100w:11s
//     * 一个sheet最多导入1048576条数据
//     *
//     * @param file
//     * @param list
//     */
//    public static void saveFile_1(File file, List<Logs> list, String sheetName) {
//        try {
//            //直接打开会报错，需要先用上面的方法先读写一遍文件
//            saveTitle(file, new ArrayList<>(), sheetName);
//            //每一个XSSFWorkbook最多存1048576条数据，
//            XSSFWorkbook workbook1 = new XSSFWorkbook(new FileInputStream(file));
//            //SXSSFWorkbook事务缓存，可以存百万级别数据，不过一个Sheet最多只能存100w数据,分页也不行，需要先写入厚
//            SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(workbook1);
//
//            Log.d("liao", "sheetName = " + sheetName);
//            Sheet sheet = sxssfWorkbook.getSheet(sheetName) != null ? sxssfWorkbook.getSheet(sheetName) : sxssfWorkbook.createSheet(sheetName);
//            //上次保存的日期
//            String lastDate = "";
//            String currentDate = "";
//            int lastDateIndex = 0;//日期开始所在的数据下标,前面有两行标题需要自己+2，用于日期和序号的单元格合并
//            int dateIndex = 0;//日期序号
//            Cell cell;
//            for (int i = 0; i < list.size(); i++) {
//                //前面写入了标题，这里+
//                Row row = sheet.createRow(i + content_row_start_line);
//                //当前的数据的处理日期
//                currentDate = TimeUtils.getTimeFormat(list.get(i).getSaveTime(), "yyyy-MM-dd");
//                if (!TextUtils.equals(currentDate, lastDate)) {
//                    //日期有变化，需要把之前的日期和序号合并,注意+2
//                    if (!TextUtils.isEmpty(lastDate)) {
//                        //不为空才合并
//                        //合并序号
//                        sheet.addMergedRegion(new CellRangeAddress(lastDateIndex + 2, (i - 1) + 2, 0, 0));
//                        //合并日期
//                        sheet.addMergedRegion(new CellRangeAddress(lastDateIndex + 2, (i - 1) + 2, 1, 1));
//                    }
//                    //数据更新，从0就开始更新
//                    lastDate = currentDate;
//                    lastDateIndex = i;//日期的下标更新
//                    dateIndex++;//序号更新+1
//                }
//                //序号
//                cell = row.createCell(0);
//                cell.setCellValue(dateIndex);
//                //日期
//                cell = row.createCell(1);
//                cell.setCellValue(currentDate);
//                //保存时间，小时：分钟
//                cell = row.createCell(2);
//                cell.setCellValue(TimeUtils.getTimeFormat(list.get(i).getSaveTime(), "HH:mm"));
//                //温度
//                cell = row.createCell(3);
//                cell.setCellValue(String.valueOf(list.get(i).getTemperature()));
//                //湿度
//                cell = row.createCell(4);
//                cell.setCellValue(String.valueOf(list.get(i).getHumidity()));
//                //下载记录人
//                cell = row.createCell(5);
//                cell.setCellValue(downloadName);
//                //备注
//                cell = row.createCell(6);
//                cell.setCellValue(downloadDescribe);
//                if (i % 100000 == 0) {
//                    Log.d("liao", "page = " + sheetName + " --- i = " + i);
//                }
//            }
//            //保存完了，把最后一天的数据合并
//            //合并序号
//            sheet.addMergedRegion(new CellRangeAddress(lastDateIndex + 2, (list.size() - 1) + 2, 0, 0));
//            //合并日期
//            sheet.addMergedRegion(new CellRangeAddress(lastDateIndex + 2, (list.size() - 1) + 2, 1, 1));
//
//            OutputStream outputStream = new FileOutputStream(file);
//            sxssfWorkbook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
////            Toast.makeText(context, "另存成功", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            Log.e("liao", "文件打开失败,需要设备支持xlsx文件 : " + e);
//        }
//    }
//
//    /**
//     * 5w数据1s，50w数据6s，100w，11s，200w:22s
//     * 支持份文件导出
//     *
//     * @param file
//     * @param list        数据总量
//     * @param fetchedSize 每个sheet保存多少条数据
//     */
//    public static void saveFile_multi(File file, List<Logs> list, int fetchedSize) {
//        try {
//            //每一个XSSFWorkbook最多存1048576条数据，
//            //计算sheet页数
//            int sheetSize = list.size() / fetchedSize + (list.size() % fetchedSize == 0 ? 0 : 1);
//            for (int page = 0; page < sheetSize; page++) {
//                saveFile_1(file, list.subList(page * fetchedSize, Math.min(list.size(), fetchedSize * (page + 1))), "tab_" + page);
//            }
////            Toast.makeText(context, "另存成功", Toast.LENGTH_SHORT).show();
//        } catch (
//                Exception e) {
//            Log.e("liao", "文件打开失败,需要设备支持xlsx文件 : " + e);
//        }
//    }
//
//}
