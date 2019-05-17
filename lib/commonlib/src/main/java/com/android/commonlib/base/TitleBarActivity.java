package com.android.commonlib.base;

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
public class TitleBarActivity extends BaseActivity {

    /**
     * 包含TitleBar的根布局
     */
    private ViewGroup mRootView;
    /**
     * 主布局
     */
    private View mContentView;

    /**
     * TitleBar控件
     */
    private TitleBar mTitleBar;

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(initContentView(v, 0, 0, false), params);
    }

    @Override
    public void setContentView(int id) {
        setContentView(id, 0, false);
    }

    @Override
    public void setContentView(View v) {
        setContentView(v, 0, false);
    }

    /**
     * 加载布局
     *
     * @param id               布局ID
     * @param isToolBarLinkage ToolBar是否联动
     */
    public void setContentView(int id, int styleID, boolean isToolBarLinkage) {
        super.setContentView(initContentView(null, id, styleID, isToolBarLinkage));
    }

    public void setContentView(View v, int styleID, boolean isToolBarLinkage) {
        super.setContentView(initContentView(v, 0, styleID, isToolBarLinkage));
    }

    /**
     * 初始化布局
     *
     * @param v
     * @param styleID
     * @param isToolBarLinkage
     * @return
     */
    private View initContentView(View v, int layoutID, int styleID, boolean isToolBarLinkage) {
        //初始化布局加载适配器
        LayoutInflater inflater = getLayoutInflater();
        mRootView = (ViewGroup) inflater.inflate(R.layout.base_activity, null);
        //初始化TitleBar
        mTitleBar = (TitleBar) mRootView.findViewById(R.id.titlebar_toolbar_activity);
        setSupportActionBar(mTitleBar);
        //加载主布局
        //判断是否映射解析布局文件，还是直接添加布局
        if (layoutID > 0) {
            //解析主布局
            //主布局容器
            ViewStub mContentLayout = (ViewStub) mRootView.findViewById(R.id.vsc_toolbar_activity_content);
            mContentLayout.setLayoutInflater(inflater);
            mContentLayout.setLayoutResource(layoutID);
            mContentView = mContentLayout.inflate();
        } else if (v != null) {
            //直接添加布局
            mContentView = v;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.BELOW, mTitleBar.getId());
            mRootView.addView(mContentView, 2, layoutParams);
        }
        return mRootView;
    }

    /**
     * 获取TitleBar对象
     *
     * @return
     */
    public TitleBar getSupportTitleBar() {
        return mTitleBar;
    }

    /**
     * 获取根布局
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }
}
