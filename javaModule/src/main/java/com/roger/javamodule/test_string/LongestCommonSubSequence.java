package com.roger.javamodule.test_string;

/**
 * Create by Roger on 2019/8/19
 * 最长子序列
 */
public class LongestCommonSubSequence {

    public static void main(String[] args) {
//        LongestCommonSubSequence.getLCS("495","94984");
//        LongestCommonSubSequence.getLCS("132535365","123456789");
        LongestCommonSubSequence.getLCS("ABCBDAB", "BDCABA");
    }


    /**
     * getLCS TODO :得到最长子序列长度，并输出所有最长子序列
     *
     * @param x 序列x
     * @param y 序列y
     * @return 最长子序列长度
     * @author zhiman
     * @date 2018/04/17 下午9:24:18
     */
    public static int getLCS(String x, String y) {
        int xlen = x.length();
        int ylen = y.length();
        // 此处的棋盘长度要比字符串长度多加1，需要多存储一行0和一列0
        int[][] commonSublen = new int[xlen + 1][ylen + 1];
        // 1代表上 2 代表向左上 3代表向左 4代表上或者左
        int[][] direction = new int[xlen + 1][ylen + 1];
        // 将整个数组commonSublen填充值
        for (int i = 1; i <= xlen; i++) {
            char xi = x.charAt(i - 1);
            for (int j = 1; j <= ylen; j++) {
                char yj = y.charAt(j - 1);
                if (xi == yj) {
                    commonSublen[i][j] = commonSublen[i - 1][j - 1] + 1;
                    // 2 代表向左上
                    direction[i][j] = 2;
                } else if (commonSublen[i - 1][j] > commonSublen[i][j - 1]) {
                    commonSublen[i][j] = commonSublen[i - 1][j];
                    // 1代表上
                    direction[i][j] = 1;
                } else if (commonSublen[i - 1][j] < commonSublen[i][j - 1]) {
                    commonSublen[i][j] = commonSublen[i][j - 1];
                    // 3代表左
                    direction[i][j] = 3;
                } else {
                    // 如果commonSublen[i - 1][j] == commonSublen[i][j - 1]
                    // 向上或者向左不影响结果
                    // 4代表上 或者 左
                    commonSublen[i][j] = commonSublen[i - 1][j];
                    // 1代表上
                    direction[i][j] = 4;
                }
            }
        }
        int maxSublen = commonSublen[xlen][ylen];
        String lcs = "";
        printSublen(commonSublen, x.toCharArray(), y.toCharArray());
        System.out.println("\n-----------------\n");
        printSublen(direction, x.toCharArray(), y.toCharArray());
        System.out.println("\n-----------------\n");
        printAllLcs(direction, x, lcs, maxSublen, xlen, ylen);
        return maxSublen;
    }

    /**
     * printAllLcs TODO :打印所有的最长公共子序列（不止一个）
     *
     * @param direction 方向矩阵
     * @param x
     * @param lcs
     * @param maxSublen
     */
    private static void printAllLcs(int[][] direction, String x, String lcs, int maxSublen, int i, int j) {
        if (i == 0 || j == 0) {
            StringBuilder sb = new StringBuilder(lcs);
            lcs = sb.reverse().toString();
            if (lcs.length() == maxSublen) {
                System.out.println(lcs);
            }
            return;
        }
        int dir = direction[i][j];
        switch (dir) {
            case 1:// 表示向上
                printAllLcs(direction, x, lcs, maxSublen, i - 1, j);
                break;
            case 2:// 代表向左上
                lcs += x.charAt(i - 1);
                printAllLcs(direction, x, lcs, maxSublen, i - 1, j - 1);
                break;
            case 3:// 3代表向左
                printAllLcs(direction, x, lcs, maxSublen, i, j - 1);
                break;
            case 4:// 表示向上 或 代表向左
                // 表示向上
                printAllLcs(direction, x, lcs, maxSublen, i - 1, j);
                // 代表向左
                printAllLcs(direction, x, lcs, maxSublen, i, j - 1);
                break;
        }
    }

    /**
     * printSublen TODO :打印commonSublen矩阵
     */
    private static void printSublen(int[][] commonSublen) {
        for (int i = 0; i < commonSublen.length; i++) {
            for (int j = 0; j < commonSublen[0].length; j++) {
                System.out.print(commonSublen[i][j] + "\t");
            }
            System.out.println("\n");
        }
    }

    private static void printSublen(int[][] commonSublen, char[] x, char[] y) {
        for (int i = 0; i < commonSublen.length; i++) {
            if (i == 0) {
                System.out.print(" " + "\t");
                System.out.print(" " + "\t");
                for (int k = 0; k < y.length; k++) {
                    System.out.print(y[k] + "\t");
                }
                System.out.println("\n");
            }
            if (i == 0) {
                System.out.print(" " + "\t");
            }
            for (int j = 0; j < commonSublen[0].length; j++) {
                if (i > 0 && j == 0) {
                    System.out.print(x[i - 1] + "\t");
                }
                System.out.print(commonSublen[i][j] + "\t");
            }
            System.out.println("\n");
        }
    }
}
