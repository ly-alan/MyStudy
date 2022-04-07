package com.roger.tvmodule.view;

import android.util.Log;
import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import com.roger.tvmodule.R;

/**
 * @Author Roger
 * @Date 2021/10/20 15:55
 * @Description
 */
public class TextPresenter extends Presenter {
    /**
     * 创建ViewHolder，作用同RecyclerView$Adapter的onCreateViewHolder
     *
     * @param viewGroup
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
//        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_text, viewGroup, false);

        final VideoCardView cardView = new VideoCardView(viewGroup.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                if (selected) {
                    setBackgroundColor(getResources().getColor(R.color.selected_background));
                    setSunTitleText("subTitle");
                } else {
                    setBackgroundColor(getResources().getColor(R.color.white));
                    setSunTitleText("");
                }
                super.setSelected(selected);
            }
        };


//        ImageCardView cardView = new ImageCardView(viewGroup.getContext()) {
//            @Override
//            public void setSelected(boolean selected) {
//                Log.d("liao", "select " + selected);
//                if (selected) {
//                    setBackgroundColor(getResources().getColor(R.color.selected_background));
//                } else {
//                    setBackgroundColor(getResources().getColor(R.color.white));
//                }
//                super.setSelected(selected);
//            }
//        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);

        return new ViewHolder(cardView);
    }

    /**
     * 同RecyclerView$Adapter的onBindViewHolder，但是解耦了position
     *
     * @param viewHolder
     * @param o
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
//        if (o instanceof String) {
//        ((TextView) viewHolder.view.findViewById(R.id.tv_list_text)).setText(o.toString());
//        }
        ((VideoCardView) viewHolder.view).setTitleText(o.toString());
//        ((ImageCardView) viewHolder.view).setTitleText(o.toString());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        //解绑时释放资源
    }
}
