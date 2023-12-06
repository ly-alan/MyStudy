package com.roger.main.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.roger.main.R;

import java.lang.reflect.Field;

/**
 * @Author Roger
 * @Date 2023/12/6 15:26
 * @Description
 */
public class MyNumberPicker extends NumberPicker {
    public MyNumberPicker(Context context) {
        super(context);
        init();
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVa) {
                Log.d("liao", "old : new =  = " + oldVal + newVa);
            }
        });
        setNumberPickerDividerColor(this);
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(numberPicker, new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 设置picker之间的间距
     */
    private void setPickerMargin(NumberPicker picker) {
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) picker.getLayoutParams();
        p.setMargins(0, 0, 0, 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            p.setMarginStart(0);
            p.setMarginEnd(0);
        }
    }

//    /**
//     * 设置picker分割线的颜色
//     */
//    private void setDividerColor(NumberPicker picker) {
//        Field field = NumberPicker.class.getDeclaredField("mSelectionDivider");
//        if (field != null) {
//            field.setAccessible(true);
//            field.set(picker, new ColorDrawable(Color.RED));
//        }
//    }

//    /**
//     * 设置picker分割线的宽度
//     */
//    private void setNumberPickerDivider(NumberPicker picker) {
//        Field[] fields = NumberPicker.class.getDeclaredFields();
//        for (Field f : fields) {
//            if (f.getName().equals("mSelectionDividerHeight")) {
//                f.setAccessible(true);
//                f.set(picker, 1);
//                break;
//            }
//        }
//    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            //设置文字的颜色和大小
            ((EditText) view).setTextColor(getResources().getColor(R.color.black));
            ((EditText) view).setTextSize(16);
        }
    }
}
