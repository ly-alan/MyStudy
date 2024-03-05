package com.roger.javamodule.collect_string;

import com.roger.javamodule.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @Description excel文件转化为android的strings文件
 */
public class ExcelToStrings {
    //android根目录地址
//    private static String ROOT_PROJECT_PATH = "D:\\Work\\live\\tve3-android\\mix-mobile";
    private static String ROOT_PROJECT_PATH = "D:\\work\\cv-media-mobile-v2";

    //想要读取的excel文件地址
    private static String EXCEL_FILE_PATH = "D:\\Program Files\\Lark\\mfc_mobile_string.xls";
    //   excel第一列的char（一般都是ABCD），常量，用来计算value在那一列
    private static final char EXCEL_COLUMN_START_KEY = 'A';
    //excel保存key在哪一列，需要大写
    private static char EXCEL_COLUMN_KEY = 'A';
//    //key从哪一行开始
//    private static int EXCEL_COLUMN_KEY_START_ROW = 2;

    //excel保存value在哪一列,需要大写
    private static char EXCEL_COLUMN_VALUE = 'E';
//    //value从哪一行开始
//    private static int EXCEL_COLUMN_VALUE_START_ROW = 1;

    //strings文件的key有多少个，从起始地址向下扫描多少行（不用格子为空判断，防止中间有很多空行）
    private static int MAX_STRINGS_NUM = 5000;

    //保存的文件路径，如果有多个string文件，下面会新建多个strings.xml文件
//    private static String SAVE_FILE_PATH = "C:\\Users\\ASUS\\Desktop\\StringDist";
    private static String SAVE_FILE_PATH = "C:\\Users\\admin\\Desktop\\final\\mfc_string";

    //    生成的string.xml保存文件夹，第一行一般都是语言标识，可以考虑按第一行命名
    private static String SAVE_FILE_FOLDER = "values-ru";


    //遍历的excel中的键值对
    private static Map<String, String> excelMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("start traverse");

        //弹窗选择excel和保存文件的位置
//        EXCEL_FILE_PATH = JFileUtils.selectFolderPath();
//        if (EXCEL_FILE_PATH == null || EXCEL_FILE_PATH.length() == 0) {
//            System.out.println("Select folder is null");
//            return;
//        }
//        SAVE_FILE_PATH = EXCEL_FILE_PATH + File.separator + "dist";

        File saveFile = new File(SAVE_FILE_PATH);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        //遍历excel中的key和value值，保存起来
        traverseExcel(new File(EXCEL_FILE_PATH), excelMap);
        //遍历android项目中的strings.xml文件
        traverseProjectToStringXml(new File(ROOT_PROJECT_PATH));
    }

    /**
     * 遍历excel中的key-value
     *
     * @param excelFile excel文件
     */
    private static void traverseExcel(File excelFile, Map<String, String> excelMap) {
        try {
            //读取所有的string
            List<List<Object>> result = ExcelUtils.readExcelFirstSheet(excelFile);
            //按key转换为map,从指定行开始
            for (int i = 0; i < Math.min(result.size(), MAX_STRINGS_NUM); i++) {
                List<Object> rowList = result.get(i);//指定行的单元格
                try {
                    excelMap.put(String.valueOf(rowList.get(EXCEL_COLUMN_KEY - EXCEL_COLUMN_START_KEY)),
                            String.valueOf(rowList.get(EXCEL_COLUMN_VALUE - EXCEL_COLUMN_START_KEY)));
                } catch (Exception e) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历文件夹，查找string.xml
     *
     * @param projectRootFile android项目的跟目录
     */
    private static void traverseProjectToStringXml(File projectRootFile) {
        if (projectRootFile == null) {
            System.out.println("file is null : ");
            return;
        }
        if (!projectRootFile.exists()) {
            System.out.println("file is not exit : " + projectRootFile.getAbsolutePath());
            return;
        }
        if (projectRootFile.isDirectory()) {
            File[] files = projectRootFile.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                traverseProjectToStringXml(file);
            }
        } else {
            if (!projectRootFile.getParent().contains("res\\values")) {
                //只读取res\value文件夹下面的，不是value文件夹下的文件不去读取
                return;
            }
            if (!projectRootFile.getName().contains("string")) {
                //只读取strings文件
                return;
            }
            System.out.println("读取文件：" + projectRootFile.getParent() + " : " + projectRootFile.getName());
            if (XmlUtils.isXmlFile(projectRootFile.getName())) {
                //读取原有的文件
                List<StringModel> stringModels = XmlUtils.getStringMapForXml(projectRootFile);
                saveXmlString(stringModels);
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
        try {
            System.out.println("目录文件：" + stringModels.get(0).getParentPath() + " : " + stringModels.get(0).getLanguage());
            String filePath = stringModels.get(0).getParentPath();
            filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
            filePath = filePath.replace("\\", "_");
            filePath = filePath.replace(":", "_");
            File parent = new File(SAVE_FILE_PATH + File.separator + filePath + File.separator + SAVE_FILE_FOLDER);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            File xmlfile = new File(parent, "strings" + XmlUtils.XML);
            if (!xmlfile.exists()) {
                xmlfile.createNewFile();
            }

            Document doc = null;
            Element root = null;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.newDocument();
                root = doc.createElement("resources");
                doc.appendChild(root);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Element elementString;
            //将原有的文件所含有的key写入到另一个xml，值从excel文件中读取
            for (StringModel stringModel : stringModels) {
                try {
                    elementString = doc.createElement("string");
                    elementString.setAttribute("name", stringModel.getKey());
                    elementString.setTextContent(excelMap.get(stringModel.getKey()));
//                    Log.d("liao", "key = " + stringModel.getKey() + " = " + excelMap.get(stringModel.getKey()));
//                    elementString.setAttribute(stringModel.getKey(), excelMap.get(stringModel.getKey()));
                    root.appendChild(elementString);
                } catch (Exception e) {

                }
            }
            XmlUtils.outputXml(doc, xmlfile.getAbsolutePath());
            System.out.println("保存文件到string");
        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
        }
    }

}
