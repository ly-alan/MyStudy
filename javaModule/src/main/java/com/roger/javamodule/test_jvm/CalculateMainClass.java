package com.roger.javamodule.test_jvm;


public class CalculateMainClass {

    public static void main(String[] args) {
        function_1();
        function_2();
        function_3();
        function_4();
    }

    /**
     * j=0；
     */
    private static void function_1() {
        int j = 0;
        for (int i = 0; i < 10; i++) {
            j = (j++);
        }
        System.out.println("function_1");
        System.out.println(j);
    }

    private static void function_2() {
        int j = 0;
        for (int i = 0; i < 10; i++) {
            j++;
        }
        System.out.println("function_2");
        System.out.println(j);
    }

    private static void function_3() {
        int j = 0;
        for (int i = 0; i < 10; i++) {
            j = ++j;
        }
        System.out.println("function_3");
        System.out.println(j);
    }

    private static void function_4() {
        int j = 0;
        for (int i = 0; i < 10; i++) {
            j = (++j);
        }
        System.out.println("function_4");
        System.out.println(j);
    }
}
