package com.auto.click.ui.splash

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.auto.click.R
import com.auto.click.base.BaseActivity
import com.auto.click.service.AccessibilityHelper
import com.auto.click.service.AutoService
import com.auto.click.ui.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @Author Roger
 * @Date 2023/3/10 16:44
 * @Description
 */
class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash;
    }

    override fun onResume() {
        super.onResume()
        checkState()
    }

    override fun configUI(view: View) {
        tv_step.setOnClickListener(View.OnClickListener {
            when (tv_step.text) {
                getString(R.string.open_access_service) -> {
                    //去设置页面
                    AccessibilityHelper.jumpToAccessibilitySetting(baseContext)
                }

                getString(R.string.open_float_window_service) -> {
                    startWindow()
                }

                getString(R.string.start_user) -> {
                    startActivity(Intent(baseContext, MainActivity::class.java))
                    finish()
                }
                else -> {}
            }
        })
    }

    /**
     * 检查权限
     */
    private fun checkState() {
        val hasAccessibility: Boolean = AccessibilityHelper.checkAccessibilityOpen(
            baseContext,
            AutoService::class.java
        )
        if (!hasAccessibility) {
            tv_step.setText(R.string.open_access_service)
            return
        }
        val hasWinPermission: Boolean = Settings.canDrawOverlays(this)
        if (!hasWinPermission) {
            tv_step.setText(R.string.open_float_window_service)
        } else {
            tv_step.setText(R.string.start_user)
        }
    }


    /**
     * 跳转设置悬浮弹窗权限
     */
    private var settingLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                if (it.resultCode == 0) {
                    if (Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, "悬浮窗权限授权成功", Toast.LENGTH_SHORT).show()
                    }
                }
            });

    /**
     * 开启悬浮窗
     */
    private fun startWindow() {
        if (!Settings.canDrawOverlays(this)) {
            settingLauncher.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
        }
    }

}