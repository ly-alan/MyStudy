package com.roger.javamodule.collect_string;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExcelUtils {

    /**
     * 是否为ecxel文件
     *
     * @param fileName
     * @return
     */
    public static boolean isExcelFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        if (fileName.endsWith(XLS) || fileName.endsWith(XLSX)) {
            return true;
        }
        return false;
    }

    /**
     * create xls file
     */
    public static File createExcelFile(String path, String fileName) {
        String filePath = path + File.separator + fileName;
        if (Utils.isEmpty(filePath)) {
            System.out.println("filePath is null");
            return null;
        }
        if (!isExcelFile(filePath)) {
            System.out.println("only create excel file");
            return null;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }
        //可以表示xls和xlsx格式文件的类
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            //新创建的xls需要新创建新的工作簿，offine默认创建的时候会默认生成三个sheet
            Sheet sheet = workbook.createSheet("sheet1");
            sheet.autoSizeColumn(1, true);
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            System.out.println("createWorkBook success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * 输出数据到自定义模版的Excel输出流
     *
     * @param excelTemplate 自定义模版文件
     * @param data          数据
     * @param outputStream  Excel输出流
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static void writeDataToTemplateOutputStream(File excelTemplate, List<List<Object>> data, OutputStream outputStream) throws IOException {
        Workbook book = ExcelUtils.getWorkbookFromExcel(excelTemplate);
        ExcelUtils.writeDataToWorkbookHorizontal(null, data, book, 0);
        ExcelUtils.writeWorkbookToOutputStream(book, outputStream);
    }

    /**
     * 从Excel文件获取Workbook对象
     *
     * @param excelFile Excel文件
     * @return Workbook对象
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static Workbook getWorkbookFromExcel(File excelFile) throws IOException {
        try (
                InputStream inputStream = new FileInputStream(excelFile);
        ) {
            if (excelFile.getName().endsWith(XLS)) {
                return new HSSFWorkbook(inputStream);
            } else if (excelFile.getName().endsWith(XLSX)) {
                return new XSSFWorkbook(inputStream);
            } else {
                throw new IOException("文件类型错误");
            }
        }
    }

    /**
     * 把Workbook对象内容输出到Excel文件
     *
     * @param book Workbook对象
     * @param file Excel文件
     * @throws FileNotFoundException 找不到文件异常，文件已创建，实际不存在该异常
     * @throws IOException           输入输出异常
     */
    public static void writeWorkbookToFile(Workbook book, File file) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        try (OutputStream outputStream = new FileOutputStream(file);) {
            writeWorkbookToOutputStream(book, outputStream);
        }
    }

    /**
     * 把Workbook对象输出到Excel输出流
     *
     * @param book         Workbook对象
     * @param outputStream Excel输出流
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static void writeWorkbookToOutputStream(Workbook book, OutputStream outputStream) throws IOException {
        book.write(outputStream);
    }

    /**
     * 输出数据到Workbook对象中指定页码
     *
     * @param title    标题，写在第一行，可传null
     * @param rowDatas 行数据
     * @param book     Workbook对象
     * @param page     输出数据到Workbook指定页码的页面数
     */
    public static void writeDataToWorkbookHorizontal(List<String> title, List<List<Object>> rowDatas, Workbook book, int page) {
        Sheet sheet = book.getSheetAt(page);

        Row row = null;
        Cell cell = null;

        // 设置横向表头
        if (null != title && !title.isEmpty()) {
            row = sheet.getRow(0);
            if (null == row) {
                row = sheet.createRow(0);
            }

            for (int i = 0; i < title.size(); i++) {
                cell = row.getCell(i);
                if (null == cell) {
                    cell = row.createCell(i);
                }
                cell.setCellValue(title.get(i));
            }
        }
        //设置每行数据
        List<Object> rowData = null;
        for (int i = 0; i < rowDatas.size(); i++) {
            row = sheet.getRow(i + 1);
            if (null == row) {
                row = sheet.createRow(i + 1);
            }
            rowData = rowDatas.get(i);
            if (null == rowData) {
                continue;
            }
            for (int j = 0; j < rowData.size(); j++) {
                cell = row.getCell(j);
                if (null == cell) {
                    cell = row.createCell(j);
                }
                setValue(cell, rowData.get(j));
            }
        }
    }

    /**
     * 输出数据到Workbook对象中指定页码
     *
     * @param title       标题，写在第一行，可传null
     * @param columnDatas 纵列数据
     * @param book        Workbook对象
     * @param page        输出数据到Workbook指定页码的页面数
     */
    public static void writeDataToWorkbookVertical(List<String> title, List<List<Object>> columnDatas, Workbook book, int page) {
        Sheet sheet = book.getSheetAt(page);

        Row row = null;
        Cell cell = null;

        //已经填了多少列
        int index = 0;

        // 设置表头
        if (null != title && !title.isEmpty()) {
            row = sheet.getRow(0);
            if (null == row) {
                row = sheet.createRow(0);
            }
            //先写个10，不会有超过10种语言吧？
            for (index = 0; index < 10; index++) {
                cell = row.getCell(index);
                if (cell != null && !Utils.isEmpty(cell.getStringCellValue())) {
                    continue;
                } else {
                    break;
                }
            }

            for (int i = 0; i < title.size(); i++) {
                if (i == 0) {
                    //第0个是文件路径
                    cell = row.getCell(i);
                    if (null == cell) {
                        cell = row.createCell(i);
                    }
                    //没有写入过，就写入，否则不用覆盖
                    if (Utils.isEmpty(cell.getStringCellValue())) {
                        cell.setCellValue(title.get(i));
                    }
                } else {
                    //找到最后的空列，写入最新的数据（语言）
                    cell = row.getCell(Math.max(i, index));
                    if (null == cell) {
                        cell = row.createCell(Math.max(i, index));
                    }
                    cell.setCellValue(title.get(i));
                }
            }
        }

        List<Object> rowData = null;
        for (int i = 0; i < columnDatas.size(); i++) {
            rowData = columnDatas.get(i);
            if (null == rowData) {
                continue;
            }
            for (int j = 0; j < rowData.size(); j++) {
                //第一行是title，所以要+1
                row = sheet.getRow(j + 1);
                if (null == row) {
                    row = sheet.createRow(j + 1);
                }
                if (i == 0) {
                    //第一个列表是key
                    cell = row.getCell(i);
                    if (null == cell) {
                        cell = row.createCell(i);
                    }
                    //没有写入过key，就写入，否则不用覆盖
                    if (Utils.isEmpty(cell.getStringCellValue())) {
                        setValue(cell, rowData.get(j));
                    }
                }
                if (i > 0) {
                    //value的值
                    //前面列都已经填入了数据，从index列开始填入
                    cell = row.getCell(Math.max(index, i));
                    if (null == cell) {
                        cell = row.createCell(Math.max(index, i));
                    }
                    setValue(cell, rowData.get(j));
                }
            }
        }
    }

    /**
     * 读取Excel文件第一页
     *
     * @param pathname 文件路径名
     * @return 第一页数据集合
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static List<List<Object>> readExcelFirstSheet(String pathname) throws IOException {
        File file = new File(pathname);
        return readExcelFirstSheet(file);

    }

    /**
     * 读取Excel文件第一页
     *
     * @param file Excel文件
     * @return 第一页数据集合
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static List<List<Object>> readExcelFirstSheet(File file) throws IOException {
        try (
                InputStream inputStream = new FileInputStream(file);
        ) {

            if (file.getName().endsWith(XLS)) {
                return readXlsFirstSheet(inputStream);
            } else if (file.getName().endsWith(XLSX)) {
                return readXlsxFirstSheet(inputStream);
            } else {
                throw new IOException("文件类型错误");
            }
        }

    }

    /**
     * 读取xls格式Excel文件第一页
     *
     * @param inputStream Excel文件输入流
     * @return 第一页数据集合
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static List<List<Object>> readXlsFirstSheet(InputStream inputStream) throws IOException {
        Workbook workbook = new HSSFWorkbook(inputStream);
        return readExcelFirstSheet(workbook);
    }

    /**
     * 读取xlsx格式Excel文件第一页
     *
     * @param inputStream Excel文件输入流
     * @return 第一页数据集合
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static List<List<Object>> readXlsxFirstSheet(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        return readExcelFirstSheet(workbook);
    }

    /**
     * 读取Workbook第一页
     *
     * @param book Workbook对象
     * @return 第一页数据集合
     */
    public static List<List<Object>> readExcelFirstSheet(Workbook book) {
        return readExcel(book, 0);
    }

    /**
     * 读取指定页面的Excel
     *
     * @param book Workbook对象
     * @param page 页码
     * @return 指定页面数据集合
     */
    public static List<List<Object>> readExcel(Workbook book, int page) {
        List<List<Object>> list = new ArrayList<>();
        Sheet sheet = book.getSheetAt(page);
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            // 如果当前行为空，则加入空，保持行号一致
            if (null == row) {
                list.add(null);
                continue;
            }
            List<Object> columns = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                columns.add(getValue(cell));
            }

            list.add(columns);
        }

        return list;
    }

    /**
     * 解析单元格中的值
     *
     * @param cell 单元格
     * @return 单元格内的值
     */
    private static Object getValue(Cell cell) {
        if (null == cell) {
            return null;
        }
        Object value = null;
        switch (cell.getCellType()) {
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                // 日期类型，转换为日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                }
                // 数值类型
                else {
                    // 默认返回double，创建BigDecimal返回准确值
                    value = new BigDecimal(cell.getNumericCellValue());
                }
                break;

            default:
                value = cell.toString();
                break;
        }

        return value;
    }

    /**
     * 设置单元格值
     *
     * @param cell  单元格
     * @param value 值
     */
    private static void setValue(Cell cell, Object value) {
        if (null == cell) {
            return;
        }
        if (null == value) {
            cell.setCellValue((String) null);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue(FORMAT.format((Date) value));
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }

}
