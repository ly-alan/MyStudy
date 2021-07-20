package com.roger.main.fragment;

import android.os.Environment;
import androidx.core.content.ContextCompat;

import com.android.commonlib.base.BaseFragment;
import com.android.commonlib.utils.L;
import com.roger.main.App;

import java.io.File;
import java.util.Arrays;

/**
 * Create by Roger on 2019/9/19
 */
public class FileFragment extends BaseFragment {

    @Override
    public void setView() {
        super.setView();
        //需要存储权限
        function_1();
        function_2();
        function_3();
        //不需要存储权限，内部空间
        function_4();
        function_5();
        //sd卡专属空间
        function_6();
    }

    private void function_1() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        L.i("liao", file.getAbsolutePath());
        File[] list = file.listFiles();
        L.i("liao", Arrays.toString(list));
    }

    private void function_2() {
        File file = new File(Environment.getDataDirectory().getAbsolutePath());
        L.i("liao", file.getAbsolutePath());
        File[] list = file.listFiles();
        L.i("liao", Arrays.toString(list));
    }

    private void function_3() {
        File file = new File(Environment.getDownloadCacheDirectory().getAbsolutePath());
        L.i("liao", file.getAbsolutePath());
        File[] list = file.listFiles();
        L.i("liao", Arrays.toString(list));
    }

    private void function_4() {
        File file = new File(ContextCompat.getDataDir(App.getInstance()).getAbsolutePath());
        L.i("liao", file.getAbsolutePath());
        File[] list = file.listFiles();
        L.i("liao", Arrays.toString(list));
    }

    private void function_5() {
        File file = new File(ContextCompat.getCodeCacheDir(App.getInstance()).getAbsolutePath());
        L.i("liao", file.getAbsolutePath());
        File[] list = file.listFiles();
        L.i("liao", Arrays.toString(list));
    }

    private void function_6() {
        File file = new File(App.getInstance().getExternalCacheDir().getAbsolutePath());
        L.i("liao", file.getAbsolutePath());
        File[] list = file.listFiles();
        L.i("liao", Arrays.toString(list));
    }
}
