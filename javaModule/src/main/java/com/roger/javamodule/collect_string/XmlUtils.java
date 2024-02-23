package com.roger.javamodule.collect_string;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @Author Roger
 * @Date 2021/7/21 15:51
 * @Description
 */
public class XmlUtils {


    public static String XML = ".xml";

    /**
     * 读取
     *
     * @param xmlFile
     * @return
     */
    public static List<StringModel> getStringMapForXml(File xmlFile) {
        List<StringModel> stringModels = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList nl = doc.getElementsByTagName("string");
            for (int i = 0; i < nl.getLength(); i++) {
                StringModel stringModel = new StringModel();
                stringModel.setKey(((Element) nl.item(i)).getAttribute("name"));
                try {
                    stringModel.setValue(((Element) nl.item(i)).getFirstChild().getNodeValue());
                } catch (Exception e) {
                    stringModel.setValue("");
                }
                stringModel.setFileName(xmlFile.getName());
                stringModel.setParentPath(xmlFile.getParentFile().getParentFile().getAbsolutePath());
                stringModel.setParentName(xmlFile.getParentFile().getName());
                stringModel.setLanguage(Utils.getLanguageForFileName(stringModel.parentName));
                stringModels.add(stringModel);
            }

            Collections.sort(stringModels, new Comparator<StringModel>() {
                @Override
                public int compare(StringModel o1, StringModel o2) {
                    return o1.key.compareTo(o2.key);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("get size is : " + stringModels.size() + " : form file : " + xmlFile.getAbsolutePath());
        return stringModels;
    }


    public static boolean isXmlFile(String fileName) {
        if (fileName.endsWith(XML)) {
            return true;
        }
        return false;
    }


//    /**
//     * 生成xml方法
//     */
//    public static void createStringXml(String key, String value) {
//        Document doc = null;
//        Element root = null;
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            doc = builder.newDocument();
//            root = doc.createElement("resource");
//            doc.appendChild(root);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        int len = list.size();
//        Element element;
//        for (int i = 0; i < len; i++) {
//            Person person = list.get(i);
//            element = doc.createElement("person" + (i + 1));
//            element.setAttribute("age", "" + person.getAge());
//            element.setAttribute("name", person.getName());
//            root.appendChild(element);
//        }
//    }


    /**
     * 将XML文件输出到指定的路径
     *
     * @param doc
     * @param fileName
     * @throws Exception
     */
    public static void outputXml(Document doc, String fileName) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");//设置文档的换行与缩进
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        StreamResult result = new StreamResult(pw);
        transformer.transform(source, result);
        System.out.println("生成XML文件成功!");
    }

}

