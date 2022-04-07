package com.roger.tvmodule.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.commonlib.base.BaseActivity;
import com.roger.c_annotations.RandomInt;
import com.roger.c_annotations.TestBindView;
import com.roger.c_annotations.TestUtils;
import com.roger.tvmodule.R;

public class MainActivity extends BaseActivity {

    @TestBindView(R.id.main_browse_fragment)
    View view;

    @RandomInt
    int number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestUtils.bind(this);
        Log.d("liao", "view : " + view + " : " + number);
    }
}
