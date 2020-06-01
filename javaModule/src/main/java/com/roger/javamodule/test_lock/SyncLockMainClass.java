package com.roger.javamodule.test_lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SyncLockMainClass {


    public static void main(String[] args) {
        //<10000
        normalCount();
        //<10000
        volatileCount();
        //=10000
        atomicNumCount();
    }

    private int normalNum = 0;

    private void normalIncrease() {
        new ReentrantLock();
        normalNum++;
    }

    private static void normalCount() {
        final SyncLockMainClass test = new SyncLockMainClass();
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        test.normalIncrease();
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("num = " + test.normalNum);
    }

    private volatile int volatileNum = 0;

    private void volatileIncrease() {
        volatileNum++;
    }

    private static void volatileCount() {
        final SyncLockMainClass test = new SyncLockMainClass();
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        test.volatileIncrease();
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("num = " + test.volatileNum);
    }


    private AtomicInteger atomicNum = new AtomicInteger(0);

    private void atomicIncrease() {
        atomicNum.incrementAndGet();
    }

    private static void atomicNumCount() {
        final SyncLockMainClass test = new SyncLockMainClass();
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        test.atomicIncrease();
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("num = " + test.atomicNum);
    }

}
