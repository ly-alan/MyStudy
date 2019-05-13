package com.android.commonlib.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.commonlib.R;
import com.android.commonlib.utils.ResUtils;

/**
 * 自定义导航栏
 * Created by yong.liao on 2018/3/29 0029.
 */
public class TitleBar extends Toolbar {
    /**
     * 导航栏标题
     */
    TextView mTitleText;
    /**
     * 左侧按钮布局
     */
    LinearLayout mLeftBtnsLayout;
    /**
     * 右侧按钮布局
     */
    LinearLayout mRightBtnsLayout;
    /**
     * 左侧按钮
     */
    public ImageTextButton mBackButton;
    /**
     * 右侧按钮
     */
    ImageTextButton mMenuButton;
    /**
     * 中间布局
     */
    LinearLayout mCentreLayout;
    /**
     * 默认按钮点击效果
     */
    int mButtonBackground;
    /**
     * 默认返回键样式
     */
    int mBackButtonRes;
    /**
     * 背景颜色
     */
    int mBackground;
    /**
     * 标题文本颜色
     */
    ColorStateList mTitleColor;
    /**
     * 副控件文本样式
     */
    ColorStateList mTextColor;

    public TitleBar(Context context) {
        this(context, null, R.attr.title_bar_style);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.title_bar_style);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources.Theme theme = context.getTheme();
        // 获取主题中定义的导航栏样式
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, R.style.TitleBar);
        //返回键
        mBackButtonRes = array.getResourceId(R.styleable.TitleBar_back_bg, R.drawable.title_bar_back_white);
        //点击效果
//        mButtonBackground = array.getResourceId(R.styleable.TitleBar_button_click_bg,
//                R.drawable.titlebar_item_click_bg);
        //背景颜色
        mBackground = array.getResourceId(R.styleable.TitleBar_background, 0);
        //标题颜色
        mTitleColor = array.getColorStateList(R.styleable.TitleBar_titleColor);
        if (mTitleColor == null) {
            mTitleColor = ColorStateList.valueOf(ResUtils.getColor(R.color.black));
        }
        //副控件文本颜色
        mTextColor = array.getColorStateList(R.styleable.TitleBar_textColor);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(ResUtils.getColor(R.color.black));
        }
        array.recycle();
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        //设置布局的起始位置
        setContentInsetsRelative(0, 0);
        //去除默认Navigation按钮
        setNavigationIcon(null);
        //去除阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(0);
        }
        this.setBackgroundResource(mBackground);
        inflate(getContext(), R.layout.layout_title_bar, this);
        mTitleText = (TextView) findViewById(R.id.tv_titlebar_center);
        mCentreLayout = (LinearLayout) findViewById(R.id.linear_titlebar_center);
        mLeftBtnsLayout = (LinearLayout) findViewById(R.id.linear_titlebar_left);
        mRightBtnsLayout = (LinearLayout) findViewById(R.id.linear_titlebar_right);
        //设置标题文本样式
        mTitleText.setTextColor(mTitleColor.getColorForState(getDrawableState(), 0));
    }

    /**
     * 获取一个新的导航栏按钮，并设置样式
     *
     * @return
     */
    public ImageTextButton newTitlebarButton() {
        //设置副控件文本、点击效果样式
        ImageTextButton button = new ImageTextButton(this.getContext());
        button.setTextSize(ResUtils.getDimensionPixelSize(R.dimen.text_14sp));
        button.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        button.setBackgroundResource(mButtonBackground);
        button.setTextColor(mTextColor.getColorForState(getDrawableState(), 0));
        return button;
    }

    /**
     * 获得导航栏默认返回按钮
     *
     * @return ImageTextButton
     */
    public ImageTextButton getBackButton() {
        return mBackButton;
    }

    /**
     * 获得导航栏默认菜单按钮
     *
     * @return ImageTextButton
     */
    public ImageTextButton getMenuButton() {
        return mMenuButton;
    }


    /**
     * 获取导航栏标题 TextView控件
     *
     * @return TextView
     */
    public TextView getTitleText() {
        return mTitleText;
    }

    /**
     * 设置默认的返回按钮
     */
    private void addBackButton() {
        if (null == mBackButton) {
            mBackButton = newTitlebarButton();
            mBackButton.setId(R.id.titlebar_back_button);
            mBackButton.setMinimumWidth(ResUtils.getDimensionPixelSize(R.dimen.title_btn_width));
            mLeftBtnsLayout.addView(mBackButton,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 设置默认的菜单按钮
     */
    private void addMenuButton() {
        if (null == mMenuButton) {
            mMenuButton = newTitlebarButton();
            mMenuButton.setId(R.id.titlebar_menu_button);
            mMenuButton.setMinimumWidth(ResUtils.getDimensionPixelSize(R.dimen.title_btn_width));
            mRightBtnsLayout.addView(mMenuButton,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 添加文字和图标反回
     *
     * @param drawable 图标
     * @param text     文字
     * @param listener 点击事件
     */
    public void addImageTextBack(Drawable drawable, String text, OnClickListener listener) {
        TextView textView = new TextView(this.getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResUtils.getDimensionPixelSize(R.dimen.text_14sp));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(ResUtils.getDimensionPixelSize(R.dimen.padding_14dp), 0, 0, 0);
        textView.setCompoundDrawablePadding(ResUtils.getDimensionPixelSize(R.dimen.padding_4dp));
        textView.setText(text);
        textView.setBackgroundResource(mButtonBackground);
        textView.setTextColor(mTextColor.getColorForState(getDrawableState(), 0));
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        textView.setOnClickListener(listener);
        addTitleBarButton(false, 0, textView);
    }

    /**
     * 添加文字和图标反回
     *
     * @param listener 点击事件
     */
    public void addImageTextBack(OnClickListener listener) {
        addImageTextBack(ResUtils.getDrawable(R.drawable.title_bar_back_white),
                ResUtils.getString(R.string.back),
                listener);
    }

    public void addRightView(View view) {
        mRightBtnsLayout.addView(view, 0, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 添加一个导航栏按钮
     *
     * @param isMenuButton 是否为菜单按钮(true按钮再右边，false按钮在左边)
     * @param index        按钮位置
     * @param view         继承了ImageTextButton的按钮
     */
    public void addTitleBarButton(boolean isMenuButton, int index, View view) {
        if (null != view) {
            view.setMinimumWidth(ResUtils.getDimensionPixelSize(R.dimen.title_btn_width));
            if (isMenuButton) {
                mRightBtnsLayout.addView(view, index,
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                mLeftBtnsLayout.addView(view, index,
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
    }

    /**
     * 设置返回键 带标题
     *
     * @param text     按钮文本
     * @param listener 点击事件监听
     */
    public void setBackButton(String text, OnClickListener listener) {
        addBackButton();
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(listener);
        setBackButtonText(text);
    }

    /**
     * 设置返回键 带标题
     *
     * @param resID    图片资源ID
     * @param listener 点击事件监听
     */
    public void setBackButton(@DrawableRes int resID, OnClickListener listener) {
        addBackButton();
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(listener);
        setBackButtonIcon(resID);
        mBackButton.setPressed(false);
    }


    /**
     * 设置默认的菜按钮
     *
     * @param text     按钮文本
     * @param listener 点击事件监听
     */
    public void setMenuButton(String text, OnClickListener listener) {
        addMenuButton();
        mMenuButton.setVisibility(View.VISIBLE);
        mMenuButton.setOnClickListener(listener);
        setMenuButtonText(text);
    }

    /**
     * 设置默认的菜按钮
     *
     * @param resID    图片资源ID
     * @param listener 点击事件监听
     */
    public void setMenuButton(@DrawableRes int resID, OnClickListener listener) {
        addMenuButton();
        mMenuButton.setVisibility(View.VISIBLE);
        mMenuButton.setOnClickListener(listener);
        setMenuButtonIcon(resID);
    }

    /**
     * 设置默认的菜单按钮，带图片
     *
     * @param resID
     */
    private void setMenuButtonIcon(@DrawableRes int resID) {
        addMenuButton();
        if (mMenuButton != null && resID > 0) {
            mMenuButton.setVisibility(View.VISIBLE);
            mMenuButton.setText("");
            mMenuButton.setImageResource(resID);
        }
    }

    /**
     * 确认按钮是否可点击
     */
    public void setMenuButtonEnable(boolean enable) {
        if (mMenuButton != null) {
            mMenuButton.setEnabled(enable);
        }
    }

    /**
     * 设置默认的菜单按钮文字
     *
     * @param str
     */
    private void setMenuButtonText(String str) {
        addMenuButton();
        if (mMenuButton != null) {
            if (str != null && !"".equals(str)) {
                mMenuButton.setVisibility(View.VISIBLE);
                mMenuButton.setText(str);
            } else {
                mMenuButton.setVisibility(View.INVISIBLE);
                mMenuButton.setText("");
            }
            mMenuButton.setImageDrawable(null);
        }
    }

    /**
     * 设置左侧按钮图片
     *
     * @param resID
     */
    private void setBackButtonIcon(@DrawableRes int resID) {
        addBackButton();
        if (mBackButton != null) {
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setText("");
            if (resID > 0) {
                mBackButton.setImageResource(resID);
            } else {
                mBackButton.setImageResource(mBackButtonRes);
            }
        }
    }

    /**
     * 设置左侧按钮文字
     *
     * @param stringID
     */
    public void setBackButtonText(@StringRes int stringID) {
        addBackButton();
        if (mBackButton != null && stringID > 0) {
            mBackButton.setVisibility(View.VISIBLE);
            mBackButton.setText(ResUtils.getString(stringID));
            mBackButton.setImageDrawable(null);
        }
    }

    /**
     * 设置左侧按钮文字
     *
     * @param str
     */
    public void setBackButtonText(String str) {
        addBackButton();
        if (mBackButton != null) {
            if (str != null && !"".equals(str)) {
                mBackButton.setVisibility(View.VISIBLE);
                mBackButton.setText(str);
            } else {
                mBackButton.setVisibility(View.INVISIBLE);
                mBackButton.setText("");
            }
            mBackButton.setImageDrawable(null);
        }
    }

    /**
     * 设置导航栏标题字体颜色
     *
     * @param color
     */
    public void setTitleTextColor(int color) {
        if (mTitleText != null) {
            mTitleText.setTextColor(color);
        }
    }

    /**
     * 设置副控件文本颜色
     *
     * @param color
     */
    public void setSubtitleTextColor(int color) {
        for (int i = 0; i <= getChildCount(); i++) {
            View view = getChildAt(i);
            if (null != view) {
                if (view instanceof ImageTextButton) {
                    ((ImageTextButton) view).setTextColor(color);
                } else if (view instanceof TextView && view != mTitleText) {
                    ((TextView) view).setTextColor(color);
                }
            }
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {
        if (resId > 0) {
            setTitle(getContext().getString(resId));
        }
        super.setTitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleText != null) {
            if (title != null && !title.equals("")) {
                mTitleText.setText(title.toString());
                mTitleText.setVisibility(View.VISIBLE);
                mTitleText.requestLayout();
            } else {
                mTitleText.setText("");
                mTitleText.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public CharSequence getTitle() {
        String str = getTitleText().getText().toString();
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    @Deprecated
    public CharSequence getSubtitle() {
        String str = getTitleText().getText().toString();
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

//    /**
//     * 根据ID获取导航栏按钮
//     *
//     * @param buttonID
//     * @return
//     */
//    public ImageTextButton getTitleBarButton(int buttonID) {
//        View view = findViewById(buttonID);
//        if (view instanceof ImageTextButton) {
//            return (ImageTextButton) view;
//        }
//        return null;
//    }

}
