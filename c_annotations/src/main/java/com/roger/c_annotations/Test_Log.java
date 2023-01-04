package com.roger.c_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author Roger
 * @Date 2022/12/22 17:13
 * @Description 切面编程可以参考https://www.jianshu.com/p/63a84ec8cf08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test_Log {
    String tag() default "" ;
}

//@Aspect
//public class Test_Log_Aspect{
//
//
//}
