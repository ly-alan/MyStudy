package com.roger.main.view;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import com.roger.main.R;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * @Author Roger
 * @Date 2023/12/6 13:33
 * @Description 日期选择
 */
public class DatePickerDialog extends Dialog {

    NumberPicker mPickerYear, mPickerMonth, mPickerDay;

    public DatePickerDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_data_picker);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        mPickerYear = findViewById(R.id.tv_picker_year);
        mPickerMonth = findViewById(R.id.tv_picker_month);
        mPickerDay = findViewById(R.id.tv_picker_day);

        mPickerYear.setMinValue(2023);
        mPickerYear.setMaxValue(2023);

        mPickerMonth.setMinValue(1);
        mPickerMonth.setMaxValue(12);

        mPickerDay.setMinValue(1);
        mPickerDay.setMaxValue(31);
    }
}
