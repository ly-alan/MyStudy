package com.android.commonlib.base;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.android.commonlib.R;
import com.android.commonlib.view.custom.TitleBar;

/**
 * Created by yong.liao on 2018/3/29 0029.
 */
public abstract class TitleBarFragment extends BaseFragment {

    /**
     * 导航栏
     */
    private TitleBar mTitleBar;

    /**
     * 根布局
     */
    private ViewGroup mRootView;


    /**
     * 主布局ID
     */
    private int mContentLayoutID;

    /**
     * 主题资源ID
     */
    private int mStyleID;

    /**
     * 主布局
     */
    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //判断当前Fragment是否需要改变主题,即改变Titlebar颜色主题
        if (mStyleID > 0) {
            // 构建新主题的Context上下文环境对象
            Context themeContext = new ContextThemeWrapper(getActivity()
                    .getApplicationContext(), mStyleID);
            // 通过context构建新的inflater
            // 再通过应用新主题的inflater加载视图
            inflater = getActivity().getLayoutInflater().cloneInContext(themeContext);
        }
        mRootView = (ViewGroup) inflater.inflate(R.layout.base_activity, container, false);
        //初始化TitleBar控件
        mTitleBar = (TitleBar) mRootView.findViewById(R.id.titlebar_toolbar_activity);
        //判断是否映射解析布局文件，还是直接添加布局
        if (mContentLayoutID > 0) {
            //解析布局
            //初始化主布局
            ViewStub mContentLayout = (ViewStub) mRootView.findViewById(R.id.vsc_toolbar_activity_content);
            mContentLayout.setLayoutInflater(inflater);
            mContentLayout.setLayoutResource(mContentLayoutID);
            mContentView = mContentLayout.inflate();
        } else if (mContentView != null && null == mContentView.getParent()) {
            //直接添加布局
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.BELOW, mTitleBar.getId());
            mRootView.addView(mContentView, 2, layoutParams);
        }
        return mRootView;
    }

    /**
     * 设置布局(在onCreate中调用)
     *
     * @param resID 主布局ID
     */
    public void setContentView(int resID) {
        setContentView(resID, 0);
    }


    /**
     * 设置布局(在onCreate中调用)
     *
     * @param resID   主布局ID
     * @param styleID 主题资源ID
     */
    private void setContentView(int resID, int styleID) {
        this.mContentLayoutID = resID;
        this.mStyleID = styleID;
    }

    /**
     * 设置布局(在onCreate中调用)
     *
     * @param v 主布局
     */
    private void setContentView(View v) {
        setContentView(v, 0);
    }

    /**
     * 设置布局(在onCreate中调用)
     *
     * @param v       主布局
     * @param styleID 主题资源ID
     */
    private void setContentView(View v, int styleID) {
        if (v != null) {
            this.mContentView = v;
            this.mStyleID = styleID;
        }
    }

    /**
     * 获取TitleBar对象
     *
     * @return
     */
    public TitleBar getSupportTitleBar() {
        return mTitleBar;
    }

    public ViewGroup getRootView() {
        return mRootView;
    }
}
