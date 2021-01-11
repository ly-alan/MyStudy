package com.example.testjetpack.home

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.lifecycle.LiveData
import java.util.*

class HomeLiveData : LiveData<String>() {

    override fun onActive() {
        super.onActive()
        loadData()
        Log.i("liao","onActive")
    }

    override fun onInactive() {
        super.onInactive()
        Log.i("liao","onInactive")
    }

    private fun loadData() {
        val handler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                postValue(Date().toString())
                sendEmptyMessageDelayed(1,2000)
            }
        }
        handler.sendEmptyMessageDelayed(1, 2000)
    }

}