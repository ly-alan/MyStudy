package com.roger.tvlibrary.recycle;

import android.content.Context;
import android.graphics.Rect;

import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link GridLayoutManager} extension which introduces workaround for focus finding bug when
 * navigating with dpad.
 *
 * @see <a href="http://stackoverflow.com/questions/31596801/recyclerview-focus-scrolling">http://stackoverflow.com/questions/31596801/recyclerview-focus-scrolling</a>
 */
public class TvGridLayoutManager extends GridLayoutManager {
    private TvRecyclerView mRecyclerView;

    public TvGridLayoutManager(Context context, int spanCount, TvRecyclerView rv) {
        super(context, spanCount);
        mRecyclerView = rv;
    }

    public TvGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, TvRecyclerView rv) {
        super(context, spanCount, orientation, reverseLayout);
        mRecyclerView = rv;
    }

    public TvGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, TvRecyclerView rv) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRecyclerView = rv;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        final boolean isScroll = mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE;
        if (isScroll) return focused;
        return null;
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        return nextFocus;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        int[] scrollAmount = RvHelper.getChildRectangleOnScreenScrollAmount(this, parent, child, rect, immediate);
        boolean result = super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
        if (result) {
            mRecyclerView.setSelectedItemScrollOffset(scrollAmount[0], scrollAmount[1]);
        } else {
            mRecyclerView.setSelectedItemScrollOffset(0, 0);
        }
        return result;
    }
}
