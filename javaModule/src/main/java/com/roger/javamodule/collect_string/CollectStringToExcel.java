package com.roger.javamodule.collect_string;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

/**
 * 查找string.xml中的翻译并整理成excel文档
 * 将ROOT_FILE_PATH项目下所有的strings文件中字符串导出来保存到SAVE_FILE_PATH下
 */
public class CollectStringToExcel {

    //    private static String ROOT_FILE_PATH = "D:\\Work\\cv-media-droid\\m_netdisk\\src\\mfc\\res\\values\\strings.xml";
//    private static String ROOT_FILE_PATH = "D:\\Work\\cv-media-droid\\c_ui\\src\\main\\res\\values-es\\strings.xml";
//    private static String ROOT_FILE_PATH = "D:\\Work\\cv-media-droid\\m_home\\src\\mfc\\res\\values\\strings.xml";
//    private static String ROOT_FILE_PATH = "D:\\Work\\cv-media-droid";
    private static String ROOT_FILE_PATH = "D:\\Work\\cv-media-droid";
//    private static String ROOT_FILE_PATH = "D:\\work\\live_local\\tve3-android\\tve3-app";
//    private static String ROOT_FILE_PATH = "D:\\Work\\cv-media-mobile-v2\\m_account\\src\\main";

    //    private static String SAVE_FILE_PATH = "C:\\Users\\admin\\Desktop\\Live资源文件\\Live资源文件调整\\Live资源文件调整\\step2\\live_stb";
    private static String SAVE_FILE_PATH = "C:\\Users\\admin\\Desktop\\test\\test";

    public static void main(String[] args) {
        System.out.println("start traverse");
//        ROOT_FILE_PATH = selectFolderPath();
//        if (ROOT_FILE_PATH == null || ROOT_FILE_PATH.length() == 0) {
//            System.out.println("Select folder is null");
//            return;
//        }
//        SAVE_FILE_PATH = ROOT_FILE_PATH + File.separator + "dist";
        traverseXmlForString(new File(ROOT_FILE_PATH));
    }

    /**
     * 选择文件夹路径
     *
     * @return
     */
    private static String selectFolderPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f != null) {
            return f.getPath();
        }
        return "";
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
                    //建一个和key的长度对应的翻译文本列
                    if (data.size() == 0) {
                        data.add(new ArrayList<Object>());
                    } else {
                        List<Object> emptyList = new ArrayList<>();
                        for (int i = 0; i < data.get(0).size(); i++) {
                            emptyList.add("");
                        }
                        data.add(emptyList);
                    }
                    for (int i = 0; i < stringModels.size(); i++) {
                        if (!data.get(0).contains(stringModels.get(i).key)) {
                            //没有添加过key
                            data.get(0).add(stringModels.get(i).key);
                            //这个key的几种语言先弄成空字符串
                            for (int j = 1; j < data.size(); j++) {
                                data.get(j).add("");
                            }
                        }
//                        if (stringModels.get(i).key.contains("account_yes_i_know")) {
//                            System.out.println(stringModels.get(i));
//                        }
                        //对应key所在的位置
                        int index = data.get(0).indexOf(stringModels.get(i).key);
                        if (index >= 0) {
                            data.get(title.size() - 1).set(index, stringModels.get(i).value);
                        }
//                        if (data.get(0).get(i).equals(stringModels.get(i).key)) {
//                            //key行数是对的，填入value
//                            data.get(title.size() - 1).add(stringModels.get(i).value);
//                        } else {
//                            //key对不上，两个文件值顺序有区别，找到对应的key的值填入
//                            boolean hasTranslate = false;
//                            for (StringModel model : stringModels) {
//                                if (data.get(0).get(i).equals(model.key)) {
////                                    System.out.println("i = " + i + " : data : " + data.get(title.size() - 1).size() + " : " + data.get(0).get(i) + " : " + model.value);
//                                    data.get(title.size() - 1).add(model.value);
//                                    hasTranslate = true;
//                                    break;
//                                }
//                            }
//                            //未找到对应的翻译
//                            if (!hasTranslate) {
//                                System.out.println("缺失翻译: " + title.get(title.size() - 1) + " : " + data.get(0).get(i) + ": ");
//                                data.get(title.size() - 1).add("");
//                            }
//                        }
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
