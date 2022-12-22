package com.auto.click.floating

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.provider.Settings
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.auto.click.R

/**
 * @Author Roger
 * @Date 2022/9/9 9:59
 * @Description 桌面的悬浮弹窗
 */
class FloatingWindowService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var tvContent: AppCompatTextView
    private lateinit var handler: Handler

    private var receiver: MyReceiver? = null
    private var floatingView: View? = null
    private val stringBuilder = StringBuilder()

    private var x = 0
    private var y = 0

    // 用来判断floatingView是否attached 到 window manager，防止二次removeView导致崩溃
    private var attached = false

    override fun onCreate() {
        super.onCreate()
        // 注册广播
        receiver = MyReceiver()
        val filter = IntentFilter()
        filter.addAction("android.intent.action.MyReceiver")
        registerReceiver(receiver, filter);

        // 获取windowManager并设置layoutParams
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.RGBA_8888
//            format = PixelFormat.TRANSPARENT
            gravity = Gravity.START or Gravity.TOP
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = resources.getDimensionPixelOffset(R.dimen.float_dialog_width)
            height = resources.getDimensionPixelOffset(R.dimen.float_dialog_height)
            x = 300
            y = 300
        }
        handler = Handler(this.mainLooper) { msg ->
            tvContent.text = msg.obj as String
            // 当文本超出屏幕自动滚动，保证文本处于最底部
            val offset = tvContent.lineCount * tvContent.lineHeight
            floatingView?.apply {
                if (offset > height) {
                    tvContent.scrollTo(0, offset - height)
                }
            }
            false
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Settings.canDrawOverlays(this)) {
            floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_window, null)
            tvContent = floatingView!!.findViewById(R.id.tv_content)
//            floatingView!!.findViewById<AppCompatImageView>(R.id.iv_close).setOnClickListener {
//                stringBuilder.clear()
//                windowManager.removeView(floatingView)
//                attached = false
//            }
            // 设置TextView滚动
            tvContent.movementMethod = ScrollingMovementMethod.getInstance()

            floatingView!!.findViewById<FrameLayout>(R.id.layout_drag).setOnTouchListener(
                object : View.OnTouchListener {
                    var touchTime: Long = 0;
                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                touchTime = System.currentTimeMillis();
                                x = event.rawX.toInt()
                                y = event.rawY.toInt()
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val currentX = event.rawX.toInt()
                                val currentY = event.rawY.toInt()
                                val offsetX = currentX - x
                                val offsetY = currentY - y
                                x = currentX
                                y = currentY
                                layoutParams.x = layoutParams.x + offsetX
                                layoutParams.y = layoutParams.y + offsetY
                                windowManager.updateViewLayout(floatingView, layoutParams)
                            }
                            MotionEvent.ACTION_UP -> {
                                if (touchTime > 0 && System.currentTimeMillis() - touchTime < 300) {
                                    //点击
                                    clickFloatDialog()
                                }
                            }
                        }
                        return true
                    }
                })


            windowManager.addView(floatingView, layoutParams)
            attached = true
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun clickFloatDialog() {
        if (TextUtils.equals(tvContent.text, getString(R.string.float_dialog_tv_close))) {
            tvContent.text = getString(R.string.float_dialog_tv_open)
            //开始
            Toast.makeText(this, getString(R.string.float_dialog_tv_open), Toast.LENGTH_SHORT).show()
        } else {
            tvContent.text = getString(R.string.float_dialog_tv_close)
        }
    }

    override fun onDestroy() {
        // 注销广播并删除浮窗
        unregisterReceiver(receiver)
        receiver = null
        if (attached) {
            windowManager.removeView(floatingView)
        }
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val content = intent.getStringExtra("content") ?: ""
            stringBuilder.append(content).append("\n")
            val message = Message.obtain()
            message.what = 0
            message.obj = stringBuilder.toString()
            handler.sendMessage(message)
        }
    }
}