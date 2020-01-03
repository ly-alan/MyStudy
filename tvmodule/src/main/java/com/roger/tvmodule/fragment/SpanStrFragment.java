package com.roger.tvmodule.fragment;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;

/**
 * Create by Roger on 2019/6/4
 */
public class SpanStrFragment extends BaseFragment {

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container,false);
    }

    @Override
    public void initView() {
        super.initView();
        textView = getView().findViewById(R.id.main_text);
        showTextView();
    }

    private void showTextView() {
        String content = "测试点击事件的完美执行测试点击事件的完美执行测试点击事件的完美执行测试点击事件的完美执行";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(content);

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Log.d("roger", "click  1");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //设置超链接文本颜色
                ds.setColor(getResources().getColor(android.R.color.white));
                ds.setUnderlineText(true);
//                ds.clearShadowLayer();
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                view.invalidate();
                Log.d("roger", "click  2");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //设置超链接文本颜色
                ds.setColorFilter(new LightingColorFilter(0xFFFF00FF, 0x0000FF00));
                ds.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));
                ds.setUnderlineText(true);
                ds.clearShadowLayer();
            }

        };
        //文本可点击，有点击事件
        stringBuilder.setSpan(clickableSpan1, 1, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(clickableSpan2, 5, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.focus_white2yellow)), 5, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置此方法后，点击事件才能生效
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(getResources().getColor(R.color.translate));
        textView.setText(stringBuilder);
    }
}
