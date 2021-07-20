package com.roger.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.android.commonlib.base.BaseFragment
import com.roger.main.R
import android.content.Intent.createChooser as createChooser1

/**
 * @Author Roger
 * @Date 2021/7/20 16:21
 * @Description
 */
class ShareFragment : BaseFragment() {

    var btnShare: Button? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_share, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnShare = view.findViewById(R.id.btn_share);
        btnShare?.setOnClickListener {
//            shareText()
            //两种分享内容表现没区别
            shareHtml();
        }
    }

    //系统原生分享
    private fun shareText() {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.baidu.com");
        startActivity(createChooser1(sendIntent, "share Title"));
    }

    //系统原生分享
    private fun shareHtml() {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.baidu.com");
        startActivity(createChooser1(sendIntent, "share Title"));
    }
}