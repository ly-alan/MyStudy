package com.example.testjetpack.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * MVVM 基础 ViewModel，继承 AndroidViewModel
 * 提供应用上下文访问和协程管理
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application){

    // 获取应用上下文
    protected val appContext: Application get() = getApplication()

    // 协程错误处理器
    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleException(throwable)
    }

    /**
     * 启动协程并自动处理异常
     */
    protected fun launchCoroutine(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(exceptionHandler, block = block)
    }

    /**
     * 安全启动协程（带加载状态）
     */
    protected fun launchSafe(
        showLoading: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        if (showLoading) {
            // 可以在这里触发加载状态
        }
        return launchCoroutine {
            try {
                block()
            } catch (e: Exception) {
                handleException(e)
            } finally {
                if (showLoading) {
                    // 可以在这里隐藏加载状态
                }
            }
        }
    }

    /**
     * 异常处理，子类可重写
     */
    protected open fun handleException(throwable: Throwable) {
        throwable.printStackTrace()
        // 可以在这里处理全局异常，如网络错误、数据解析错误等
    }

    /**
     * 清理资源
     */
    override fun onCleared() {
        super.onCleared()
    }
}