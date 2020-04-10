package com.roger.javamodule.test_jvm;

public class ClassInit {
    public static int value = 1;

    static {
        System.out.println("ClassInit static block");
    }

    {
        System.out.println("ClassInit no-static block");
    }

    public ClassInit() {
        System.out.println("ClassInit constructor");
    }
}
