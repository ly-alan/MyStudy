package com.roger.main.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.commonlib.base.BaseFragment;
import com.roger.main.R;
import com.roger.main.activity.ItemActivity;

public class MainFragment extends BaseFragment {

    ListView mListView;
    String[] mDatas = {
            "SubString:" + SubStrFragment.class.getSimpleName(),
            "FileFragment:" + FileFragment.class.getSimpleName(),
            "EditText:" + EditFragment.class.getSimpleName(),
            "Share:" + ShareFragment.class.getSimpleName(),
            "DatePicker:" + DatePickerFragment.class.getSimpleName(),
            "Excel:" + ExcelFragment.class.getSimpleName(),
            "SwitchButton:" + SwitchButtonFragment.class.getSimpleName()
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
        mListView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mDatas));
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
