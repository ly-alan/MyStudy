package com.roger.tvmodule.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.VerticalGridView;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;
import com.roger.tvmodule.leanback.ItemBridgeAdapter;
import com.roger.tvmodule.presenter.FullBgPresenter;
import com.roger.tvmodule.view.TextPresenter;

public class RecycleFragment extends BaseFragment {

    VerticalGridView verticalGridView;
    ItemBridgeAdapter mItemBridgeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycle, container, false);
    }

    @Override
    public void initView() {
        super.initView();
        verticalGridView = getView().findViewById(R.id.vertical_gv);

        verticalGridView.setAdapter(createAdapter());

    }

    private ItemBridgeAdapter createAdapter() {
        ItemBridgeAdapter mItemBridgeAdapter = new ItemBridgeAdapter();
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector()
                .addClassPresenter(Integer.class, new FullBgPresenter())
                .addClassPresenter(String.class, new TextPresenter());
        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(); // 填入Presenter选择器.
        mRowsAdapter.add(0);
        for (int i = 0; i < 10; i++) {
            mRowsAdapter.add("row" + i);
        }
        mItemBridgeAdapter.setAdapter(mRowsAdapter);
        mItemBridgeAdapter.setPresenter(presenterSelector);
        return mItemBridgeAdapter;
    }

}
