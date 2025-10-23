package com.roger.javamodule.collect_string;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @Description excel替换android的strings文件
 * 如果修改了字符串，太多了手动替换麻烦。
 * 直接遍历所有values/strings文件，修改string中的值
 */
public class ExcelReplaceStrings {
    //android根目录地址
    private static String ROOT_PROJECT_PATH = "D:\\work-new\\cv-media-stb\\cv-media-droid\\m_ota\\src";

    //想要读取的excel文件地址
    private static String EXCEL_FILE_PATH = "C:\\Users\\admin\\Desktop\\string\\test_ota.xls";
    //excel保存key在哪一列，需要大写,常量，用来计算value在那一列
    private static char EXCEL_COLUMN_KEY = 'A';
    //默认语言
    private static final String DEF_LANGUAGE = "en";
    //excel保存value和语言的对应关系<语言,列名>
    private static final Map<String, Character> COLUMN_VALUE_MAP = new HashMap<String, Character>() {{
        put("en", 'C');
        put("pt", 'E');
    }};
    //strings文件的key有多少个，从起始地址向下扫描多少行（不用格子为空判断，防止中间有很多空行）
    private static int MAX_STRINGS_NUM = 5000;


    //遍历的excel中的键值对<语言,Map<key,value>>
    private static Map<String, Map<String, String>> excelMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("start replace");
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
    private static void traverseExcel(File excelFile, Map<String, Map<String, String>> excelMap) {
        try {
            //读取所有的string
            List<List<Object>> result = ExcelUtils.readExcelFirstSheet(excelFile);
            // 初始化每种语言的map
            for (String language : COLUMN_VALUE_MAP.keySet()) {
                excelMap.put(language, new HashMap<>());
            }
            //按key转换为map,从指定行开始
            for (int i = 0; i < Math.min(result.size(), MAX_STRINGS_NUM); i++) {
                List<Object> rowList = result.get(i);//指定行的单元格
                if (rowList == null || rowList.isEmpty()) {
                    //空行
                    continue;
                }
                // 获取key（EXCEL_COLUMN_KEY列,一般EXCEL_COLUMN_KEY=A，key在第一列）
                int keyColumnIndex = EXCEL_COLUMN_KEY - 'A';
                if (keyColumnIndex >= rowList.size()) {
                    continue;
                }
                Object keyObj = rowList.get(keyColumnIndex);
                if (keyObj == null) {
                    //key是空的
                    continue;
                }
                String key = String.valueOf(keyObj).trim();
                if (key.isEmpty()) {
                    //key是空的
                    continue;
                }
                // 为每种语言获取对应的value
                for (Map.Entry<String, Character> entry : COLUMN_VALUE_MAP.entrySet()) {
                    String language = entry.getKey();
                    char valueColumn = entry.getValue();
                    //当前语言在excel的列
                    int valueColumnIndex = valueColumn - 'A';
                    if (valueColumnIndex < rowList.size()) {
                        Object valueObj = rowList.get(valueColumnIndex);
                        if (valueObj != null) {
                            String value = String.valueOf(valueObj);//value的空格保留
//                            String value = String.valueOf(valueObj).trim();
                            // 将键值对放入对应语言的map中，空的也保存
                            excelMap.get(language).put(key, value);
                            if (value.isEmpty()) {
                                System.out.println("语言=" + language + ";key= " + key + "；的值为空字符串");
                            }
                        } else {
                            System.out.println("语言=" + language + ";key= " + key + "；的值是空");
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 遍历文件夹，查找string.xml并替换值
     *
     * @param projectFile 项目文件
     */
    private static void traverseProjectToStringXml(File projectFile) {
        if (projectFile == null) {
            System.out.println("file is null : ");
            return;
        }
        if (!projectFile.exists()) {
            System.out.println("file is not exit : " + projectFile.getAbsolutePath());
            return;
        }
        if (projectFile.isDirectory()) {
            File[] files = projectFile.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                traverseProjectToStringXml(file);
            }
        } else {
            //文件如果是res\\value文件夹，或者res\\value-pt这样的，读取里面的string是文件，然后根据value后面带的语言去从excelMap里找到语言可key替换string.xml里的值
            if (projectFile.getName().equals("strings.xml")) {
                // 检测语言类型
                String language = detectLanguageFromPath(projectFile);
                if (language != null && COLUMN_VALUE_MAP.containsKey(language)) {
                    // 处理strings.xml文件
                    processStringsXmlFile(projectFile, language);
                }
            }
        }
    }

    /**
     * 从文件路径检测语言类型
     */
    private static String detectLanguageFromPath(File stringsFile) {
        File parentDir = stringsFile.getParentFile();
        if (parentDir == null) {
            return null;
        }

        String parentName = parentDir.getName();

        // 匹配 values 或 values-语言 格式
        if (parentName.equals("values")) {
            return DEF_LANGUAGE; // 默认语言
        } else if (parentName.startsWith("values-")) {
            // 提取语言代码，如 values-pt -> pt
            String langCode = parentName.substring(7); // 去掉 "values-"

            // 处理地区变体，如 zh-rCN -> zh
            if (langCode.contains("-r")) {
                langCode = langCode.substring(0, langCode.indexOf("-r"));
            }
            return langCode;
        }

        return null;
    }


    /**
     * 使用DOM方式处理strings.xml文件（推荐）
     */
    private static void processStringsXmlFile(File stringsFile, String language) {
        try {
            System.out.println("处理文件: " + stringsFile.getAbsolutePath() + " [语言: " + language + "]");

            // 创建DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stringsFile);
            doc.getDocumentElement().normalize();

            // 获取所有string节点
            NodeList nodeList = doc.getElementsByTagName("string");
            boolean hasChanges = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String key = element.getAttribute("name");

                    if (key != null && !key.isEmpty()) {
                        // 从excelMap中查找新值
                        String newValue = findValueInExcelMap(key, language);
                        if (newValue != null) {
                            String oldValue = element.getTextContent();
                            if (!newValue.equals(oldValue)) {
                                element.setTextContent(newValue);
                                hasChanges = true;
                                System.out.println("更新: " + key + " [" + language + "] -> " + newValue);
                            }
                        }
                    }
                }
            }

            // 如果有更改，保存文件
            if (hasChanges) {
                XmlUtils.outputXml(doc, stringsFile.getAbsolutePath());
                System.out.println("文件已更新: " + stringsFile.getAbsolutePath());
            } else {
                System.out.println("无更改: " + stringsFile.getAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("处理文件失败: " + stringsFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    /**
     * 从excelMap中查找对应语言和key的值
     * 根据你的excelMap结构选择合适的方法
     */
    private static String findValueInExcelMap(String key, String language) {
        // 如果你的excelMap结构是 <语言, <键, 值>>
        if (excelMap.containsKey(language)) {
            Map<String, String> languageMap = excelMap.get(language);
            return languageMap.get(key);
        }

        // 如果你的excelMap结构是 <键, <语言, 值>>
        if (excelMap.containsKey(key)) {
            Map<String, String> keyMap = excelMap.get(key);
            return keyMap.get(language);
        }

        return null;
    }

}
