package com.auto.click.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.auto.click.callback.ICallBack

/**
 * @Author Roger
 * @Date 2022/9/6 14:48
 * @Description 封装一些常见的操作
 */

open class BaseAccessibilityService : AccessibilityService() {


    override fun onCreate() {
        super.onCreate()
        AccessibilityServiceHelper.setCallBack(object : ICallBack {
            override fun call(obj: Any) {

            }
        })
    }

/*懒汉式单利*/
//    companion object {
//        private var instance: TestAccessibilityService? = null
//            get() {
//                if (field == null) {
//                    field = TestAccessibilityService()
//                }
//                return field
//            }
//
//        @Synchronized
//        fun get(): TestAccessibilityService {
//            return instance!!;
//        }
//    }

/*双检索单例*/
//    companion object {
//        val instance: TestAccessibilityService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            TestAccessibilityService()
//        }
//    }

    var hasSend: Boolean = false;

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onInterrupt() {

    }


    /**
     * 模拟点击事件
     *
     * @param nodeInfo nodeInfo
     */
    open fun performViewClick(nodeInfo: AccessibilityNodeInfo?) {
        nodeInfo ?: return
        var node = nodeInfo
        while (node != null) {
            if (node.isClickable) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                break
            }
            node = node.parent
        }
    }

    /**
     * 模拟返回操作
     */
    open fun performBackClick() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    /**
     * 模拟下滑操作
     */
    open fun performScrollBackward() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
    }

    /**
     * 模拟上滑操作
     */
    open fun performScrollForward() {
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    }

    /**
     * 查找对应文本的View
     *
     * @param text text
     * @return View
     */
    open fun findViewByText(text: String?): AccessibilityNodeInfo? {
        return findViewByText(text, false)
    }

    /**
     * 查找对应文本的View
     *
     * @param text      text
     * @param clickable 该View是否可以点击
     * @return View
     */
    open fun findViewByText(text: String?, clickable: Boolean): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null && nodeInfo.isClickable == clickable) {
                    return nodeInfo
                }
            }
        }
        return null
    }

    /**
     * 查找对应ID的View
     *
     * @param id id
     * @return View
     */
    open fun findViewByID(id: String?): AccessibilityNodeInfo? {
        val accessibilityNodeInfo = rootInActiveWindow ?: return null
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo
                }
            }
        }
        return null
    }

    open fun clickTextViewByText(text: String?) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo)
                    break
                }
            }
        }
    }

    open fun clickTextViewByID(id: String?) {
        val accessibilityNodeInfo = rootInActiveWindow ?: return
        val nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id)
        if (nodeInfoList != null && nodeInfoList.isNotEmpty()) {
            for (nodeInfo in nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo)
                    break
                }
            }
        }
    }

    /**
     * 模拟输入
     *
     * @param nodeInfo nodeInfo
     * @param text     text
     */
    open fun inputText(nodeInfo: AccessibilityNodeInfo, text: String?) {
        val arguments = Bundle()
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
    }


    /**
     * 点击屏幕的坐标，可以看说明https://blog.csdn.net/qq_24641847/article/details/77683826
     * GestureDescription是api24（7.0）才引入的，所以app最小版本我也设置了24
     *  x,y为相对屏幕左上角的坐标
     */
    open fun clickScreenPoint(x: Float, y: Float) {
        Log.d("liao", "click position  $x  :  $y")
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        //        Path: 要跟随的路径。必须有正好一个轮廓。路径的边界不得为负数。路径不得为空。如果路径的长度为零 (例如, 单个moveTo()), 则笔划是一个没有移动的点击
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        dispatchGesture(builder.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                Log.d("liao", "click failed")
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
                Log.d("liao", "click complete")
            }
        }, null)
    }

}