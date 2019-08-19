package com.roger.tvmodule.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        mListView.setItemsCanFocus(true);
    }

    @Override
    public void setView() {
        super.setView();
//        mListView.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_list_text, R.id.tv_list_text, mDatas));
        mListView.setAdapter(new MyAdapter());
    }

    @Override
    public void setListener() {
        super.setListener();
        //item获取焦点后，该点击事件失效
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String[] strings = mDatas[position].split(":");
//                ItemActivity.startActivity(getActivity(), strings[1]);
//            }
//        });
    }

    class MyAdapter extends BaseAdapter{

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return mDatas.length;
        }

        @Override
        public Object getItem(int position) {
            return mDatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_text, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTvText.setText(mDatas[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] strings = mDatas[position].split(":");
                    ItemActivity.startActivity(getActivity(), strings[1]);
                }
            });
            return convertView;
        }

        class ViewHolder{

            TextView mTvText;

            ViewHolder(View view) {
                mTvText = view.findViewById(R.id.tv_list_text);
            }
        }
    }
}
