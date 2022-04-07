package com.roger.tvmodule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;


import com.roger.tvmodule.R;
import com.roger.tvmodule.leanback.FocusHighlightHelper;
import com.roger.tvmodule.leanback.ItemBridgeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Roger
 * @Date 2021/10/20 15:21
 * @Description
 */
public class HorizonRowView extends FrameLayout {

    private HorizontalGridView mGridView;
    private TextView tvMore;

    private boolean lastFocusIsMe = false;


    ItemBridgeAdapter itemBridgeAdapter;

    public HorizonRowView(@NonNull Context context) {
        this(context, null);
    }

    public HorizonRowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizonRowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_horizon_row, this);
        tvMore = findViewById(R.id.tv_more);
        mGridView = (HorizontalGridView) findViewById(R.id.row_horizon_content);
        mGridView.setHasFixedSize(false);
        setClipChildren(false);
        mGridView.setNumRows(1);
        //item纵向和横向的距离
        mGridView.setItemSpacing(20);
        //item的对齐方式
        mGridView.setGravity(Gravity.CENTER_VERTICAL);
        //可使用setFocusScrollStrategy设置滚动策略
        //设置
//        mGridView.setOnChildViewHolderSelectedListener(new OnChildViewHolderSelectedListener() {
//            @Override
//            public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
//                super.onChildViewHolderSelected(parent, child, position, subposition);
//                Log.d("liao", "onChildViewHolderSelected() returned: " + position);
//                //大部分情况下可以通过该方法获取到position
//
//            }
//
//            @Override
//            public void onChildViewHolderSelectedAndPositioned(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
//                super.onChildViewHolderSelectedAndPositioned(parent, child, position, subposition);
//                Log.d("liao", "onChildViewHolderSelectedAndPositioned() returned: " + position);
//                //当通过setSelectedPosition()方法大幅移动列表时，该方法会回调，返回的是最终的真实的position（当set的值超出范围时...)
//            }
//        });


//        //设置上焦动画,缩放倍数可以在demin文件种设置lb_browse_header_select_scale，默认1.2
//        //这个类会帮忙找寻正确的焦点，不然容易丢焦，必须要，
//        // 若不需要缩放，可以设置，setupHeaderItemFocusHighlight(adapter,false)


        getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (hasFocus()) {
                    if (!lastFocusIsMe) {
                        mGridView.requestFocus();
                    }
                    lastFocusIsMe = true;
                } else {
                    lastFocusIsMe = false;
                }
//                Log.i("liao",hasFocus() + " newFocus = " + newFocus);
            }
        });
    }

//    public void setAdapter() {
//        //创建ObjectAdapter，用于提供数据，当有多种类型时，传入PresenterSelector
//        ArrayObjectAdapter objectAdapter = new ArrayObjectAdapter(new TextPresenter());
//        //初始化模拟数据
//        List<String> mDataList = new ArrayList<>();
//
//        for (int i = 0; i < 50; i++) {
//            mDataList.add("item_" + i);
//        }
//        //添加数据
//        objectAdapter.addAll(0, mDataList);
////        listRowAdapter.addAll(0,mDataList);
////        objectAdapter.add(new ListRow(null, listRowAdapter));
//        //通过前面创建的objectAdapter创建ItemBridgeAdapter，完成数据的传递
//        itemBridgeAdapter = new ItemBridgeAdapter(objectAdapter);
//        //将ItemBridgeAdapter传入HorizontalGridView
//        mGridView.setAdapter(itemBridgeAdapter);
//        FocusHighlightHelper.setupHeaderItemFocusHighlight(itemBridgeAdapter);
//    }

    public void setAdapter() {
        ClassPresenterSelector presenterSelector =  new ClassPresenterSelector()
                .addClassPresenter(String.class,new TextPresenter());
        ArrayObjectAdapter mRowsAdapter = new ArrayObjectAdapter(); // 填入Presenter选择器.
        //初始化模拟数据
        List<String> mDataList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            mDataList.add("item_" + i);
        }
        mRowsAdapter.addAll(0,mDataList);
        itemBridgeAdapter = new ItemBridgeAdapter(mRowsAdapter);
        itemBridgeAdapter.setPresenter(presenterSelector);
        mGridView.setAdapter(itemBridgeAdapter);

//        //创建ObjectAdapter，用于提供数据，当有多种类型时，传入PresenterSelector
//        ArrayObjectAdapter objectAdapter = new ArrayObjectAdapter(new TextPresenter());
//        //初始化模拟数据
//        List<String> mDataList = new ArrayList<>();
//
//        for (int i = 0; i < 50; i++) {
//            mDataList.add("item_" + i);
//        }
//        //添加数据
//        objectAdapter.addAll(0, mDataList);
////        listRowAdapter.addAll(0,mDataList);
////        objectAdapter.add(new ListRow(null, listRowAdapter));
//        //通过前面创建的objectAdapter创建ItemBridgeAdapter，完成数据的传递
//        itemBridgeAdapter = new ItemBridgeAdapter(objectAdapter);
//        //将ItemBridgeAdapter传入HorizontalGridView
//        mGridView.setAdapter(itemBridgeAdapter);
        FocusHighlightHelper.setupHeaderItemFocusHighlight(itemBridgeAdapter);
    }


}
