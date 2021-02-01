package com.roger.tvlibrary.recycle;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;

import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ExRecyclerViewHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * RecyclerView TV适配版本.
 * https://github.com/zhousuqiang/TvRecyclerView(参考源码)
 */
public class TvRecyclerView extends RecyclerView {
    private final static String TAG = TvRecyclerView.class.getSimpleName();

    public TvRecyclerView(Context context) {
        this(context, null);
    }

    public TvRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TvRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private int SCROLL_VELOCITY_FACTOR_X = 7000;
    private int SCROLL_VELOCITY_FACTOR_Y = SCROLL_VELOCITY_FACTOR_X;
    private boolean mSelectedItemCentered = true;
    private int mSelectedItemOffsetStart;
    private int mSelectedItemOffsetEnd;
    private int position = 0;
    private OnItemListener mOnItemListener;
    private OnItemClickListener mOnItemClickListener; // item 单击事件.
    private OnChildViewHolderSelectedListener mChildViewHolderSelectedListener;
    private ItemListener mItemListener;
    protected int offset = -1;
    protected int[] mOffset = new int[2]; // 选中ITEM的滚动偏移量，飞框使用

    private View mEmptyView; // 没有数据时显示的view

    private boolean mRememberLastFocus = true;
    private FocusArchivist mFocusArchivist = new FocusArchivist();
    private int mPendingFocusPos = NO_POSITION; // Adapter position that will be selected after certain layout pass.

    protected long lastLoseFocusTime = 0;
    protected int selectedPosition;

    private void init(Context context, AttributeSet attrs) {
        setHasFixedSize(true);
        setWillNotDraw(true); // 子类不执行onDraw
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setChildrenDrawingOrderEnabled(true);
        setClipChildren(false);
        setClipToPadding(false);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
        setClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setNestedScrollingEnabled(false);
        mItemListener = new ItemListener() {
            /**
             * 子控件的点击事件
             * @param itemView
             */
            @Override
            public void onClick(View itemView) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(TvRecyclerView.this, itemView, getChildLayoutPosition(itemView));
                }
            }

            /**
             * 子控件的焦点变动事件
             * @param itemView
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View itemView, boolean hasFocus) {
                if (null != itemView) {
                    if (hasFocus) {
                        lastLoseFocusTime = System.currentTimeMillis();
                        if (mOnItemListener != null)
                            mOnItemListener.onItemSelected(TvRecyclerView.this, itemView, getChildLayoutPosition(itemView));
                    } else {
                        if (!onFocusLostDealWith(itemView)) {
                            if (mOnItemListener != null)
                                mOnItemListener.onItemPreSelected(TvRecyclerView.this, itemView, getChildLayoutPosition(itemView));
                        }
                        ;
                    }
                }
            }
        };
    }

    //是否处理焦点快速丢失情况
    protected boolean onFocusLostDealWith(View itemView) {
//        if(needRequestFocus){
//            long currentTime = System.currentTimeMillis();
//            if(currentTime - lastLoseFocusTime < 50){
//                itemView.requestFocus();
//                return true;
//            }
//        }
        return false;
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    public void onChildAttachedToWindow(View child) {
        // 设置单击事件，修复.
        if (!child.hasOnClickListeners()) {
            child.setOnClickListener(mItemListener);
        }
        // 设置焦点事件，修复.
        if (child.getOnFocusChangeListener() == null) {
            child.setOnFocusChangeListener(mItemListener);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            boolean favorNaturalFocus = !mRememberLastFocus && previouslyFocusedRect != null;
            View lastFocusedView = mFocusArchivist.getLastFocus(this);
            if (favorNaturalFocus || lastFocusedView == null) {
                mPendingFocusPos = mPendingFocusPos == NO_POSITION ? 0 : mPendingFocusPos;
                try {
                    if (getChildAt(mPendingFocusPos) != null) { // 不确定子view是否可获得焦点，进行try
                        getChildAt(mPendingFocusPos).requestFocus();
                        mPendingFocusPos = NO_POSITION;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                lastFocusedView.requestFocus();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mPendingFocusPos != NO_POSITION) {
            RecyclerView.ViewHolder holder = findViewHolderForAdapterPosition(mPendingFocusPos);
            if (holder != null) {
                if (hasFocus()) {
                    holder.itemView.requestFocus();
                } else {
                    // 这里保存？
                    mFocusArchivist.archiveFocus(this, holder.itemView);
                }
            }
            mPendingFocusPos = NO_POSITION;
        }
    }

    @Override
    public void addFocusables(@NonNull ArrayList<View> views, int direction, int focusableMode) {
        // Allow focus on children only if focus is already in this view
        if (hasFocus()) {
            super.addFocusables(views, direction, focusableMode);
        } else { // if (isFocusable())
            views.add(this);
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        boolean consumed = super.requestFocus(direction, previouslyFocusedRect);
        if (!consumed) {
            mPendingFocusPos = mPendingFocusPos == NO_POSITION ? 0 : mPendingFocusPos;
        }
        return consumed;
    }

    @Override
    public boolean isInTouchMode() {
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !super.isInTouchMode());
        } else {
            return super.isInTouchMode();
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        // 一行的选中.
        if (mChildViewHolderSelectedListener != null) {
            int pos = getPositionByView(child);
            RecyclerView.ViewHolder vh = getChildViewHolder(child);
            mChildViewHolderSelectedListener.onChildViewHolderSelected(this, vh, pos);
        }
        //
        if (null != child) {
            if (mSelectedItemCentered) {
                mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
                mSelectedItemOffsetStart /= 2;
                mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
            }
        }
        // 保存下来焦点
        mFocusArchivist.archiveFocus(this, child);
        super.requestChildFocus(child, focused);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();

        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;

//        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
//        final int childTop = child.getTop() + rect.top - child.getScrollY();

        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);

        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        final boolean canScrollVertical = getLayoutManager().canScrollVertically();

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy;
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }

        if (cannotScrollForwardOrBackward(isVertical() ? dy : dx)) {
            offset = -1;
        } else {
            offset = isVertical() ? dy : dx; // 这里的offset并不一定是真正scroll的值，会出现bug
            if (dx != 0 || dy != 0) {
                if (immediate) {
                    scrollBy(dx, dy);
                } else {
                    smoothScrollBy(dx, dy);
                }
                return true;
            }

        }


        // 重绘是为了选中item置顶，具体请参考getChildDrawingOrder方法
        postInvalidate();

        return false;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(defAdapterDataObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(defAdapterDataObserver);
        }
    }

    private boolean cannotScrollForwardOrBackward(int value) {
        return cannotScrollBackward(value) || cannotScrollForward(value);
    }

    /**
     * 判断第一个位置，没有移动.
     * getStartWithPadding --> return (mIsVertical ? getPaddingTop() : getPaddingLeft());
     */
    public boolean cannotScrollBackward(int delta) {
        return (getFirstVisiblePosition() == 0 && delta <= 0);
    }

    /**
     * 判断是否达到了最后一个位置，没有再移动了.
     * getEndWithPadding -->  mIsVertical ?  (getHeight() - getPaddingBottom()) :
     * (getWidth() - getPaddingRight());
     */
    public boolean cannotScrollForward(int delta) {
        int lastCompletelyVisibleItemPosition = findLastCompletelyVisibleItemPosition();
        if (lastCompletelyVisibleItemPosition == NO_POSITION) {
            return ((getFirstVisiblePosition() + 1 + getLayoutManager().getChildCount()) == getLayoutManager().getItemCount()) && (delta >= 0);
        } else {
            return lastCompletelyVisibleItemPosition + 1 == getLayoutManager().getItemCount() && delta >= 0;
        }
    }

    @Override
    public int getBaseline() {
        return offset;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        // ViewFlinger --> smoothScrollBy(int dx, int dy, int duration, Interpolator interpolator)
        // ViewFlinger --> run --> hresult = mLayout.scrollHorizontallyBy(dx, mRecycler, mState);
        // LinearLayoutManager --> scrollBy --> mOrientationHelper.offsetChildren(-scrolled);
//        Timber.e("XXXXXXXXXXXXXX" +"smoothScrollBy-----------"+dx+".."+dy);
        LayoutManager mLayout = getLayoutManager();
        if (mLayout == null) {
            return;
        }
        if (isLayoutFrozen()) {
            return;
        }
        if (!mLayout.canScrollHorizontally()) {
            dx = 0;
        }
        if (!mLayout.canScrollVertically()) {
            dy = 0;
        }
        if (dx != 0 || dy != 0) {
            try {
                ReflectionUtils.invokeMethod(ExRecyclerViewHelper.getViewFlinger(this), "smoothScrollBy",
                        new Class[]{int.class, int.class, int.class, int.class},
                        new Object[]{dx, dy, SCROLL_VELOCITY_FACTOR_X, SCROLL_VELOCITY_FACTOR_Y});
            } catch (InvocationTargetException e) {
                super.smoothScrollBy(dx, dy);
            }
        }
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        mPendingFocusPos = position;
    }

    public int getSelectedItemOffsetStart() {
        return mSelectedItemOffsetStart;
    }

    public int getSelectedItemOffsetEnd() {
        return mSelectedItemOffsetEnd;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        LinearLayoutManager layout = (LinearLayoutManager) getLayoutManager();
        return layout.getOrientation() == LinearLayoutManager.VERTICAL;
    }

    /**
     * 设置选中的Item距离开始或结束的偏移量；
     * 与滚动方向有关；
     * 与setSelectedItemAtCentered()方法二选一
     *
     * @param offsetEnd 从结尾到你移动的位置.
     */
    public void setSelectedItemOffset(int offsetStart, int offsetEnd) {
        this.mSelectedItemOffsetStart = offsetStart;
        this.mSelectedItemOffsetEnd = offsetEnd;
    }

    /**
     * 设置选中的Item居中；
     * 与setSelectedItemOffset()方法二选一
     */
    public void setSelectedItemAtCentered(boolean isCentered) {
        this.mSelectedItemCentered = isCentered;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View view = getFocusedChild();
        if (null != view) {
            position = getChildAdapterPosition(view) - getFirstVisiblePosition();
            if (position < 0) {
                return i;
            } else {
                if (i == childCount - 1) {//这是最后一个需要刷新的item
                    if (position > i) {
                        position = i;
                    }
                    return position;
                }
                if (i == position) {//这是原本要在最后一个刷新的item
                    return childCount - 1;
                }
            }
        }
        return i;
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() == 0)
            return 0;
        else
            return findFirstCompletelyVisibleItemPosition();
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0)
            return 0;
        else
            return getChildLayoutPosition(getChildAt(childCount - 1));
    }

    @Override
    public void onScrollStateChanged(int state) {
//        Timber.e("XXXXXXXXXXXXXX" +"onScrollStateChanged-----------"+state+"");
        // 通知页码改变
        if (mPagingableListener != null) {
            //int lastVisibleItemPosition = findLastVisibleItemPosition();
            int lastVisibleItemPosition = findLastVisibleItemPosition();
            if (lastVisibleItemPosition != NO_POSITION)
                mPagingableListener.onVisibleChange(lastVisibleItemPosition);
        }
        if (state == SCROLL_STATE_IDLE) {
            offset = -1;
            // 通知item移动
            if (mOnItemListener != null && getFocusedChild() != null) {
                final View focus = getFocusedChild();
                mOnItemListener.onReviseFocusFollow(this, focus, getChildLayoutPosition(focus));
            }
            executePaging();
        }
        super.onScrollStateChanged(state);
    }

    private interface ItemListener extends View.OnClickListener, View.OnFocusChangeListener {
    }

    public interface OnItemListener {
        void onItemPreSelected(TvRecyclerView parent, View itemView, int position);

        void onItemSelected(TvRecyclerView parent, View itemView, int position);

        void onReviseFocusFollow(TvRecyclerView parent, View itemView, int position);
    }

    public interface OnChildViewHolderSelectedListener {
        void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder vh, int position);
    }

    public interface OnItemClickListener {
        void onItemClick(TvRecyclerView parent, View itemView, int position);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 控制焦点高亮问题.
     * 2016.08.29
     */
    public void setOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        mChildViewHolderSelectedListener = listener;
    }

    private int getPositionByView(View view) {
        if (view == null) {
            return NO_POSITION;
        }
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        if (params == null || params.isItemRemoved()) {
            // when item is removed, the position value can be any value.
            return NO_POSITION;
        }
        return params.getViewPosition();
    }

    /////////////////// 按键加载更多 start start start //////////////////////////

    private PagingableListener mPagingableListener;
    private boolean isLoading = false;
    private OnPagingCompleteListener mOnPagingCompletedListener = new OnPagingCompleteListener() {
        @Override
        public void onPagingCompleted() {
            isLoading = false;
        }
    };

    public interface OnPagingCompleteListener {
        void onPagingCompleted();
    }

    public interface PagingableListener {
        void onLoadMoreItems(OnPagingCompleteListener pagingCompleteListener);

        void onVisibleChange(int lastVisibleItem);
    }

    public void setOnLoadMoreComplete() {
        isLoading = false;
    }

    public void setPagingableListener(PagingableListener pagingableListener) {
        this.mPagingableListener = pagingableListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    private boolean executePaging() {
        // 先判断分页Listener
        if (mPagingableListener == null) return false;
        // 判断layoutmanager
        LayoutManager lm = getLayoutManager();
        if (lm == null) return false;
        int totalItemCount = lm.getItemCount();
        /*int lastVisibleItem = findLastVisibleItemPosition();
        int lastComVisiPos = findLastCompletelyVisibleItemPosition();*/
        int visibleItemCount = getChildCount();
        int firstVisibleItem = findFirstVisibleItemPosition();
        // 判断是否显示最底了.
        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            isLoading = true;
            mPagingableListener.onLoadMoreItems(mOnPagingCompletedListener);
            return true;
        }
        return false;
    }

    /**
     * 判断是否为横向布局
     */
    private boolean isHorizontalLayoutManger() {
        LayoutManager lm = getLayoutManager();
        if (lm != null) {
            if (lm instanceof LinearLayoutManager) {
                LinearLayoutManager llm = (LinearLayoutManager) lm;
                return LinearLayoutManager.HORIZONTAL == llm.getOrientation();
            }
            if (lm instanceof GridLayoutManager) {
                GridLayoutManager glm = (GridLayoutManager) lm;
                return GridLayoutManager.HORIZONTAL == glm.getOrientation();
            }
        }
        return false;
    }

    /**
     * 最后的位置.
     */
    public int findLastVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * 滑动到底部.
     */
    public int findLastCompletelyVisibleItemPosition() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
            if (layoutManager instanceof GridLayoutManager) {
                return ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    public int findFirstVisibleItemPosition() {
        LayoutManager lm = getLayoutManager();
        if (lm != null) {
            if (lm instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
            }
            if (lm instanceof GridLayoutManager) {
                return ((GridLayoutManager) lm).findFirstVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    public int findFirstCompletelyVisibleItemPosition() {
        LayoutManager lm = getLayoutManager();
        if (lm != null) {
            if (lm instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) lm).findFirstCompletelyVisibleItemPosition();
            }
            if (lm instanceof GridLayoutManager) {
                return ((GridLayoutManager) lm).findFirstCompletelyVisibleItemPosition();
            }
        }
        return RecyclerView.NO_POSITION;
    }

    /////////////////// 按键加载更多 end end end //////////////////////////

    /////////////////// 按键拖动 Item start start start ///////////////////////

    private final ArrayList<OnItemKeyListener> mOnItemKeyListeners =
            new ArrayList<OnItemKeyListener>();

    public static interface OnItemKeyListener {
        public boolean dispatchKeyEvent(KeyEvent event);
    }

    public void addOnItemKeyListener(OnItemKeyListener listener) {
        mOnItemKeyListeners.add(listener);
    }

    public void removeOnItemKeyListener(OnItemKeyListener listener) {
        mOnItemKeyListeners.remove(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    ////////////////// 按键拖动 Item end end end /////////////////////////

    /**
     * 设置默认选中.
     */
    public void setDefaultSelect(int pos) {
        ViewHolder vh = findViewHolderForAdapterPosition(pos);
        if (vh != null) {
            requestFocusFromTouch();
            vh.itemView.requestFocus();
        }
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        mEmptyView.setVisibility(GONE);
    }

    private void checkIfEmpty() {
        if (mEmptyView != null && getAdapter() != null) {
            mEmptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
        }
    }

    // 默认的数据变化观察者
    private AdapterDataObserver defAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mFocusArchivist.clear();
            TvRecyclerView rv = TvRecyclerView.this;
            // 滚动到第一个位置，否则换个菜单后默认不显示第一个
            rv.scrollToPosition(0);
            // 如果没有数据则不能获得焦点
            if (rv.getAdapter().getItemCount() <= 0) {
                rv.setFocusable(false);
                if (rv.hasFocus()) rv.clearFocus();
            } else {
                rv.setFocusable(true);
            }
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    /**
     * 获取选中ITEM的滚动偏移量
     */
    public int[] getSelectedItemScrollOffset() {
        return mOffset;
    }

    public void setSelectedItemScrollOffset(int dx, int dy) {
        mOffset[0] = dx;
        mOffset[1] = dy;
    }

    //////////from another tv recyclerview

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = super.onKeyDown(keyCode, event);
        int direction = keyCode2Direction(keyCode);
        if (!result && direction != -1) {
            result = handleDpadKey(direction);
        }
        return result;
    }

    private boolean handleDpadKey(int direction) {

        View focused = getFocusedChild();
        /*-------------------------------start---------------------------------*/
//        int focusedPosition = getLayoutManager().getPosition(focused);
//        Adapter adapter = getAdapter();
//        if(focusedPosition == 0 || (adapter != null && focusedPosition == adapter.getItemCount() - 1)){
//            return true;
//        }
        /*---------------------------------end--------------------------------*/
        if (focused != null) {
            View nextFocus = focused.focusSearch(direction);
            if (nextFocus != null) {
                Log.i(TAG, "searched focused child! focusedPosition:" + getLayoutManager().getPosition(nextFocus));
                nextFocus.requestFocus();
            } else {
                Log.i(TAG, "didn't search focused child!");
                if (focused.getParent() == null) {
                    LayoutManager lm = getLayoutManager();
                    int focusedPos = lm.getPosition(focused);
                    if (focusedPos == -1) {
                        nextFocus = lm.findViewByPosition(findLastVisibleItemPosition());
                    } else {
                        nextFocus = getLayoutManager().findViewByPosition(focusedPos - 4);
                    }
                    if (nextFocus != null) {
                        nextFocus.requestFocus();
                    } else {
                        Log.e(TAG, "wtf");
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * keycode值转成Direction值
     */
    private int keyCode2Direction(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                return FOCUS_DOWN;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return FOCUS_RIGHT;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                return FOCUS_LEFT;

            case KeyEvent.KEYCODE_DPAD_UP:
                return FOCUS_UP;

            default:
                return -1;
        }
    }

    public void setPendingFocusPos(int pos) {
        mPendingFocusPos = pos;
    }

    public void clearFocusArchivist() {
        mFocusArchivist.clear();
    }

    // 保存RecyclerView的焦点
    class FocusArchivist {
        private int mLastSelectedPos = NO_POSITION;
        private long mLastSelectedId = NO_ID;

        void archiveFocus(@NonNull RecyclerView rv) {
            if (rv.hasFocus()) {
                View focusedChild = rv.getFocusedChild();
                archiveFocus(rv, focusedChild);
            }
        }

        void archiveFocus(@NonNull RecyclerView rv, View child) {
            mLastSelectedPos = rv.getChildAdapterPosition(child);
            mLastSelectedId = rv.getChildItemId(child);
        }

        void archiveFocus(@NonNull RecyclerView rv, int pos) {
            mLastSelectedId = pos;
        }

        void clear() {
            mLastSelectedPos = 0;
            mLastSelectedId = NO_ID;
        }


        @Nullable
        View getLastFocus(@NonNull RecyclerView rv) {
            View lastFocused = findLastFocusedViewById(rv, mLastSelectedId);

            if (lastFocused != null) {
                return lastFocused;
            }

            return findLastFocusedViewByPos(rv, mLastSelectedPos);
        }

        private View findLastFocusedViewById(@NonNull RecyclerView rv, long id) {
            RecyclerView.Adapter adapter = rv.getAdapter();
            if (adapter != null && adapter.hasStableIds() && mLastSelectedId != NO_ID) {
                RecyclerView.ViewHolder viewHolder = rv.findViewHolderForItemId(id);
                if (viewHolder != null) {
                    //viewHolder.itemView.requestFocus();
                    return viewHolder.itemView;
                }
            }

            return null;
        }


        private View findLastFocusedViewByPos(@NonNull RecyclerView rv, int pos) {
            if (pos != NO_POSITION) {
                RecyclerView.ViewHolder viewHolder = rv.findViewHolderForAdapterPosition(pos);
                if (viewHolder != null) {
                    return viewHolder.itemView;
                }
            }
            return null;
        }
    }

    public void setScrollVelocityFactor(int factor) {
        this.SCROLL_VELOCITY_FACTOR_X = factor;
        this.SCROLL_VELOCITY_FACTOR_Y = factor;
    }
}