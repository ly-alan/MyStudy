package com.android.commonlib.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 首页tab也进行懒加载数据，第一次显示时才加载数据
 * Created by liaoyong on 2017/10/17.
 */

public abstract class LazyFragment extends BaseFragment {
    /**
     * 布局根目录
     */
    private View mContentView;
    /**
     * 是否可见状态
     */
    private boolean isFragmentVisible;
    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;
    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;
    /**
     * <pre>
     *
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    private boolean forceLoad = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirstLoad = true;
        mContentView = setContentView(inflater, container, savedInstanceState);
        isPrepared = true;
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lazyLoad();
    }

    /**
     * 根部结局
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

    /**
     * 设置布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected final void onVisible() {
        isFragmentVisible = true;
        onFragmentVisibleChange(isFirstLoad, true);
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected final void onInvisible() {
        isFragmentVisible = false;
        onFragmentVisibleChange(isFirstLoad, false);
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected final void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                firstVisible();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }

    /**
     * 已经初始化完成
     *
     * @return
     */
    public final boolean isPrepared() {
        return isPrepared;
    }

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public final void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    public final boolean isFirstLoad() {
        return isFirstLoad;
    }

    public final boolean isFragmentVisible() {
        return isFragmentVisible;
    }

    /**
     * fragment第一次可见时调用
     * 在该方法中,进行数据加载的操作
     */
    public abstract void firstVisible();

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isFirstLoad 第一次加载
     * @param isVisible   true  不可见 -> 可见
     *                    false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isFirstLoad, boolean isVisible) {

    }

}
