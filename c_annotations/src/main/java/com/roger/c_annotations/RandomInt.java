package com.roger.c_annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Create by Roger on 2020/1/3
 * Retention注解：描述作用时间
 * <p>
 * SOURCE 注解将被编译器忽略掉，相当于注释，例如：Override注解
 * CLASS 注解将被编译器记录在class文件中，编译时生效的注解，运行时不会被虚拟机保留
 * RUNTIME 注解将被编译器记录在class文件中，运行时会被虚拟机保留，可以通过反射获取到
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * Target注解：描述注解的修饰在哪些元素
 * <p>
 * TYPE,类，接口（包括注解类型）或枚举的声明
 * FIELD,属性的声明
 * METHOD,方法的声明
 * PARAMETER,方法形式参数声明
 * CONSTRUCTOR,构造方法的声明
 * LOCAL_VARIABLE,局部变量声明
 * ANNOTATION_TYPE,注解类型声明
 * PACKAGE，包的声明
 */
@Retention(CLASS)
@Target(value = FIELD)
public @interface RandomInt {
    int minValue() default 0;

    int maxValue() default 65535;
}
