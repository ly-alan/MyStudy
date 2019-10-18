package com.roger.main.fragment;

import android.util.Log;

import com.android.commonlib.base.BaseFragment;
import com.android.commonlib.utils.SPUtils;

/**
 * 第一次调用花费时间较长，和文件大小有关，2M大小文件可达600ms。后续使用文件话费几乎不占时间
 * 2个sharePreference也只有前几次调用时间比较长
 * Create by Roger on 2019/10/18
 */
public class SharePreferenceFragment extends BaseFragment {
    @Override
    public void setView() {
        super.setView();
        long lastTime;
        for (int i = 0; i < 10; i++) {
            lastTime = System.currentTimeMillis();
            SPUtils.putString("key_" + i, "sjaksaaaaaaddddddddddddddjk" +
                    "ikikikikikikikikikikikikikikikikindddddddddddddddd" +
                    "ikikikikikikikikikikikikikikikikindddddddddddddddd" +
                    "ikikikikikikikikikikikikikikikikindddddddddddddddd" +
                    "dddddddkwijuhnbvdsfurfvbgesyu");
            Log.i("liao", "put = " + String.valueOf(System.currentTimeMillis() - lastTime));
            lastTime = System.currentTimeMillis();
            SPUtils.putString("test2", "key_" + i, "");
            Log.i("liao", "put 2 =" + String.valueOf(System.currentTimeMillis() - lastTime));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            lastTime = System.currentTimeMillis();
            SPUtils.getString("key_" + i, "");
            Log.i("liao", "get=" + String.valueOf(System.currentTimeMillis() - lastTime));
            lastTime = System.currentTimeMillis();
            SPUtils.getString("test2", "key_" + i, "");
            Log.i("liao", "get 2=" + String.valueOf(System.currentTimeMillis() - lastTime));
        }
    }
}
