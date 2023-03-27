package com.auto.click.ui.applist

import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auto.click.BR
import com.auto.click.R
import com.auto.click.base.MVVMBaseActivity
import com.auto.click.databinding.ActivityAppListBinding
import kotlinx.android.synthetic.main.activity_app_list.*

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
        initTitleBar()

        var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_app_list.layoutManager = layoutManager;
        if (rv_app_list.itemDecorationCount == 0) {
            rv_app_list.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        }

        mViewModel?.appLiveData?.observe(this, Observer {
            rv_app_list.adapter = AppListAdapter(it);
        })
    }

    override fun getBindingVariable(): Int {
        return BR._all
    }


    private fun initTitleBar() {
        title = "选择要辅助的应用"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}