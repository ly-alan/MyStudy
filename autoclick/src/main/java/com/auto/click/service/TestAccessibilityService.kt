package com.auto.click.service

import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 * @Author Roger
 * @Date 2022/9/6 10:51
 * @Description
 */
class TestAccessibilityService : BaseAccessibilityService() {

    /*内部类单利*/
//    companion object {
//        val instance = SingletonHolder.holder
//    }
//
//    private object SingletonHolder {
//        val holder = TestAccessibilityService()
//    }

//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        var serviceInfo = AccessibilityServiceInfo()
//        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
//        serviceInfo.packageNames = arrayOf("com.test")
//        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
//        setServiceInfo(serviceInfo)
//    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        super.onAccessibilityEvent(event)
        Log.d("liao", event.toString());
        when (event!!.eventType) {
            //窗口状态改变
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {}
            //通知状态改变
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {}
//            view的点击、长按、滑动事件如果要接收到信息，需要对应的事件被消费了才会有响应，
//            如：你点击、长按一个不能点击的控件，则这里也不会受到对应的事件响应
            //点击
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {}
            //长按
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {}
            //滑动
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {}
        }
    }

    override fun onInterrupt() {
    }
}