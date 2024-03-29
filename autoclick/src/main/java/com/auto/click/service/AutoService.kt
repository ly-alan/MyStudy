package com.auto.click.service

import android.util.ArraySet
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.auto.click.function.ScanListUtils

/**
 * @Author Roger
 * @Date 2022/9/6 10:51
 * @Description 自动服务
 */
class AutoService : BaseAccessibilityService() {

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
//        //代码中动态设置监控对象
//        Log.d("liao","onServiceConnected")
//        var serviceInfo = AccessibilityServiceInfo()
//        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
//        serviceInfo.packageNames = arrayOf("com.auto.click")
//        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
//        setServiceInfo(serviceInfo)
//    }

    var setList: ArraySet<String>? = ArraySet<String>();

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        super.onAccessibilityEvent(event)
//        Log.d("liao", event.toString());
        when (event!!.eventType) {
            //窗口状态改变
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {}
            //通知状态改变
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {}
//            view的点击、长按、滑动事件如果要接收到信息，需要对应的事件被消费了才会有响应，
//            如：你点击、长按一个不能点击的控件，则这里也不会受到对应的事件响应
            //点击
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
//                setList = ScanListUtils.openList(event)
                Toast.makeText(baseContext, "触发点击，开始记录扫描列表数据", Toast.LENGTH_LONG).show()
            }
            //长按
            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {
                Toast.makeText(baseContext, "长按列表数据", Toast.LENGTH_LONG).show()
            }
            //滑动
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {}
            //焦点变化
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> {
                ScanListUtils.scanFocusListText(event, setList);
            }
        }
    }

    override fun onInterrupt() {
    }
}