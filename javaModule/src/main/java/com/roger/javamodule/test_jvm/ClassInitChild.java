package com.roger.javamodule.test_jvm;

public class ClassInitChild extends ClassInit {
    static {
        System.out.println("Child static block");
    }

    {
        System.out.println("Child no-static block");
    }

    public ClassInitChild() {
        System.out.println("Child constructor");
    }
}
