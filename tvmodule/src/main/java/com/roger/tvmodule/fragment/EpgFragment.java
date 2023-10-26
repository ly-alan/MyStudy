package com.roger.tvmodule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;

/**
 * @Author Roger
 * @Date 2023/9/4 16:04
 * @Description
 */
public class EpgFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

}
