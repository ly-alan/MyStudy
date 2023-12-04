package com.roger.main.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.roger.main.R;

/**
 * @Author Roger
 * @Date 2023/12/4 17:01
 * @Description
 */
public class CombinedEditTextView extends LinearLayout {

    TextView mTvResult;
    View mLayoutCombined;
    EditText mEt1, mEt2;
    TextView mTv1, mTv2;


    public CombinedEditTextView(Context context) {
        super(context);
        init();
    }

    public CombinedEditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CombinedEditTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
        View v = mLayoutInflater.inflate(R.layout.combined_text_view, null, false);
        addView(v);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        mTvResult = findViewById(R.id.tv_combined_result);
        mLayoutCombined = findViewById(R.id.layout_combined);
        mTv1 = findViewById(R.id.tv_combined_1);
        mTv2 = findViewById(R.id.tv_combined_2);
        mEt1 = findViewById(R.id.et_combined_1);
        mEt2 = findViewById(R.id.et_combined_2);
        showEditLayout(false);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mTvResult.getVisibility() == VISIBLE) {
                    showEditLayout(true);
                }
                return false;
            }
        });
    }

    private void showEditLayout(boolean showEdit) {
        if (showEdit) {
            mTvResult.setVisibility(GONE);
            mLayoutCombined.setVisibility(VISIBLE);
        } else {
            mTvResult.setVisibility(VISIBLE);
            mLayoutCombined.setVisibility(GONE);
        }
    }


}
