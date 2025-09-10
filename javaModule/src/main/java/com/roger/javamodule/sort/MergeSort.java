package com.roger.javamodule.sort;

/**
 * 归并排序
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] testArr = SortUtils.createTestData(8);

        SortUtils.printLog("排序前", testArr);

        mergeSort(testArr);

        SortUtils.printLog("排序后", testArr);
    }


    private static void mergeSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        int[] temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, temp);
    }

    private static void mergeSort(int[] arr, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            //拆分2个数组
            mergeSort(arr, left, mid, temp);
            mergeSort(arr, mid + 1, right, temp);
            merge(arr, left, right, mid, temp);
        }
    }

    private static void merge(int[] arr, int left, int right, int mid, int[] temp) {
        int i = left, j = mid + 1;//左右两边数组的开始位置
        int k = 0;//合并数组填入数据的开始位置
        while (i <= mid && j <= right) {
            //把较小的数据依次存起来
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        while (i <= mid) {
            //左数组比右数组长，把剩下的数据依次填入
            temp[k++] = arr[i++];
        }
        while (j <= right) {
            //右数组比较长，把剩下的数据依次填入
            temp[k++] = arr[j++];
        }
        //temp此时是排好序的，直接覆盖arr的数据
        System.arraycopy(temp, 0, arr, left, k);
    }

}
