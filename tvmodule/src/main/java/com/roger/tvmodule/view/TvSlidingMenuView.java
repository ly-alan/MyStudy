package com.roger.tvmodule.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roger.tvmodule.R;

/**
 * @Author Roger
 * @Date 2021/10/19 11:00
 * @Description
 */
public class TvSlidingMenuView extends FrameLayout {

    View contentView;
    View slidingView;

    public TvSlidingMenuView(@NonNull Context context) {
        super(context);
        init();
    }

    public TvSlidingMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvSlidingMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void into(Activity activity) {
        if (activity == null) {
            return;
        }
        FrameLayout frameLayout = activity.getWindow().getDecorView().findViewById(android.R.id.content);
//        slidingView = LayoutInflater.from(getContext()).inflate(R.layout.layout_menu_sliding, null);
//        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
////        layoutParams.height = LayoutParams.MATCH_PARENT;
////        layoutParams.width = LayoutParams.WRAP_CONTENT;
////        layoutParams.gravity = Gravity.LEFT;
////        frameLayout.addView(slidingView, layoutParams);
//        slidingView.setVisibility(GONE);
        slidingView = new DrawerView(getContext());
        frameLayout.addView(slidingView);

    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_sliding_view, this, true);
        setFocusable(true);
        contentView = findViewById(R.id.c_ui_content);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
