package com.roger.main.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @Author Roger
 * @Date 2023/11/29 20:58
 * @Description 軟鍵盤
 */
public class KeyBoardUtils {
    /**
     * 显示软键盘
     *
     * @param view 依附的View
     */
    public static void showKeyboard(View view) {
        if (view == null) return;
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            //view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }


    /**
     * 隐藏软键盘
     *
     * @param view 依附的View
     */
    public static void hideKeyboard(View view) {
        if (view == null) return;
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

}
