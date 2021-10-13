package com.roger.javamodule.collect_string;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找string.xml中的翻译并整理成excel文档
 */
public class CollectStringToExcel {

    private static String ROOT_FILE_PATH = "D:\\Work\\mfc-droid\\lib-ui";

    private static String SAVE_FILE_PATH = "C:\\Users\\ASUS\\Desktop\\final";

    public static void main(String[] args) {
        System.out.println("start traverse");
        traverseXmlForString(new File(ROOT_FILE_PATH));
    }

    private static List<String> title = new ArrayList<>();
    private static List<List<Object>> data = new ArrayList<>();

    /**
     * 遍历文件夹，查找string.xml
     *
     * @param rootFile
     */
    private static void traverseXmlForString(File rootFile) {
        if (rootFile == null) {
            System.out.println("file is null : ");
            return;
        }
        if (!rootFile.exists()) {
            System.out.println("file is not exit : " + rootFile.getAbsolutePath());
            return;
        }
        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                traverseXmlForString(file);
            }
        } else {
            if (!rootFile.getParent().contains("values")) {
                //只读取value文件夹下面的，不是value文件夹下的文件不去读取
                return;
            }
            if (!rootFile.getName().contains("string")) {
                //只读取strings文件
                return;
            }
            System.out.println("读取文件：" + rootFile.getParent() + " : " + rootFile.getName());
            if (XmlUtils.isXmlFile(rootFile.getName())) {
                List<StringModel> stringModels = XmlUtils.getStringMapForXml(rootFile);
                try {
                    System.out.println("目录文件：" + stringModels.get(0).getParentPath() + " : " + stringModels.get(0).getLanguage());
                    if (title.size() == 0 ||
                            !title.get(0).equals(stringModels.get(0).getParentPath() + "+" + stringModels.get(0).getFileName())
                    ) {
                        //不同目录
                        System.out.println("新目录文件");
                        title.clear();
                        data.clear();
                    }
                    //添加title，key列
                    if (title.size() == 0) {
                        //以父文件夹的路径+文件名作为标识
                        title.add(stringModels.get(0).getParentPath() + "+" + stringModels.get(0).getFileName());
                        data.add(new ArrayList<Object>());
                    }
                    //添加语言列
                    title.add(stringModels.get(0).language);
                    data.add(new ArrayList<Object>());

                    for (int i = 0; i < stringModels.size(); i++) {
                        if (data.get(0).size() <= i) {
                            //没有添加过key
                            data.get(0).add(stringModels.get(i).key);
                        }

                        if (data.get(0).get(i).equals(stringModels.get(i).key)) {
                            //key是对的，填入value
                            data.get(title.size() - 1).add(stringModels.get(i).value);
                        } else {
                            //key对不上，两个文件值顺序有区别，找到对应的key的值填入
                            for (StringModel model : stringModels) {
                                if (data.get(0).get(i).equals(model.key)) {
                                    data.get(title.size() - 1).add(model.value);
                                }
                            }
                        }
                    }
                    System.out.println("读取xml文件完成，保存文件信息到excel");
                    saveXmlString(stringModels);
                } catch (Exception e) {
                    System.out.println("Error : " + e.toString());
                }
            } else {
                System.out.println("file is not excel");
            }
        }
    }

    /**
     * 保存数据
     *
     * @param stringModels
     */
    private static void saveXmlString(List<StringModel> stringModels) {
        if (stringModels == null || stringModels.size() == 0) {
            System.out.println("string list is null");
            return;
        }
        File file = checkOrCreateFile(stringModels.get(0).getParentPath(), stringModels.get(0).fileName);
        if (file != null && file.exists()) {
            System.out.println("写入excel内容");
            writeDataToExcel(file);
            System.out.println("写入结束");
        } else {
            System.out.println("create excel file fail");
        }
    }

    /**
     * 检查xml文件是否已经创建
     *
     * @param filePath
     * @param fileName
     * @return
     */
    private static File checkOrCreateFile(String filePath, String fileName) {
        filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
        filePath = filePath.replace("\\", "_");
        filePath = filePath.replace(":", "_");

        return ExcelUtils.createExcelFile(SAVE_FILE_PATH, filePath + "_" + fileName.replace(XmlUtils.XML, ExcelUtils.XLSX));
    }

    /**
     * 写入数据
     *
     * @param excelFile
     */
    public static void writeDataToExcel(File excelFile) {
        try {
            //1:创建工作簿
            Workbook workbook = ExcelUtils.getWorkbookFromExcel(excelFile);
            //2:写入sheet
            ExcelUtils.writeDataToWorkbookVertical(title, data, workbook, 0);
            OutputStream stream = new FileOutputStream(excelFile);
            workbook.write(stream);
//            //最后一步，关闭工作簿
            workbook.close();
        } catch (IOException e) {
            System.out.println("写入excel错误：" + e.toString());
        }
    }
}
