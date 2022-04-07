package com.roger.tvmodule.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roger.tvmodule.R;


/**
 * @Author: Elegant Wang
 * @CreateDate: 2020/11/6 9:31
 * @Description:
 */
public class RegionLimitFocusWrapper extends FrameLayout {
    private static final String TAG = "RegionLimitFocusWrapper";

    private boolean mLimitFocusVertical;


    public RegionLimitFocusWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RegionLimitFocusWrapper(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PersistentFocusWrapper);
//        int orientation = ta.getInt(R.styleable.RegionLimitFocusWrapper_limitOrientation, 2);
//        ta.recycle();
        mLimitFocusVertical = true;
    }


    private boolean shouldLimitFromDirection(int direction) {
        return ((mLimitFocusVertical && (direction == FOCUS_UP || direction == FOCUS_DOWN)) ||
                (!mLimitFocusVertical && (direction == FOCUS_LEFT || direction == FOCUS_RIGHT)));
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (shouldLimitFromDirection(direction)) {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        } else {
            return super.focusSearch(focused, direction);
        }
    }
}
