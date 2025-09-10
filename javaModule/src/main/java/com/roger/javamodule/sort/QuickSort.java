package com.roger.javamodule.sort;

/**
 * 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] testArr = SortUtils.createTestData(5);

        SortUtils.printLog("排序前", testArr);

        quickSort(testArr, 0, testArr.length - 1);

        SortUtils.printLog("排序后", testArr);
    }


    /**
     * 快速排序
     *
     * @param arr
     * @param left
     * @param right
     */
    private static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            //取两头的为基准
//            int index = partitionLomuto(arr, left, right);
//            quickSort(arr, left, index - 1);
//            quickSort(arr, index + 1, right);
            //取中间为基准
            int index = partitionHoare(arr, left, right);
            quickSort(arr, left, index);
            quickSort(arr, index + 1, right);
        }
    }

    /**
     * lomuto快排
     *
     * @param testArr
     * @param left
     * @param right
     * @return
     */
    private static int partitionLomuto(int[] testArr, int left, int right) {
        int pivot = testArr[right];//以最后一个数为基准
        int i = left - 1;// i是“≤pivot”部分的右边界
        for (int j = left; j < right; j++) {
            //从最左边开始比较
            if (testArr[j] <= pivot) {
                //当前位置的值大于基准值，把较小的值换到前面
                i++;//交换数据的位置+1
                SortUtils.swap(testArr, i, j);
            }
        }
        SortUtils.swap(testArr, i + 1, right);// 将pivot放到正确位置
        return i + 1;
    }

    /**
     * Hoare处理，以中间数据为基准
     *
     * @param testArr
     * @param left
     * @param right
     * @return
     */
    private static int partitionHoare(int[] testArr, int left, int right) {
        int pivot = testArr[(left + right) / 2];//以中间数为基准
        int i = left - 1;
        int j = right + 1;
        while (true) {
            do {
                i++;
            } while (testArr[i] < pivot);
            do {
                j--;
            } while (testArr[j] > pivot);
            if (i >= j) {
                return j;
            }
            SortUtils.swap(testArr, i, j);
        }
    }


}
