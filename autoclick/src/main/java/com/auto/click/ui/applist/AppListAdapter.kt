package com.auto.click.ui.applist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.auto.click.R
import com.auto.click.model.AppInfo
import com.bumptech.glide.Glide

/**
 * @Author Roger
 * @Date 2022/9/19 14:23
 * @Description
 */
class AppListAdapter : RecyclerView.Adapter<AppListAdapter.MyViewHolder> {

    var appLists: List<AppInfo>? = null;

    constructor()
    constructor(appList: List<AppInfo>) {
        appLists = appList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_app_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        appLists?.get(position)?.let {
            holder.ivIcon?.setImageDrawable(it.icon)
            holder.tvName?.text = it.label
            holder.tvPackage?.text = it.packageName
            holder.tvSign?.setText(it.sign)
        }
    }

    override fun getItemCount(): Int {
        if (appLists == null) {
            return 0
        } else {
            return appLists!!.size
        }
    }


    //默认内部类就是静态的，非静态的需要加 inner 关键字修饰
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivIcon: ImageView? = null;
        var tvName: TextView? = null;
        var tvPackage: TextView? = null;
        var tvSign: TextView? = null;

        init {
            Log.d("liao", "item = $itemView")
            ivIcon = itemView.findViewById(R.id.icon)
            tvName = itemView.findViewById(R.id.tv_app_name)
            tvPackage = itemView.findViewById(R.id.tv_app_package)
            tvSign = itemView.findViewById(R.id.tv_app_sign)
        }

    }


}