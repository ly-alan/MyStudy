package com.auto.click.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * @Author Roger
 * @Date 2022/9/6 10:36
 * @Description
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: View = LayoutInflater.from(this).inflate(getLayoutId(), null)
        setContentView(view)
        setDataBinding(view)
        configUI(view)
    }

    open fun setDataBinding(view: View) {

    }


    abstract fun getLayoutId(): Int

    abstract fun configUI(view: View)

}