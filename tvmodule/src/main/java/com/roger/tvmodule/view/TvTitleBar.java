package com.roger.tvmodule.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class TvTitleBar extends Toolbar {

    public TvTitleBar(Context context) {
        super(context);
        init();
    }

    public TvTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TvTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setFocusable(false);
        setEnabled(false);
    }

}
