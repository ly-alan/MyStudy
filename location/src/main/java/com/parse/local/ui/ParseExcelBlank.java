package com.parse.local.ui;


import com.parse.local.excel.ExcelUtils;
import com.parse.local.excel.Utils;
import com.parse.local.gaode.AmapPoi;
import com.parse.local.gaode.BankBranchLocator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ParseExcelBlank {

    /**
     * 处理Excel文件：读取C列数据，处理后写入D、E列
     *
     * @param excelFile      Excel文件
     * @param input          带解析参数所在列
     * @param blankName      输出参数所在列
     * @param province       输入参数3
     * @param statusCallback 状态回调接口
     * @return 处理是否成功
     */
    public static boolean processExcelFile(File excelFile,
                                           String input,
                                           String blankName,
                                           String province,
                                           StatusCallback statusCallback) {

        Workbook workbook = null;
        OutputStream fos = null;
        try {
            statusCallback.updateStatus("正在打开Excel文件...");
            // 判断文件类型并创建Workbook
            workbook = ExcelUtils.getWorkbookFromExcel(excelFile);
            // 获取第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();

            statusCallback.updateStatus("开始处理数据，共 " + totalRows + " 行数据...");

            // 读取C列数据并处理
            List<ProcessData> processList = new ArrayList<>();
            for (int i = 0; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                // 读取列数据（索引从0开始，按输入的列来找到索引）
                Cell inputCell = row.getCell(ExcelUtils.columnLetterToIndex(input));
                String originalData = getCellValueAsString(inputCell);

                // 跳过空行和标题行（根据实际情况调整）
                if (originalData == null || originalData.trim().isEmpty()) {
                    continue;
                }
                if (originalData.contains("支行") || originalData.contains("分行")) {
                    //保存需要解析的行
                    processList.add(new ProcessData(i, row, originalData));
                }
            }
            // 第二步：处理数据
            int processedRows = 0;
            for (ProcessData data : processList) {
                statusCallback.updateStatus("正在处理第 " + (data.rowIndex + 1) + " 行数据: " + data.originalData);
                // 解析地址
                AmapPoi result = performTimeConsumingOperation(data.originalData);
                if (result == null || Utils.isEmpty(result.province)) {
                    for (int retry = 3; retry > 0; retry--) {
                        statusCallback.updateStatus("解析<" + data.originalData + ">失败,重试");
                        result = performTimeConsumingOperation(data.originalData);
                        if (result != null && !Utils.isEmpty(result.province)) {
                            break;
                        }
                    }
                    if (result == null || Utils.isEmpty(result.province)) {
                        continue;
                    }
                }
                // 更新状态
                statusCallback.updateStatus("解析<" + data.originalData + ">成功 正在写入: " + result.getProvince());
                if (!Utils.isEmpty(blankName)) {
                    // 创建或获取D列单元格（第3列）
                    Cell cellD = data.row.getCell(ExcelUtils.columnLetterToIndex(blankName));
                    if (cellD == null) {
                        cellD = data.row.createCell(ExcelUtils.columnLetterToIndex(blankName));
                    }
                    //返回的name包含了支行
                    cellD.setCellValue(getBlankName(result.getName()));
                }
                if (!Utils.isEmpty(province)) {
                    // 创建或获取E列单元格（第4列）
                    Cell cellE = data.row.getCell(ExcelUtils.columnLetterToIndex(province));
                    if (cellE == null) {
                        cellE = data.row.createCell(ExcelUtils.columnLetterToIndex(province));
                    }
                    cellE.setCellValue(result.getProvince() + "&" + result.getCity());
                }

                processedRows++;
            }

            statusCallback.updateStatus("正在保存文件...");

            // 保存文件（在原文件基础上保存）
            fos = new FileOutputStream(excelFile);
            workbook.write(fos);
            workbook.close();
            statusCallback.updateStatus("处理完成！共成功处理 " + processedRows + " 行数据");
            return true;

        } catch (Exception e) {
            statusCallback.updateError("处理出现异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // 关闭资源
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取单元格值的字符串表示
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }


    // 辅助类
    static class ProcessData {
        int rowIndex;
        Row row;
        String originalData;

        ProcessData(int rowIndex, Row row, String originalData) {
            this.rowIndex = rowIndex;
            this.row = row;
            this.originalData = originalData;
        }
    }

    /***
     * 获取银行名称
     * @param name
     * @return
     */
    private static String getBlankName(String name) {
        if (Utils.isEmpty(name)) {
            return "";
        }
        String[] list = name.trim().split("\\(");
        if (list.length > 0) {
            return list[0];
        }
        return name;
    }

    /**
     * 模拟耗时操作 - 替换为您的实际业务逻辑
     */
    private static AmapPoi performTimeConsumingOperation(String originalData) {
        return BankBranchLocator.searchBankBranch(originalData);
    }

    /**
     * 状态回调接口
     */
    public interface StatusCallback {
        void updateStatus(String status);

        void updateError(String status);
    }
}
