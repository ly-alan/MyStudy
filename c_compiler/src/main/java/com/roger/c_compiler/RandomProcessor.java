package com.roger.c_compiler;

import com.google.auto.service.AutoService;
import com.roger.c_annotations.RandomInt;
import com.roger.c_annotations.TestBindView;
import com.roger.c_annotations.Test_Class_Type;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Create by Roger on 2020/1/3
 */
@AutoService(Processor.class)
public class RandomProcessor extends AbstractProcessor {

    private Messager messager;
    private Types typesUtil;
    private Elements elementsUtil;
    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnv.getMessager();
        typesUtil = processingEnv.getTypeUtils();
        elementsUtil = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    private void note(String msg) {
        //打印日志
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //返回所支持的java版本，一般返回当前所支持的最新java版本即可
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //你所需要处理的所有注解，该方法的返回值会被process()方法所接收
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(RandomInt.class.getCanonicalName());
        annotations.add(Test_Class_Type.class.getCanonicalName());
        annotations.add(TestBindView.class.getCanonicalName());
        note("liao getSupportedAnnotationTypes");
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //对注解的处理
//        processRandomInt();
        processBindView(roundEnvironment);
        System.out.println("process -> " + roundEnvironment.toString());
        //好像这个方法必须返回true才会生产文件
        return true;
    }

    private void processRandomInt() {
//        for (Map.Entry<String, List<AnnotatedRandomElement>> entry : annotatedElementMap.entrySet()) {
//            MethodSpec constructor = createConstructor(entry.getValue());
//            TypeSpec binder = createClass(getClassName(entry.getKey()), constructor);
//            JavaFile javaFile = JavaFile.builder(getPackage(entry.getKey()), binder).build();
//            javaFile.writeTo(filer);
//        }
    }

    private void processBindView(RoundEnvironment roundEnvironment) {
        //获取用TestBindView描述的对象
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(TestBindView.class);
        //所以需要将所有 Element 按照(包名+类名) 分类，在对应的包名目录下新建文件
        HashMap<String, List<Element>> packageElementMap = new HashMap<>();
        for (Element element : elements) {
            //获取包名+类名
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String enclosingName = enclosingElement.getQualifiedName().toString();
            //enclosingName = xx.xx.xx.Activity(包括包名和类名)
            note("enclosingName = " + enclosingName);

            //根据包名类名对所有注解进行分类
            List<Element> elementList = packageElementMap.get(enclosingName);
            if (elementList == null) {
                elementList = new ArrayList<>();
                packageElementMap.put(enclosingName, elementList);
            }
            elementList.add(element);
        }
        //根据包类名生成对应的文件
        for (Map.Entry<String, List<Element>> entry : packageElementMap.entrySet()) {
            //根据Activity中带有 TestBindView注解的字段创建 java 文件 java文件保存在 app/build/generated/source/apt/debug或release 下
            createBindViewFile(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 创建 bindView的文件
     *
     * @param key   包类名
     * @param value 包含注解的对象列表
     */
    private void createBindViewFile(String key, List<Element> value) {
        note(key);
        if (value.size() == 0) {
            return;
        }
        try {
            //创建文件
            JavaFileObject jfo = filer.createSourceFile(key + "_ViewBinding", new Element[]{});
            Writer writer = jfo.openWriter();

            //获取包名
            TypeElement enclosingElement = (TypeElement) value.get(0).getEnclosingElement();
            Name pkName = elementsUtil.getPackageOf(enclosingElement).getQualifiedName();
            //获取类名
            String className = enclosingElement.getSimpleName().toString();
            //写入内容
            StringBuilder builder = new StringBuilder();
            //文件包名
            builder.append("package ").append(pkName).append(";\n\n");
            builder.append("//Auto generated by apt,do not modify!!\n\n");
            //文件类名
            builder.append("public class ").append(className).append("_ViewBinding").append(" { \n\n");//文件类名
            //构造函数
            builder.append("\tpublic ").append(className).append("_ViewBinding(").append(className).append(" activity){ \n");

            for (Element element : value) {
                VariableElement bindViewElement = (VariableElement) element;
                //参数名
                String bindViewFiledName = bindViewElement.getSimpleName().toString();
                String bindViewFiledClassType = bindViewElement.asType().toString();

                TestBindView bindView = element.getAnnotation(TestBindView.class);
                //设置的id
                int id = bindView.value();
                note(String.format(Locale.getDefault(), "%s %s = %d", bindViewFiledClassType, bindViewFiledName, id));

                String info = String.format(Locale.getDefault(),
                        "\t\tactivity.%s = activity.findViewById(%d);\n", bindViewFiledName, id);
                builder.append(info);
            }

            builder.append("\t}\n");
            builder.append("}");

            writer.write(builder.toString());
            writer.flush();
            writer.close();

        } catch (Exception e) {

        }
    }
}
