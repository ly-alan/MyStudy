package com.auto.click.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @Author Roger
 * @Date 2022/9/6 9:30
 * @Description
 */
abstract class MVVMBaseActivity<VM : BaseViewModel, T : ViewDataBinding> : DataBindingBaseActivity<T>() {

    var mViewModel: VM? = null

    override fun bindingViewModel() {
        initViewModel();
        mBinding!!.setVariable(getBindingVariable(), mViewModel)
        mBinding!!.executePendingBindings()
    }

    open fun initViewModel() {
        mViewModel = this.createViewModel()
        if (mViewModel == null) {
            mViewModel = ViewModelProvider(this).get(getVMClass())
        }
    }

    private fun getVMClass(): Class<VM> {
        var cls: Class<*>? = javaClass
        var vmClass: Class<VM>? = null
        while (vmClass == null && cls != null) {
            vmClass = getVMClass(cls)
            cls = cls.superclass
        }
        if (vmClass == null) {
            vmClass = BaseViewModel::class as Class<VM>
        }
        return vmClass
    }

    private fun getVMClass(cls: Class<*>): Class<VM>? {
        val type = cls.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (t in types) {
                if (t is Class<*>) {
                    val vmClass = t as Class<VM>
                    if (BaseViewModel::class.java.isAssignableFrom(vmClass)) {
                        return vmClass
                    }
                } else if (t is ParameterizedType) {
                    val rawType = t.rawType
                    if (rawType is Class<*>) {
                        val vmClass = rawType as Class<VM>
                        if (BaseViewModel::class.java.isAssignableFrom(vmClass)) {
                            return vmClass
                        }
                    }
                }
            }
        }
        return null
    }


    open fun getViewModel(): VM {
        return mViewModel!!
    }


    open fun createViewModel(): VM? {
        return null
    }

    protected abstract fun getBindingVariable(): Int


}