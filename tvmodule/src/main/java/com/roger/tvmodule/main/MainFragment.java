package com.roger.tvmodule.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;
import com.roger.tvmodule.view.TvTitleBar;

public class MainFragment extends BaseFragment {

    ListView mListView;
    TvTitleBar tvTitleBar;
    String[] mDatas = {
            "测试SpanString:SpanStrFragment",
            "测试oom:OOMFragment",
            "测试android 10文件系统:FileFragment",
            "测试MenuDrawer:MenuDrawerFragment"
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
        tvTitleBar = getView().findViewById(R.id.tv_titlebar);
        titleStyle1();
    }

    private void titleStyle1() {
        tvTitleBar.setSubLogo(R.mipmap.ic_launcher);
        tvTitleBar.setTitle("title > SubTitle");
        tvTitleBar.setSubtitle("122");
        tvTitleBar.setMenuText("CN");
        tvTitleBar.setSubMenuText("16:05");
        tvTitleBar.setLeftLayoutPadding(200, 30, 0, 0);
        tvTitleBar.showLeftTitleDivider(getResources().getDrawable(R.mipmap.home_line));
//        tvTitleBar.showRightTitleDivider(getResources().getDrawable(R.mipmap.home_line));
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

    class MyAdapter extends BaseAdapter {

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

        class ViewHolder {

            TextView mTvText;

            ViewHolder(View view) {
                mTvText = view.findViewById(R.id.tv_list_text);
            }
        }
    }
}
