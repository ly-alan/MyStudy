package com.roger.javamodule.algorithm;

import com.roger.javamodule.util.Log;

/**
 * @Author Roger
 * @Date 2023/3/28 18:14
 * @Description
 */
public class HanoiTower {

    public static void main(String[] args) {
        HanoiTower hanoiTower = new HanoiTower();
        hanoiTower.start(9);
    }

    /**
     * @param n
     */
    private void start(int n) {
        doStep(n, "a", "c", "b");
    }

    /**
     * 汉罗塔有n阶 圆盘，分别编号1-n
     * 3根柱子：a，b，c，将所有的圆盘按从小到大顺序从a移动到c，即为胜利
     *
     * @param n
     * @param form 开始，
     * @param to   结束，
     * @param temp 过渡
     */
    void doStep(int n, String form, String to, String temp) {

        if (n == 1) {    //很明显,当n==1的时候,我们只需要直接将圆盘从起始柱子移至目标柱子即可.
            showStepLog(n, form, to);
        } else {
            doStep(n - 1, form, temp, to);   //递归处理,一开始的时候,先将n-1个盘子移至过渡柱上
            showStepLog(n, form, to);             //然后再将底下的大盘子直接移至目标柱子即可
            doStep(n - 1, temp, to, form);    //然后重复以上步骤,递归处理放在过渡柱上的n-1个盘子
        }
    }

    int index = 0;

    private void showStepLog(int n, String form, String to) {
        index++;
        Log.d("liao", "第 " + index + " 步" + "  move " + n + " 号 : " + form + " --> " + to);
    }

}
