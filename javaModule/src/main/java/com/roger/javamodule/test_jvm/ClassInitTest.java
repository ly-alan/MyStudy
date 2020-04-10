package com.roger.javamodule.test_jvm;

public class ClassInitTest {

    public static void main(String[] args) {
//        function_1();
//        function_2();
//        function_3();
        function_4();
    }

    /**
     * 输出 ：ClassInit static block
     * 构造方法和非静态方法都没有被调用
     * 类已经初始化，但是未实例化。
     */
    private static void function_1() {
        ClassInit.value = 2;
    }

    /**
     * 输出：ClassInit static block
     * ClassInit no-static block
     * ClassInit constructor
     */
    private static void function_2() {
        new ClassInit();
    }

    /**
     * 输出：ClassInit static block
     * Child类未被初始化，static方法未被执行。
     * 静态变量所在的父类已被成功初始化
     */
    private static void function_3() {
        ClassInitChild.value = 2;
    }

    /**
     * ClassInit static block
     * Child static block
     * ClassInit no-static block
     * ClassInit constructor
     * Child no-static block
     * Child constructor
     * ----------------
     * ClassInit no-static block
     * ClassInit constructor
     * 对象初始化顺序：静态变量/静态代码块 -> 普通代码块 -> 构造函数
     * 1. 父类静态变量和静态代码块；
     * 2. 子类静态变量和静态代码块；
     * 3. 父类普通成员变量和普通代码块；
     * 4. 父类的构造函数；
     * 5. 子类普通成员变量和普通代码块；
     * 6. 子类的构造函数。
     */
    private static void function_4() {
        ClassInit p = new ClassInitChild();
        System.out.println("----------------");
        p = new ClassInit();
    }

}
