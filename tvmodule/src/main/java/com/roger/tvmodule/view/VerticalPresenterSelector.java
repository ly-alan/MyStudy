package com.roger.tvmodule.view;

import android.util.Log;

import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

/**
 * @Author Roger
 * @Date 2021/11/4 16:04
 * @Description
 */
public class VerticalPresenterSelector extends PresenterSelector {

    public VerticalPresenterSelector() {
        super();
    }

    @Override
    public Presenter getPresenter(Object item) {
        Log.d("liao", "item : " + item);
        if (item instanceof String){
            return new RowPresenter();
        }
        return new ListRowPresenter();
    }
}
