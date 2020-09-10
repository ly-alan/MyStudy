package com.roger.tvmodule.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.commonlib.base.BaseActivity;
import com.roger.c_annotations.TestBindView;
import com.roger.c_annotations.TestUtils;
import com.roger.tvmodule.FragmentFactory;
import com.roger.tvmodule.R;

public class ItemActivity extends BaseActivity {

    private static final String KEY_FRAGMENT_NAME = "FRAGMENT_NAME";

    @TestBindView(R.id.frame_layout_fragment)
    View view;


    public static void startActivity(Context context, String fragmentName) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra(KEY_FRAGMENT_NAME, fragmentName);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        TestUtils.bind(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = FragmentFactory.creatFragment(getIntent().getStringExtra(KEY_FRAGMENT_NAME));
        transaction.add(R.id.frame_layout_fragment, fragment);
        transaction.commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }, 100);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }, 100);
            }
        }
    }
}
