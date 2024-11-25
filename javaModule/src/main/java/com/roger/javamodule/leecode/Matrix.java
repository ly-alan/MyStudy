package com.roger.javamodule.leecode;

/**
 * 数组操作
 */
public class Matrix {


    /**
     * 给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。
     * <p>
     * 你必须在 原地 旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要 使用另一个矩阵来旋转图像。
     */

    /**
     * 替换对应规律为
     * <p>
     * matrix_new[col][n−row−1]=matrix_old[row][col]
     * (matrix_new的row位置的值为matrix_old的col，matrix_new的col位置的值为matrix_old的n−row−1)
     * 其它位置都可以如此替换，比如
     * matrix_old[col][n−row−1]经过代换后为matrix_new[n−row−1][matrix.length - col - 1]
     *
     * @param matrix
     */
    public void rotate2(int[][] matrix) {
        for (int row = 0; row < matrix.length / 2; row++) {
            for (int col = 0; col < (matrix.length + 1) / 2; col++) {
                int temp = matrix[row][col];
                matrix[row][col] = matrix[matrix.length - col - 1][row];
                matrix[matrix.length - col - 1][row] = matrix[matrix.length - row - 1][matrix.length - col - 1];
                matrix[matrix.length - row - 1][matrix.length - col - 1] = matrix[col][matrix.length - row - 1];
                matrix[col][matrix.length - row - 1] = temp;
            }
        }
    }

    /**
     * 先水平翻转，再对角线翻转
     *
     * @param matrix
     */
    public void rotate3(int[][] matrix) {
        int n = matrix.length;
        // 水平翻转
        for (int i = 0; i < n / 2; ++i) {
            for (int j = 0; j < n; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - i - 1][j];
                matrix[n - i - 1][j] = temp;
            }
        }
        // 主对角线翻转
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }



    /**
     * 编写一种算法，若M × N矩阵中某个元素为0，则将其所在的行与列清零。
     * @param matrix
     */
    public void setZeroes(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return;
        }
        boolean[] row = new boolean[matrix.length];
        boolean[] col = new boolean[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = true;
                    col[j] = true;
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (row[i] || col[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }


}
