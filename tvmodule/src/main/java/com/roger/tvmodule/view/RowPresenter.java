package com.roger.tvmodule.view;

import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

/**
 * @Author Roger
 * @Date 2021/11/4 16:17
 * @Description
 */
public class RowPresenter extends Presenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(new HorizonRowView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        HorizonRowView horizonRowView = (HorizonRowView) viewHolder.view;
        horizonRowView.setAdapter();
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
