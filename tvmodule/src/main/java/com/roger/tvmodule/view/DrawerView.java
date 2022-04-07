package com.roger.tvmodule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roger.tvmodule.R;

/**
 * @Author Roger
 * @Date 2021/11/10 14:26
 * @Description
 */
public class DrawerView extends RegionLimitFocusWrapper {

    View view;

    public DrawerView(@NonNull Context context) {
        this(context, null);
    }

    public DrawerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_sliding, this, true);
        setFocusable(true);
        setVisibility(GONE);

        view = findViewById(R.id.text);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            view.requestFocus();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalFocusChangeListener(mGlobalFocusChangeListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalFocusChangeListener(mGlobalFocusChangeListener);
    }

    ViewTreeObserver.OnGlobalFocusChangeListener mGlobalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            if (hasFocus()) {
                setVisibility(VISIBLE);
            } else {
                setVisibility(GONE);
            }
        }
    };
}
