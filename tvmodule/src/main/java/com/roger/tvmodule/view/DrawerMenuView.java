package com.roger.tvmodule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roger.tvmodule.R;

/**
 * @Author Roger
 * @Date 2021/11/12 14:56
 * @Description
 */
public class DrawerMenuView extends FrameLayout {

    View contentView;
    View slidingView;

    public DrawerMenuView(@NonNull Context context) {
        this(context, null);
    }

    public DrawerMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_sliding_view, this, true);
        setFocusable(true);
        contentView = findViewById(R.id.c_ui_content);
        slidingView = findViewById(R.id.c_ui_sliding);

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (slidingView == null) {
                    return;
                }
                if (hasFocus) {
                    slidingView.setVisibility(VISIBLE);
                }
            }
        });
    }


}
