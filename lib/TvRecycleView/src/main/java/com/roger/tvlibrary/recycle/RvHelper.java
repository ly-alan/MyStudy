package com.roger.tvlibrary.recycle;

import android.annotation.SuppressLint;
import android.graphics.Rect;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by roger on 2016/11/28.
 */
class RvHelper {
//    // TODO: 2016/11/28 先不适用该方法 待完善
//    public static int getMaxVerticalScroll(RecyclerViewTV rv, boolean forward) {
//        RecyclerView.LayoutManager lm = rv.getLayoutManager();
//        if (lm == null) return -1;
//        if (lm instanceof GridLayoutManager) {
//            return -1;// 不支持GridLayoutManager
//        } else if (lm instanceof LinearLayoutManager) {
//            LinearLayoutManager llm = (LinearLayoutManager) lm;
//            if (forward) {
//                int lvip = llm.findLastVisibleItemPosition();
//                View lvv = llm.findViewByPosition(lvip);
//                Rect rect = new Rect(); // 最后一个可见view的位置
//                lvv.getGlobalVisibleRect(rect);
//            } else {
//
//            }
//        } else {
//            // not supported
//            return -1;
//        }
//        return 0;
//    }

    @SuppressLint("WrongConstant")
    public static boolean isVertical(LinearLayoutManager lm) {
        return lm.getOrientation() == OrientationHelper.VERTICAL;
    }



    public static int[] getChildRectangleOnScreenScrollAmount(RecyclerView.LayoutManager lm, RecyclerView parent, View child, Rect rect, boolean immediate) {
        int[] out = new int[2];
        final int parentLeft = lm.getPaddingLeft();
        final int parentTop = lm.getPaddingTop();
        final int parentRight = lm.getWidth() - lm.getPaddingRight();
        final int parentBottom = lm.getHeight() - lm.getPaddingBottom();
        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
        final int childTop = child.getTop() + rect.top - child.getScrollY();
        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft);
        final int offScreenTop = Math.min(0, childTop - parentTop);
        final int offScreenRight = Math.max(0, childRight - parentRight);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom);

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (lm.getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
            dx = offScreenRight != 0 ? offScreenRight
                    : Math.max(offScreenLeft, childRight - parentRight);
        } else {
            dx = offScreenLeft != 0 ? offScreenLeft
                    : Math.min(childLeft - parentLeft, offScreenRight);
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy = offScreenTop != 0 ? offScreenTop
                : Math.min(childTop - parentTop, offScreenBottom);
        out[0] = dx;
        out[1] = dy;
        return out;
    }

    public static boolean isFocusedChildVisibleAfterScrolling(RecyclerView.LayoutManager lm, RecyclerView parent, int dx, int dy, Rect tmpRect) {
        final View focusedChild = parent.getFocusedChild();
        if (focusedChild == null) {
            return false;
        }
        final int parentLeft = lm.getPaddingLeft();
        final int parentTop = lm.getPaddingTop();
        final int parentRight = lm.getWidth() - lm.getPaddingRight();
        final int parentBottom = lm.getHeight() - lm.getPaddingBottom();
        final Rect bounds = tmpRect;
        lm.getDecoratedBoundsWithMargins(focusedChild, bounds);

        if (bounds.left - dx >= parentRight || bounds.right - dx <= parentLeft
                || bounds.top - dy >= parentBottom || bounds.bottom - dy <= parentTop) {
            return false;
        }
        return true;
    }
}
