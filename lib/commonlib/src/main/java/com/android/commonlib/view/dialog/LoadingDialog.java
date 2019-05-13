package com.android.commonlib.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.commonlib.R;


/**
 * 加载提示弹窗
 */
public class LoadingDialog extends Dialog {
    /**
     * 提示文本控件
     */
    TextView mTvMessage;
    /**
     *
     */
    ProgressBar mProgress;
    /**
     * 提示语
     */
    String mMessage;

    public LoadingDialog(Context context) {
        this(context, R.style.LoadingDialogStyle);
    }

    public LoadingDialog(Context context, String message) {
        this(context, R.style.LoadingDialogStyle);
        this.mMessage = message;
        setCanceledOnTouchOutside(true);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading_data);
        initView();
        setMessage(mMessage);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mProgress = (ProgressBar) findViewById(R.id.loading_bar);
        this.mTvMessage = (TextView) findViewById(R.id.tv_loading_data);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setMessage(String text) {
        if (text != null && !TextUtils.isEmpty(text)) {
            this.mMessage = text;
        } else {
            this.mMessage = "";
        }
        this.mTvMessage.setText(mMessage);
    }

}
