package com.roger.tvmodule.home;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roger.tvmodule.R;

public class HomeActivity extends AppCompatActivity {

    View layoutMenu, btn1, btn2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        layoutMenu = findViewById(R.id.layout_menu);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn1.setOnFocusChangeListener(focusChangeListener);
        btn2.setOnFocusChangeListener(focusChangeListener);
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            ViewGroup.LayoutParams params = layoutMenu.getLayoutParams();
            if (hasFocus) {
                params.width = getResources().getDimensionPixelOffset(R.dimen.dp_300);
            } else {
                params.width = getResources().getDimensionPixelOffset(R.dimen.dp_80);
            }
            layoutMenu.setLayoutParams(params);
        }
    };


}
