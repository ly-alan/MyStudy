package com.android.commonlib.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.commonlib.R;
import com.android.commonlib.utils.ResUtils;
import com.android.commonlib.view.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


/**
 * Created by yong.liao on 2018/3/29 0029.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * Fragment管理器
     */
    public FragmentManager mFragmentManager;
    /**
     * 进度提示弹窗
     */
    LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFragmentManager = getSupportFragmentManager();
        setStatusBar();
        //注册EventBus
        if (isUserEvent()) {
            EventBus.getDefault().register(this);
        }
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(getTAG())) {
            BaseApplication.getInstance().addActivity(getTAG(), this);
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
        setView();
        setListener();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        setView();
        setListener();
    }

    public void initView() {
    }

    public void setView() {
    }

    public void setListener() {
    }

    public boolean isUserEvent() {
        return false;
    }

    public void showLoadingDialog() {
        showLoadingDialog(true);
    }

    public void showLoadingDialog(boolean touchOutsideCancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.setCanceledOnTouchOutside(touchOutsideCancelable);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 设置状态栏颜色
     */
    private void setStatusBar() {
        //5.0以上修改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ResUtils.getColor(R.color.window_background_black));
        }
    }


    @Override
    protected void onDestroy() {
        //注销EventBus
        if (isUserEvent()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(getTAG());
    }

    public String getTAG() {
        return getClass().getSimpleName();
    }

    public void postEventData(Object object) {
        if (null != object) {
            EventBus.getDefault().post(object);
        }
    }

    /**
     * 安装apk
     *
     * @param filePath
     */
    public void installedApk(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 打开软键盘
     */
    public void openSoftKeyboard(EditText editText) {
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }
    }

    /**
     * 关闭键盘
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
