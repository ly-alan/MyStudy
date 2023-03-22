package com.auto.click.ui.applist

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auto.click.BR
import com.auto.click.R
import com.auto.click.base.MVVMBaseActivity
import com.auto.click.databinding.ActivityAppListBinding

/**
 * @Author Roger
 * @Date 2022/9/19 14:04
 * @Description
 */
class AppListActivity : MVVMBaseActivity<AppListViewModel, ActivityAppListBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_app_list
    }

    override fun configUI(view: View) {
        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding?.rvAppList?.layoutManager = layoutManager;


        mViewModel?.appLiveData?.observe(this, Observer {
            mBinding?.rvAppList?.adapter = AppListAdapter(it);
        })

        mBinding?.rvAppList?.let {
//            it.addOnItemTouchListener()

        }
    }

    override fun getBindingVariable(): Int {
        return BR._all
    }
}