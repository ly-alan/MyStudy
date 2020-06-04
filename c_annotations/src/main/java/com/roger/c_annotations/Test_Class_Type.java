package com.roger.c_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target(value = TYPE)
public @interface Test_Class_Type {
}
