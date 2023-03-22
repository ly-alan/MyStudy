package com.auto.click.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.auto.click.R
import com.auto.click.base.BaseActivity
import com.auto.click.floating.FloatingWindowService
import com.auto.click.service.AccessibilityHelper
import com.auto.click.service.AutoTouchService
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @Author Roger
 * @Date 2022/9/6 9:06
 * @Description
 */
class MainActivity : BaseActivity() {


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
        tv_select_pkg.setOnClickListener {

        }
        tv_start.setOnClickListener {
            checkState()
        }
        tv_stop.setOnClickListener {

        }
    }


    override fun onResume() {
        super.onResume()
        checkState();
    }

    /**
     * 开启悬浮窗
     */
    private fun openFloatDialog() {
        startService(Intent(baseContext, FloatingWindowService::class.java))
    }


    /**
     * 检查权限
     */
    private fun checkState() {
        val hasAccessibility: Boolean = AccessibilityHelper.checkAccessibilityOpen(
            baseContext,
            AutoTouchService::class.java
        )
        if (!hasAccessibility) {
            showRequestAccessibilityDialog()
            return
        }
        val hasWinPermission: Boolean = Settings.canDrawOverlays(this)
        if (!hasWinPermission) {
            requestPermissionAndShow();
            return
        }
        openFloatDialog()
    }

    /**
     * 开启无障碍服务
     */
    private fun showRequestAccessibilityDialog() {
        AlertDialog.Builder(this).setTitle("无障碍服务未开启")
            .setMessage(getString(R.string.app_name) + " 未开启无障碍服务，无法正常使用")
            .setPositiveButton("去开启") { dialog, which -> // 显示授权界面
                //去设置页面
                AccessibilityHelper.jumpToAccessibilitySetting(baseContext)
            }
            .setNegativeButton("取消", null).show()
    }


    /**
     * 开启悬浮窗权限
     */
    private fun requestPermissionAndShow() {
        AlertDialog.Builder(this).setTitle("悬浮窗权限未开启")
            .setMessage(getString(R.string.app_name) + " 获得悬浮窗权限，才能正常使用应用")
            .setPositiveButton("去开启") { dialog, which -> // 显示授权界面
                settingLauncher.launch(
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                )
            }
            .setNegativeButton("取消", null).show()
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
}