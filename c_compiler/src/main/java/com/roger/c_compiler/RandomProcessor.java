package com.roger.c_compiler;

import com.google.auto.service.AutoService;
import com.roger.c_annotations.RandomInt;
import com.roger.c_annotations.RandomString;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

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
        annotations.add(RandomString.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }
}
