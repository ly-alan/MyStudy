package com.roger.tvmodule.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.roger.tvmodule.R;

public class TvTitleBar extends ConstraintLayout {

    ImageView ivLogo;
    ImageView ivStatus;

    LinearLayout leftLayout;
    TextView tvTitle;
    TextView tvSubTitle;

    LinearLayout rightLayout;
    TextView tvMenu;
    TextView tvSubMenu;


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
        LayoutInflater.from(getContext()).inflate(R.layout.c_ui_layout_titlebar_content, this);
        setFocusable(false);
        setEnabled(false);
        ivLogo = findViewById(R.id.c_ui_title_logo);
        ivStatus = findViewById(R.id.c_ui_title_status);
        leftLayout = findViewById(R.id.c_ui_layout_title_left);
        tvTitle = findViewById(R.id.c_ui_title_tv);
        tvSubTitle = findViewById(R.id.c_ui_title_sub_tv);
        rightLayout = findViewById(R.id.c_ui_layout_title_right);
        tvMenu = findViewById(R.id.c_ui_title_tv_menu);
        tvSubMenu = findViewById(R.id.c_ui_title_tv_sub_menu);
    }

    public void setLogo(@DrawableRes int resId) {
        ivLogo.setImageResource(resId);
    }

    public void setSubLogo(@DrawableRes int resId) {
        this.ivStatus.setImageResource(resId);
    }


    public void setTitle(int resId) {
        this.tvTitle.setText(resId);
    }


    public void setTitle(CharSequence title) {
        this.tvTitle.setText(title);
    }


    public void setSubtitle(int resId) {
        tvSubTitle.setText(resId);
    }


    public void setSubtitle(CharSequence subtitle) {
        tvSubTitle.setText(subtitle);
    }

    public void setMenuText(int resId) {
        tvMenu.setText(resId);
    }

    public void setMenuText(CharSequence menuText) {
        tvMenu.setText(menuText);
    }

    public void setSubMenuText(int resId) {
        tvSubMenu.setText(resId);
    }

    public void setSubMenuText(CharSequence subText) {
        tvSubMenu.setText(subText);
    }

    public LinearLayout getLeftLayout() {
        return leftLayout;
    }

    public TextView getTitleView() {
        return tvTitle;
    }

    public TextView getSubTitleView() {
        return tvSubTitle;
    }

    public LinearLayout getRightLayout() {
        return rightLayout;
    }

    public TextView getMenuView() {
        return tvMenu;
    }

    public TextView getSubMenuView() {
        return tvSubMenu;
    }

    public void setLeftLayoutPadding(int left, int top, int right, int bottom){
        leftLayout.setPadding(left,  top,  right,  bottom);
    }

    public void showLeftTitleDivider(Drawable divider) {
        leftLayout.setDividerDrawable(divider);
    }

    public void showRightTitleDivider(Drawable divider) {
        rightLayout.setDividerDrawable(divider);
    }

}
