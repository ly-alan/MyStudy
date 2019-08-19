package com.roger.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.commonlib.base.BaseActivity;
import com.roger.main.FragmentFactory;
import com.roger.main.R;

public class ItemActivity extends BaseActivity {

    private static final String KEY_FRAGMENT_NAME = "FRAGMENT_NAME";

    public static void startActivity(Context context, String fragmentName) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra(KEY_FRAGMENT_NAME, fragmentName);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = FragmentFactory.creatFragment(getIntent().getStringExtra(KEY_FRAGMENT_NAME));
        transaction.add(R.id.frame_layout_fragment, fragment);
        transaction.commit();
    }
}
