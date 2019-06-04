package com.roger.tvmodule.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;

public class MainFragment extends BaseFragment {

    ListView mListView;
    String[] mDatas = {
            "测试SpanString:SpanStrFragment",
            "测试:SpanStrFragment"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        mListView = getView().findViewById(R.id.lv_main_list);
    }

    @Override
    public void setView() {
        super.setView();
        mListView.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_list_text, mDatas));
    }

    @Override
    public void setListener() {
        super.setListener();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] strings = mDatas[position].split(":");
                ItemActivity.startActivity(getActivity(), strings[1]);
            }
        });
    }
}
