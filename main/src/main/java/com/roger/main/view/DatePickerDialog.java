package com.roger.main.view;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import com.roger.main.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * @Author Roger
 * @Date 2023/12/6 13:33
 * @Description 日期选择
 */
public class DatePickerDialog extends Dialog {

    NumberPickerView mPickerYear, mPickerMonth, mPickerDay, mPickerHour, mPickerMin;

    public DatePickerDialog(Context context) {
        super(context, R.style.DialogTheme);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_data_picker);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(params);
        mPickerYear = findViewById(R.id.tv_picker_year);
        mPickerMonth = findViewById(R.id.tv_picker_month);
        mPickerDay = findViewById(R.id.tv_picker_day);
        mPickerHour = findViewById(R.id.tv_picker_hour);
        mPickerMin = findViewById(R.id.tv_picker_min);


//        mPickerYear.setMinValue(2023);
//        mPickerYear.setMaxValue(2023);

        List<String> listMonth = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            listMonth.add(String.valueOf(i));
        }
        mPickerMonth.setDisplayedValues(listMonth.toArray(new String[0]));
        mPickerMonth.setMinValue(1);
        mPickerMonth.setMaxValue(12);

        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            list.add(String.valueOf(i));
        }
        mPickerDay.setDisplayedValues(list.toArray(new String[0]));
        mPickerDay.setMinValue(1);
        mPickerDay.setMaxValue(31);

        mPickerMonth.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVa) {
                Log.d("liao","selecte " + oldVal + "  -- " + newVa);
            }
        });
    }
}
