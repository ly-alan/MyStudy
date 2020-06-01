package com.example.tv_titlebar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TvTitleBar extends ConstraintLayout {

    /**
     * 默认7个控件，左边两个图片，中间2个标题，右边2个文本加一个，扩展view布局
     */
    public static enum ViewType {
        IV_LOGO,
        IV_ICON,
        TV_TITLE,
        TV_SUBTITLE,
        TV_MENU,
        TV_EXPLANATION,
        VIEW_EXTEND;
    }

    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvMenu;
    private TextView tvExplanation;
    private View viewExtend;

    public TvTitleBar(Context context) {
        super(context);
    }

    public TvTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TvTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    public void show(Builder builder) {
        //todo 应该直接调用这一个接口可以显示和设置visibleGone，value应该使用一个集合
        for (ViewType type : builder.map.keySet()) {
//            getViewByType(type)
            createViewAndAdd(type, builder.map.get(type));
        }
    }

    public void updateValue(ViewType viewType, Object object) {

    }

    /**
     * @param viewType
     * @param visibility One of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
     */
    public void changeVisible(ViewType viewType, int visibility) {

    }

    /**
     * 创建并添加view
     *
     * @param viewType
     */
    private void createViewAndAdd(ViewType viewType, Object value) {
        View view = getViewByType(viewType);
        if (view != null && indexOfChild(view) >= 0) {
            //view 已经添加到布局上了
            System.out.println("view has add");
            return;
        }
        view = createView(getViewIdResByType(viewType), viewType);
        if (view == null) {
            System.out.println("view create error");
            return;
        }
        setViewValue(view, value);
//        addView(view, getViewLayoutParams(view, viewType));
        addView(view);
        getViewConstraintSet(view, viewType).applyTo(this);
    }

    /**
     * 创建ImageView
     *
     * @return
     */
    private ImageView createImageView(@IdRes int id) {
        ImageView imageView = new ImageView(getContext());
        imageView.setId(id);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    /**
     * 创建TextView
     *
     * @param id
     * @return
     */
    private TextView createTextView(@IdRes int id) {
        TextView textView = new TextView(getContext());
        textView.setId(id);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * 设置控件值显示
     *
     * @param view
     * @param value
     */
    private void setViewValue(View view, Object value) {
        if (view == null) {
            System.out.println("setViewValue is error : view == null");
            return;
        }
        if (view instanceof ImageView) {
            if (value instanceof Drawable) {
                ((ImageView) view).setImageDrawable((Drawable) value);
            } else if (value instanceof Bitmap) {
                ((ImageView) view).setImageBitmap((Bitmap) value);
            } else if (value instanceof Integer) {
                ((ImageView) view).setImageResource((int) value);
            }
        } else if (view instanceof TextView) {
            if (value instanceof CharSequence) {
                ((TextView) view).setText((CharSequence) value);
            } else if (value instanceof Integer) {
                ((TextView) view).setText((int) value);
            }
        } else {
            System.out.println("view type is error");
        }
        view.setBackgroundColor(new Random().nextInt());
    }

    /**
     * 创建view
     *
     * @param id
     * @param viewType
     * @return
     */
    private View createView(@IdRes int id, ViewType viewType) {
        switch (viewType) {
            case IV_ICON:
                ivIcon = createImageView(id);
                return ivIcon;
            case IV_LOGO:
                ivLogo = createImageView(id);
                return ivLogo;
            case TV_TITLE:
                tvTitle = createTextView(id);
                return tvTitle;
            case TV_SUBTITLE:
                tvSubTitle = createTextView(id);
                return tvSubTitle;
            case TV_MENU:
                tvMenu = createTextView(id);
                return tvMenu;
            case TV_EXPLANATION:
                tvExplanation = createTextView(id);
                return tvExplanation;
            case VIEW_EXTEND:
                viewExtend = createTextView(id);
                return viewExtend;
            default:
                return null;
        }
    }

    /**
     * @param viewType
     * @return 根据type获取view 可能为null
     * @deprecated 不建议调用，所以用deprecated修饰
     */
    public View getViewByType(ViewType viewType) {
        switch (viewType) {
            case IV_ICON:
                return ivIcon;
            case IV_LOGO:
                return ivLogo;
            case TV_TITLE:
                return tvTitle;
            case TV_SUBTITLE:
                return tvSubTitle;
            case TV_MENU:
                return tvMenu;
            case TV_EXPLANATION:
                return tvExplanation;
            case VIEW_EXTEND:
                return viewExtend;
            default:
                return null;
        }
    }

    /**
     * 设置view格式
     *
     * @param view
     * @param viewType
     * @return
     */
    private ConstraintSet getViewConstraintSet(View view, ViewType viewType) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainHeight(view.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        switch (viewType) {
            case IV_LOGO:
                constraintSet.connect(view.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
                //设置控件比例1:1正方形
                constraintSet.setDimensionRatio(view.getId(), "1:1");
                break;
            case IV_ICON:
                constraintSet.connect(view.getId(), ConstraintSet.LEFT, R.id.lib_tv_titlebar_logo, ConstraintSet.RIGHT);
                constraintSet.setDimensionRatio(view.getId(), "1:1");
                break;
            case TV_TITLE:
                constraintSet.connect(view.getId(), ConstraintSet.LEFT, R.id.lib_tv_titlebar_icon, ConstraintSet.RIGHT);
                constraintSet.constrainWidth(view.getId(), ConstraintSet.WRAP_CONTENT);
                break;
            case TV_SUBTITLE:
                constraintSet.connect(view.getId(), ConstraintSet.LEFT, R.id.lib_tv_titlebar_title, ConstraintSet.RIGHT);
                constraintSet.constrainWidth(view.getId(), ConstraintSet.WRAP_CONTENT);
                break;
            case TV_MENU:
                //最右边
                constraintSet.connect(view.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
                constraintSet.constrainWidth(view.getId(), ConstraintSet.WRAP_CONTENT);
                break;
            case TV_EXPLANATION:
                constraintSet.connect(view.getId(), ConstraintSet.RIGHT, R.id.lib_tv_titlebar_menu, ConstraintSet.LEFT);
                constraintSet.constrainWidth(view.getId(), ConstraintSet.WRAP_CONTENT);
                break;
            case VIEW_EXTEND:
                constraintSet.connect(view.getId(), ConstraintSet.RIGHT, R.id.lib_tv_titlebar_explanation, ConstraintSet.LEFT);
                constraintSet.constrainWidth(view.getId(), ConstraintSet.WRAP_CONTENT);
                break;
            default:
                break;
        }
        constraintSet.constrainMinWidth(view.getId(), getMeasuredHeight());
        return constraintSet;
    }

    private LayoutParams getViewLayoutParams(View view, ViewType viewType) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.MATCH_PARENT;
        params.matchConstraintMinWidth = getHeight();
        params.dimensionRatio = "1:1";
        return params;
    }

    /**
     * 根据type获取对应view的id
     *
     * @param viewType
     * @return
     */
    private int getViewIdResByType(ViewType viewType) {
        switch (viewType) {
            case IV_LOGO:
                return R.id.lib_tv_titlebar_logo;
            case IV_ICON:
                return R.id.lib_tv_titlebar_icon;
            case TV_TITLE:
                return R.id.lib_tv_titlebar_title;
            case TV_SUBTITLE:
                return R.id.lib_tv_titlebar_subtitle;
            case TV_MENU:
                return R.id.lib_tv_titlebar_menu;
            case TV_EXPLANATION:
                return R.id.lib_tv_titlebar_explanation;
            case VIEW_EXTEND:
                return R.id.lib_tv_titlebar_extend;
            default:
                return View.NO_ID;
        }
    }


    public static class Builder {

        Map<ViewType, Object> map = new HashMap<>();

        public Builder() {
        }

        public Builder add(ViewType viewType, Object object) {
            map.put(viewType, object);
            return this;
        }
    }


}
