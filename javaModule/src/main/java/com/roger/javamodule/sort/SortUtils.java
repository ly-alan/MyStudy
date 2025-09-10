package com.roger.javamodule.sort;

import java.util.Arrays;

public class SortUtils {
    /**
     * 交互i，j两个角标的数据
     *
     * @param arr
     * @param i
     * @param j
     */
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    /**
     * 创建测试数据
     *
     * @param length 数组长度
     * @return
     */
    public static int[] createTestData(int length) {
        int[] list = new int[length];
        for (int i = 0; i < length; i++) {
            list[i] = (int) (Math.random() * length);
        }
        return list;
    }

    public static void printLog(String tag, int[] list) {
        System.out.println(tag + " 数组为：" + Arrays.toString(list));
    }
}
