package com.android.commonlib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.commonlib.view.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by yong.liao on 2018/3/29 0029.
 */
public class BaseFragment extends Fragment {

    /**
     * 进度提示弹窗
     */
    LoadingDialog mLoadingDialog;

    public BaseFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //注册EventBus
        if (isUserEvent()) {
            EventBus.getDefault().register(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setView();
        setListener();
    }

    public void initView() {
    }

    public void setView() {
    }

    public void setListener() {
    }

    public boolean isUserEvent() {
        return false;
    }

    public void showLoadingDialog() {
        showLoadingDialog(true);
    }

    public void showLoadingDialog(boolean touchOutsideCancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
        }
        mLoadingDialog.setCanceledOnTouchOutside(touchOutsideCancelable);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销EventBus
        if (isUserEvent()) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void postEventData(Object object) {
        if (null != object) {
            EventBus.getDefault().post(object);
        }
    }
}
