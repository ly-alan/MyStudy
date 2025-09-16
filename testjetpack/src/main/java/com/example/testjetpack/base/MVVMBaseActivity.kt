package com.example.testjetpack.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class MVVMBaseActivity<VM : BaseViewModel, VB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    private var mActivityProvider: ViewModelProvider? = null

    /**
     * 获取布局资源ID
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 获取 DataBinding 的变量ID (BR.viewModel)
     */
    protected abstract fun getBindingVariable(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化 DataBinding
        initDataBinding()

        // 调用初始化方法
        initView()
        initData()
        initObserver()
    }

    /**
     * 初始化 DataBinding
     */
    private fun initDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        // 关键：设置 lifecycleOwner，否则 LiveData 不会自动更新UI
        binding.lifecycleOwner = this

        // 初始化 ViewModel
        initViewModel()

        // 设置绑定变量
        binding.setVariable(getBindingVariable(), viewModel)
        binding.executePendingBindings()
    }

    /**
     * 初始化 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        val viewModelClass = getViewModelClass()
        viewModel = obtainViewModel(viewModelClass)
        // 添加生命周期观察
        lifecycle.addObserver(viewModel)
    }

    /**
     * 通过反射获取 ViewModel Class
     */
    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<VM> {
        var clazz: Class<*> = this.javaClass
        while (clazz != Any::class.java) {
            (clazz.genericSuperclass as? ParameterizedType)?.let { type ->
                type.actualTypeArguments.forEach { typeArgument ->
                    if (typeArgument is Class<*> && BaseViewModel::class.java.isAssignableFrom(
                            typeArgument
                        )
                    ) {
                        return typeArgument as Class<VM>
                    } else if (typeArgument is ParameterizedType) {
                        val rawType = typeArgument.rawType
                        if (rawType is Class<*> && BaseViewModel::class.java.isAssignableFrom(
                                rawType
                            )
                        ) {
                            return rawType as Class<VM>
                        }
                    }
                }
            }
            clazz = clazz.superclass
        }
        throw IllegalArgumentException("Cannot find ViewModel class")
    }

    /**
     * 通过 ViewModelProvider 获取 ViewModel
     */
    protected fun <T : ViewModel> obtainViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider!!.get(modelClass)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::viewModel.isInitialized) {
            lifecycle.removeObserver(viewModel)
        }
        if (::binding.isInitialized) {
            binding.unbind()
        }
    }

    // 抽象方法
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initObserver()

}