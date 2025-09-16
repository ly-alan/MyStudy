package com.example.testjetpack.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class MVVMBaseFragment<VM : BaseViewModel, VB : ViewDataBinding> : Fragment() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    private var fragmentProvider: ViewModelProvider? = null
    private var isLazyLoad = false // 是否已经懒加载

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 获取布局ID
        val layoutId = getLayoutId()
        return if (layoutId != 0) {
            // 使用传统布局方式
            inflater.inflate(layoutId, container, false)
        } else {
            // 使用 DataBinding 方式（需要在 configUI 中初始化）
            null
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configUI(view)
    }

    private fun configUI(view: View) {
        // 初始化 DataBinding
        binding =
            DataBindingUtil.bind(view) ?: throw IllegalStateException("Binding cannot be null")
        binding.lifecycleOwner = viewLifecycleOwner

        // 初始化 ViewModel
        initViewModel()

        // 设置绑定变量
        binding.setVariable(getBindingVariable(), viewModel)
        binding.executePendingBindings()

        // 初始化视图
        initView()

        // 处理懒加载
        if (allowLazyLoad()) {
            if (userVisibleHint && !isLazyLoad) {
                isLazyLoad = true
                initData()
            }
        } else {
            initData()
        }

        // 设置观察者
        initObserver()
    }

    /**
     * 初始化 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun initViewModel() {
        val viewModelClass = getViewModelClass()
        viewModel = obtainViewModel(viewModelClass, useActivityScope())

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
                    when (typeArgument) {
                        is Class<*> -> {
                            if (BaseViewModel::class.java.isAssignableFrom(typeArgument)) {
                                return typeArgument as Class<VM>
                            }
                        }

                        is ParameterizedType -> {
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
            }
            clazz = clazz.superclass
        }
        throw IllegalArgumentException("Cannot find ViewModel class")
    }

    /**
     * 通过 ViewModelProvider 获取 ViewModel
     */
    protected fun <T : ViewModel> obtainViewModel(
        modelClass: Class<T>,
        useActivityScope: Boolean = false
    ): T {
        return if (useActivityScope) {
            ViewModelProvider(requireActivity()).get(modelClass)
        } else {
            if (fragmentProvider == null) {
                fragmentProvider = ViewModelProvider(this)
            }
            fragmentProvider!!.get(modelClass)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::viewModel.isInitialized) {
            lifecycle.removeObserver(viewModel as LifecycleObserver)
        }
        if (::binding.isInitialized) {
            binding.unbind()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentProvider = null
    }

    /**
     * 设置用户可见提示（用于懒加载）
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (view != null && allowLazyLoad() && isVisibleToUser && !isLazyLoad) {
            isLazyLoad = true
            initData()
        }
    }

    // ========== 抽象方法 ==========
    /**
     * 获取布局资源ID
     * 返回 0 表示使用 DataBinding，返回有效的 layoutId 表示使用传统布局
     */
    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     * 获取 DataBinding 的变量ID (BR.viewModel)
     */
    protected abstract fun getBindingVariable(): Int

    /**
     * 初始化视图
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化观察者
     */
    protected abstract fun initObserver()

    // ========== 可重写的方法 ==========

    /**
     * 是否允许懒加载（默认false）
     */
    protected open fun allowLazyLoad(): Boolean = false

    /**
     * 是否使用 Activity 范围的 ViewModel（数据共享，默认false）
     */
    protected open fun useActivityScope(): Boolean = false
}