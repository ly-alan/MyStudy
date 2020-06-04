package com.roger.c_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface TestBindView {
    //用于测试BindView ，代替findViewById
    int value();
}
