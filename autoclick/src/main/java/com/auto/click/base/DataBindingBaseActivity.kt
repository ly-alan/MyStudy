package com.auto.click.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @Author Roger
 * @Date 2022/9/6 9:22
 * @Description
 */
abstract class DataBindingBaseActivity<T : ViewDataBinding> : BaseActivity() {

    var mBinding: T? = null

    override fun setDataBinding(view: View) {
        if (mBinding == null) {
            mBinding = DataBindingUtil.bind(view)
            mBinding!!.lifecycleOwner = this
        }
        bindingViewModel();
    }

    open fun bindingViewModel() {

    }

    open fun getViewDataBinding(): T {
        return mBinding!!
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBinding != null) {
            mBinding!!.unbind()
        }
    }

}