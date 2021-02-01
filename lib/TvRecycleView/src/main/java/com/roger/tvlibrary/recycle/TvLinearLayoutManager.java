package com.roger.tvlibrary.recycle;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 因为快速长按焦点丢失问题.
 * Created by roger on 2016/8/25.
 */
public class TvLinearLayoutManager extends LinearLayoutManager {

    private int[] mMeasuredDimension = new int[2];
    private boolean mIsAutoMeaure = false;
    private RecyclerView mRecyclerView;

    public TvLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TvLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TvLinearLayoutManager(Context context) {
        super(context);
    }

    public TvLinearLayoutManager(Context context, RecyclerView recyclerView) {
        this(context);
        mRecyclerView = recyclerView;
    }

    /**
     * 用于leanback自动适应.
     */
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

        if (!mIsAutoMeaure) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        } else {

            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            int width = 0;
            int height = 0;

            //
            int tempWidth = 0;
            int tempHeight = 0;

            for (int i = 0; i < getItemCount(); i++) {
                try {
                    measureScrapChild(recycler, i,
                            widthSpec,
                            View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                            mMeasuredDimension);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                if (getOrientation() == HORIZONTAL) {
                    tempWidth += mMeasuredDimension[0];
                    if (i == 0) {
                        tempHeight = mMeasuredDimension[1];
                    }
                } else {  // VERTICAL
                    tempHeight += mMeasuredDimension[1];
                    if (i == 0) {
                        tempWidth = mMeasuredDimension[0];
                    }
                }
            }

            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                case View.MeasureSpec.AT_MOST:
                    width = widthSize;
                    break;
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    width = tempWidth;
                    break;
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                case View.MeasureSpec.AT_MOST:
                    height = heightSize;
                    break;
                case View.MeasureSpec.UNSPECIFIED:
                default:
                    height = tempHeight;
                    break;
            }

            setMeasuredDimension(width, height);
        }

    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(widthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }

    /**
     * 自动适应布局. (当height="wrap_..")
     */
    public void setAutoMeasureEnabled(boolean isAutoMeaure) {
        this.mIsAutoMeaure = isAutoMeaure;
    }

    /**
     * 缓解d-pad按住不动跑焦点的问题
     */
    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        if (mRecyclerView == null) return null;
        final FocusFinder ff = FocusFinder.getInstance();
        View result = null;
        if (direction == View.FOCUS_FORWARD || direction == View.FOCUS_BACKWARD) {
            // convert direction to absolute direction and see if we have a view there and if not
            // tell LayoutManager to add if it can.
            if (canScrollVertically()) {
                final int absDir = direction == View.FOCUS_FORWARD ? View.FOCUS_DOWN : View.FOCUS_UP;
                result = ff.findNextFocus(mRecyclerView, focused, absDir);
            }
            if (canScrollHorizontally()) {
                boolean rtl = getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL;
                final int absDir = (direction == View.FOCUS_FORWARD) ^ rtl ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
                result = ff.findNextFocus(mRecyclerView, focused, absDir);
            }
        } else {
            result = ff.findNextFocus(mRecyclerView, focused, direction);
        }

        if (result != null) {
            return result;
        }

        if (mRecyclerView.getDescendantFocusability() == ViewGroup.FOCUS_BLOCK_DESCENDANTS) {
            return mRecyclerView.getParent().focusSearch(focused, direction);
        }

        // if (DEBUG) Log.v(getTag(), "regular focusSearch failed direction " + direction);
        final boolean isScroll = mRecyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE;

        if (isScroll) {
            result = focused;
        }

        return result;
    }

    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //这里滑动到底部或顶部 View还没加载出来的时候会导致焦点跳转错乱
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        if (nextFocus == null)
            return focused;
        else return nextFocus;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        int[] scrollAmount = RvHelper.getChildRectangleOnScreenScrollAmount(this, parent, child, rect, immediate); // 获取滚动距离
        boolean result = super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible);
        if (parent instanceof TvRecyclerView) {
            TvRecyclerView rtv = (TvRecyclerView) parent;
            if (result) {
                rtv.setSelectedItemScrollOffset(scrollAmount[0], scrollAmount[1]);
            } else {
                rtv.setSelectedItemScrollOffset(0, 0);
            }
        }
        return result;
    }

    public void setMyRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

}
