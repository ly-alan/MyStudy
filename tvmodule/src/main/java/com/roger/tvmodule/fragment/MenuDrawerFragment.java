package com.roger.tvmodule.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.RowsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.VerticalGridView;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;
import com.roger.tvmodule.leanback.FocusHighlightHelper;
import com.roger.tvmodule.leanback.ItemBridgeAdapter;
import com.roger.tvmodule.view.RowPresenter;
import com.roger.tvmodule.view.TextPresenter;
import com.roger.tvmodule.view.TvSlidingMenuView;
import com.roger.tvmodule.view.VerticalPresenterSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Roger
 * @Date 2021/10/15 16:59
 * @Description
 */
public class MenuDrawerFragment extends BaseFragment {

    VerticalGridView mVerticalGridView;
//    TvSlidingMenuView menuView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_drawer_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                Log.d("liao", oldFocus + " : " + newFocus);
//            }
//        });

//        menuView = view.findViewById(R.id.sliding_menu);
//        menuView.into(getActivity());

        mVerticalGridView = view.findViewById(R.id.vertical_gv);
        mItemBridgeAdapter = getMoviceItemDatas();
        mVerticalGridView.setAdapter(mItemBridgeAdapter);
//        mVerticalGridView.setSaveChildrenPolicy();
        mVerticalGridView.setSaveChildrenPolicy(VerticalGridView.SAVE_LIMITED_CHILD);

//        FocusHighlightHelper.setupHeaderItemFocusHighlight(mItemBridgeAdapter);
//        mVerticalGridView.setItemAlignmentOffset(0);
//        mVerticalGridView.setItemAlignmentOffsetPercent(
//                VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
//
//        // align to a fixed position from top
////        mVerticalGridView.setWindowAlignmentOffset(windowAlignOffsetTop);
//        mVerticalGridView.setWindowAlignmentOffsetPercent(
//                VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
//        mVerticalGridView.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);


    }

    ItemBridgeAdapter mItemBridgeAdapter;



// 测试数据,demo.

    public ItemBridgeAdapter getMoviceItemDatas() {
        mItemBridgeAdapter = new ItemBridgeAdapter();
//        final VerticalPresenterSelector presenterSelector = new VerticalPresenterSelector();
        ClassPresenterSelector presenterSelector =  new ClassPresenterSelector()
                .addClassPresenter(String.class,new RowPresenter());
        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(); // 填入Presenter选择器.
//        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(new RowPresenter()); // 填入Presenter选择器.
        for (int i = 0; i < 20; i++) {
            mRowsAdapter.add("row_" + i);
        }
        mItemBridgeAdapter.setAdapter(mRowsAdapter);
        mItemBridgeAdapter.setPresenter(presenterSelector);
        return mItemBridgeAdapter;
    }


//    public ItemBridgeAdapter getMoviceItemDatas() {
//        mItemBridgeAdapter = new ItemBridgeAdapter();
//        final VerticalPresenterSelector presenterSelector = new VerticalPresenterSelector();
//        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(presenterSelector); // 填入Presenter选择器.
//
//        List<String> mDataList = new ArrayList<>();
//
//        for (int i = 0; i < 50; i++) {
//            mDataList.add("item_" + i);
//        }
//
//        for (int i = 0; i < 20; i++) {
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new TextPresenter());
////            mRowsAdapter.add("row_" + i);
//            listRowAdapter.addAll(0,mDataList);
//            mRowsAdapter.add(mDataList);
//
////            mRowsAdapter.add(new ListRow(new HeaderItem("row_" + i), listRowAdapter));
//        }
//
//        mItemBridgeAdapter.setAdapter(mRowsAdapter);
//
//        return mItemBridgeAdapter;
//    }


}
