package com.roger.workmanager;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Create by Roger on 2020/1/8
 */
public class MainActivity extends Activity {

    Handler handler = new Handler();
    //执行间隔
    private final int delayMill = 10000;

    boolean wakeScreen = false;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("liao", "countDown : " + wakeScreen);
            if (System.currentTimeMillis() >= actionDoTime) {
                if (wakeScreen) {
                    if (startApp()) {
                        //先要点亮屏幕后再启动
                        //成功后，停止循环
                        return;
                    }
                }
                if (!wakeScreen) {
//                    //方法1.直接点亮屏幕
//                    wakeUpScreen(MainActivity.this);
                    // 方法2：设置窗口标志，允许在锁屏界面显示并点亮屏幕
                    wakeUpAndUnlock();
//                    // 方法3：解锁设备（Android 8.0+）
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        UnlockHelper.unlockDevice(MainActivity.this);
//                    }
                    wakeScreen = true;
                }
            } else {
                showProgress();
            }
            handler.postDelayed(runnable, delayMill);
        }
    };

    //开启倒计时的时间
    private long actionDoTime;//执行时间

    EditText etDelay;
    View btnStart;

    TextView tvTips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        actionDoTime = getTodayNinePmTimestamp();
        handler.postDelayed(runnable, delayMill);
    }

    private void initViews() {
        tvTips = findViewById(R.id.tv_state);
        etDelay = findViewById(R.id.et_delay_min);
        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTips.setText("start countdown");
                actionDoTime = getInputText();
                wakeScreen = false;
            }
        });
    }

    private long getInputText() {
        long nineTime = getTodayNinePmTimestamp();
        try {
            String inputText = etDelay.getText().toString();
            int num = Integer.parseInt(inputText);
            return Math.min(nineTime, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(num));
        } catch (Exception e) {
            return nineTime;
        }
    }

    private void showProgress() {
        try {
            Calendar actionTime = Calendar.getInstance();
            actionTime.setTimeInMillis(actionDoTime);
            long hoursRemaining = TimeUnit.MILLISECONDS.toHours(actionDoTime - System.currentTimeMillis());
            long minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(actionDoTime - System.currentTimeMillis()) % 60;
            long secondRemaining = TimeUnit.MILLISECONDS.toSeconds(actionDoTime - System.currentTimeMillis()) % 60;
            tvTips.setText("Target time : " + String.format("%02d:%02d", actionTime.get(Calendar.HOUR_OF_DAY), actionTime.get(Calendar.MINUTE))
                    + "\n"
                    + "Remaining time : " + String.format("%02dh:%02dm:%02ds", hoursRemaining, minutesRemaining, secondRemaining));
        } catch (Exception e) {
            tvTips.setText("show tips error : " + e.getMessage());
        }
    }


    public long getTodayNinePmTimestamp() {
        Calendar calendar = Calendar.getInstance();

        // 设置为当天晚上9点
        calendar.set(Calendar.HOUR_OF_DAY, 21); // 24小时制，21点=晚上9点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        // 如果当前时间已经超过晚上9点，就获取第二天的晚上9点
//        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }

        return calendar.getTimeInMillis();
    }

    private boolean startApp() {
        try {
            // 方式1：通过包名启动
            String packageName = "com.larksuite.suite";
//            String packageName = "com.github.shadowsocks.tv";
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);

            if (launchIntent != null) {
                startActivity(launchIntent);
                tvTips.setText("doAction success : ");
                return true;
            } else {
                tvTips.setText("no install apk ");
            }
        } catch (Exception e) {
            tvTips.setText("doAction fail : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    private void wakeUpAndUnlock() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
            setShowWhenLocked(true);

            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null) {
                keyguardManager.requestDismissKeyguard(this, null);
            }
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            );
        }
    }

    /**
     * 点亮屏幕
     */
    public static void wakeUpScreen(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if (powerManager != null) {
            // 获取WakeLock
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "MyApp:ScreenWakeLock"
            );

            // 点亮屏幕（保持1分钟）
            wakeLock.acquire(60 * 1000);

            // 在合适的时机释放
            new Handler().postDelayed(() -> {
                if (wakeLock.isHeld()) {
                    wakeLock.release();
                }
            }, 1000); // 1秒后释放
        }
    }

}
