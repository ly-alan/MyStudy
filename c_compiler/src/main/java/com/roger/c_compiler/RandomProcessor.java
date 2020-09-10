package com.roger.c_compiler;

import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.roger.c_annotations.RandomInt;
import com.roger.c_annotations.TestBindView;
import com.roger.c_annotations.Test_Class_Type;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import static java.util.Objects.requireNonNull;

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
        processRandomInt();
        processBindView(roundEnvironment);
        note("process -> " + roundEnvironment.toString());


        /****** butterknife的方法 *****/
//        Map<TypeElement, BindingSet> bindingMap = findAndParseTargets(roundEnvironment);


        //好像这个方法必须返回true才会生产文件)
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

//                String info1 = String.format(Locale.getDefault(),
//                        "\t\tactivity.%s = %d;\n", "number", new Random().nextInt(65535));
//                builder.append(info1);
            }

            builder.append("\t}\n");
            builder.append("}");

            writer.write(builder.toString());
            writer.flush();
            writer.close();

        } catch (Exception e) {

        }
    }


//    private Map<TypeElement, BindingSet> findAndParseTargets(RoundEnvironment env) {
//        Map<TypeElement, BindingSet.Builder> builderMap = new LinkedHashMap<>();
//        Set<TypeElement> erasedTargetNames = new LinkedHashSet<>();
//
//        // Process each @BindArray element.
//        for (Element element : env.getElementsAnnotatedWith(TestBindView.class)) {
//            if (!SuperficialValidation.validateElement(element)) continue;
//            try {
//                parseBindViews(element, builderMap, erasedTargetNames);
//            } catch (Exception e) {
//                //
//                note("parseTestBindView error");
//            }
//        }
//
//        Map<TypeElement, ClasspathBindingSet> classpathBindings =
//                findAllSupertypeBindings(builderMap, erasedTargetNames);
//
//        // Associate superclass binders with their subclass binders. This is a queue-based tree walk
//        // which starts at the roots (superclasses) and walks to the leafs (subclasses).
//        Deque<Map.Entry<TypeElement, BindingSet.Builder>> entries = new ArrayDeque<>(builderMap.entrySet());
//        Map<TypeElement, BindingSet> bindingMap = new LinkedHashMap<>();
//        while (!entries.isEmpty()) {
//            Map.Entry<TypeElement, BindingSet.Builder> entry = entries.removeFirst();
//
//            TypeElement type = entry.getKey();
//            BindingSet.Builder builder = entry.getValue();
//
//            TypeElement parentType = findParentType(type, erasedTargetNames, classpathBindings.keySet());
//            if (parentType == null) {
//                bindingMap.put(type, builder.build());
//            } else {
//                BindingInformationProvider parentBinding = bindingMap.get(parentType);
//                if (parentBinding == null) {
//                    parentBinding = classpathBindings.get(parentType);
//                }
//                if (parentBinding != null) {
//                    builder.setParent(parentBinding);
//                    bindingMap.put(type, builder.build());
//                } else {
//                    // Has a superclass binding but we haven't built it yet. Re-enqueue for later.
//                    entries.addLast(entry);
//                }
//            }
//        }
//
//        return builderMap;
//    }
//
//
//    private void parseBindViews(Element element, Map<TypeElement, BindingSet.Builder> builderMap,
//                                Set<TypeElement> erasedTargetNames) {
//        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
//
//        // Start by verifying common generated code restrictions.
//        boolean hasError = isInaccessibleViaGeneratedCode(BindViews.class, "fields", element)
//                || isBindingInWrongPackage(BindViews.class, element);
//
//        // Verify that the type is a List or an array.
//        TypeMirror elementType = element.asType();
//        String erasedType = doubleErasure(elementType);
//        TypeMirror viewType = null;
//        FieldCollectionViewBinding.Kind kind = null;
//        if (elementType.getKind() == TypeKind.ARRAY) {
//            ArrayType arrayType = (ArrayType) elementType;
//            viewType = arrayType.getComponentType();
//            kind = FieldCollectionViewBinding.Kind.ARRAY;
//        } else if (LIST_TYPE.equals(erasedType)) {
//            DeclaredType declaredType = (DeclaredType) elementType;
//            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
//            if (typeArguments.size() != 1) {
//                error(element, "@%s List must have a generic component. (%s.%s)",
//                        BindViews.class.getSimpleName(), enclosingElement.getQualifiedName(),
//                        element.getSimpleName());
//                hasError = true;
//            } else {
//                viewType = typeArguments.get(0);
//            }
//            kind = FieldCollectionViewBinding.Kind.LIST;
//        } else {
//            error(element, "@%s must be a List or array. (%s.%s)", BindViews.class.getSimpleName(),
//                    enclosingElement.getQualifiedName(), element.getSimpleName());
//            hasError = true;
//        }
//        if (viewType != null && viewType.getKind() == TypeKind.TYPEVAR) {
//            TypeVariable typeVariable = (TypeVariable) viewType;
//            viewType = typeVariable.getUpperBound();
//        }
//
//        // Verify that the target type extends from View.
//        if (viewType != null && !isSubtypeOfType(viewType, VIEW_TYPE) && !isInterface(viewType)) {
//            if (viewType.getKind() == TypeKind.ERROR) {
//                note(element, "@%s List or array with unresolved type (%s) "
//                                + "must elsewhere be generated as a View or interface. (%s.%s)",
//                        BindViews.class.getSimpleName(), viewType, enclosingElement.getQualifiedName(),
//                        element.getSimpleName());
//            } else {
//                error(element, "@%s List or array type must extend from View or be an interface. (%s.%s)",
//                        BindViews.class.getSimpleName(), enclosingElement.getQualifiedName(),
//                        element.getSimpleName());
//                hasError = true;
//            }
//        }
//
//        // Assemble information on the field.
//        String name = element.getSimpleName().toString();
//        int[] ids = element.getAnnotation(BindViews.class).value();
//        if (ids.length == 0) {
//            error(element, "@%s must specify at least one ID. (%s.%s)", BindViews.class.getSimpleName(),
//                    enclosingElement.getQualifiedName(), element.getSimpleName());
//            hasError = true;
//        }
//
//        Integer duplicateId = findDuplicate(ids);
//        if (duplicateId != null) {
//            error(element, "@%s annotation contains duplicate ID %d. (%s.%s)",
//                    BindViews.class.getSimpleName(), duplicateId, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
//            hasError = true;
//        }
//
//        if (hasError) {
//            return;
//        }
//        TypeName type = TypeName.get(requireNonNull(viewType));
//        boolean required = isFieldRequired(element);
//
//        BindingSet.Builder builder = getOrCreateBindingBuilder(builderMap, enclosingElement);
//        builder.addFieldCollection(new FieldCollectionViewBinding(name, type, requireNonNull(kind),
//                new ArrayList<>(elementToIds(element, BindViews.class, ids).values()), required));
//
//        erasedTargetNames.add(enclosingElement);
//    }

}
