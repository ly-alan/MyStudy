package com.roger.javamodule.test_gc;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import sun.rmi.runtime.Log;

public class GcRootThread {
    private int _10MB = 10 * 1024 * 1024;
    private byte[] memory = new byte[8 * _10MB];

    public static void main(String[] args) {
        System.out.println("开始前:");
        printMemory();
        doThread();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
        System.out.println("延迟5s，完成GC");
        printMemory();
    }


    private static void doThread(){
        AsyncTask at = new AsyncTask(new GcRootThread());
        Thread thread = new Thread(at)
//        {
//            @Override
//            public void run() {
//                try {
//                    byte[] memory = new byte[8 * 10 * 1024 * 1024];
//                    System.out.println("memory = " + memory.length);
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (Exception e) {
//                }
//            }
//        }
        ;
        thread.start();
        System.gc();
        System.out.println("main方法执行完，完成GC");
        printMemory();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        at = null;
        System.gc();
        System.out.println("线程执行完毕，完成GC");
        printMemory();
    }

    /**
     * 打印出当前JVM剩余空间和总的空间大小
     */
    public static void printMemory() {
        System.out.print("free is " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + " M, ");
        System.out.println("total is " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + " M, ");
    }

    private static class AsyncTask implements Runnable {
        private GcRootThread gcRootThread;

        public AsyncTask(GcRootThread gcRootThread) {
            this.gcRootThread = gcRootThread;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
            }
        }
    }
}
