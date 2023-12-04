package com.roger.main.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.commonlib.base.BaseFragment;
import com.kyleduo.switchbutton.SwitchButton;
import com.roger.main.R;
import com.roger.main.view.CenterImageSpan;
import com.roger.main.view.IActionDoneListener;
import com.roger.main.view.PressedEditView;

/**
 * 第一次调用花费时间较长，和文件大小有关，2M大小文件可达600ms。后续使用文件话费几乎不占时间
 * 2个sharePreference也只有前几次调用时间比较长
 * Create by Roger on 2019/10/18
 */
public class SwitchButtonFragment extends BaseFragment {

    SwitchButton switch_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_switch_button, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch_btn = view.findViewById(R.id.switch_btn);
        Drawable drawable = getContext().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0,0,40,40);
        SpannableStringBuilder ssb = new SpannableStringBuilder("  添加图片");
        ssb.setSpan(new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        switch_btn.setText(ssb, ssb);
    }

    @Override
    public void setView() {
        super.setView();
    }
}
