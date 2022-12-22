package com.auto.click.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.auto.click.R
import com.auto.click.base.DataBindingBaseActivity
import com.auto.click.databinding.ActivityMainBinding
import com.auto.click.floating.FloatingWindowService
import com.auto.click.service.AccessibilityServiceHelper
import com.auto.click.ui.applist.AppListActivity
import com.auto.click.utils.Utils

/**
 * @Author Roger
 * @Date 2022/9/6 9:06
 * @Description
 */
class MainActivity : DataBindingBaseActivity<ActivityMainBinding>() {

    /**
     * 跳转设置悬浮弹窗权限
     */
    private var settingLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode == 0) {
                    if (Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, "悬浮窗权限授权成功", Toast.LENGTH_SHORT).show()
                        startService(Intent(this@MainActivity, FloatingWindowService::class.java))
                    }
                }
            });

    /**
     * 选择监控的app
     */
    private var appListLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode == 0) {

                }
            }
        )

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun configUI(view: View) {
        mBinding?.tvSetting?.setOnClickListener {
            if (!AccessibilityServiceHelper.checkAccessibilityEnabled(
                    baseContext,
                    getString(R.string.description_in_xml)
                )
            ) {
                //去设置页面
                AccessibilityServiceHelper.jumpToAccessibilitySetting(baseContext)
            }
        }
        mBinding?.tvStart?.setOnClickListener {
            startWindow(it);
        }
        mBinding?.tvStop?.setOnClickListener {
            appListLauncher.launch(Intent(this, AppListActivity::class.java))
        }
    }

    /**
     * 开启悬浮窗
     */
    private fun startWindow(view: View?) {
        if (!Settings.canDrawOverlays(this)) {
            settingLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        } else {
            startService(Intent(this@MainActivity, FloatingWindowService::class.java))
        }
    }


}