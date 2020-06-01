package com.roger.javamodule.test_jvm;


public class CalculateMainClass {


    /**
     * @param args
     */
    public static void main(String[] args) {
//        0: iconst_0
//        1: istore_1
//        2: iinc          1, 1
//        5: iconst_0
//        6: istore_2
//        7: iload_2
//        8: iinc          2, 1
//        11: istore_2
        int i = 0;
        i++;
        int j = 0;
        j = j++;
        System.out.println("i = " + j);

        function_1();
        function_2();
        function_3();
        function_4();
    }

    /**
     *
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
