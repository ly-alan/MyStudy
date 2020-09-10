package com.roger.tvmodule.fragment

import android.os.Bundle
import android.os.Process
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.android.commonlib.base.BaseFragment
import com.roger.tvmodule.R
import io.reactivex.Flowable.create
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.*
import java.util.*

public class OOMFragment : BaseFragment(), View.OnClickListener {

    val ERROR_HINT: String = "Error ! please input a number in upper EditText First"
    val UNIT_M = 1024 * 1024;
    var dashboard: TextView? = null;
    var etDigtal: EditText? = null;
    var digtal = -1;
    var heap: ArrayList<ByteArray> = ArrayList();


    private val increaseFDRunnable = Runnable {
        try {
            val bufferedReader = BufferedReader(FileReader("/proc/" + Process.myPid() + "/status"))
            Log.d("liao", bufferedReader.readLine())
            //                Thread.sleep(Long.MAX_VALUE);
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private val emptyRunnable = Runnable {
        try {
            Thread.sleep(Long.MAX_VALUE)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun createFDThread() {
        Observable.create { subscriber: ObservableEmitter<Any?> ->
            subscriber.onNext(true)
            subscriber.onComplete();
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Any? ->
                    try {
                        val bufferedReader = BufferedReader(FileReader("/proc/" + Process.myPid() + "/status"))
                        Log.e("liao", "next : " + bufferedReader.readLine())
                    } catch (e: Exception) {
                    }
                }, { onError: Throwable? ->
                    Log.e("liao", "onError : ")
                }) {
                    Log.e("liao", "onComplete : ")
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_oom, container, false)
    }

    override fun initView() {
        super.initView()
        dashboard = getView()?.findViewById<View>(R.id.tv_dashboard) as TextView
        etDigtal = getView()?.findViewById<View>(R.id.et_digtal) as EditText
        getView()?.findViewById<View>(R.id.bt1)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt2)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt3)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt4)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt5)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt6)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt7)?.setOnClickListener(this)
        getView()?.findViewById<View>(R.id.bt8)?.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        digtal = try {
            Integer.valueOf(etDigtal!!.text.toString())
        } catch (e: Exception) {
            -1
        }
        when (view.id) {
            R.id.bt1 -> showFileContent("/proc/" + Process.myPid() + "/limits")
            R.id.bt2 -> if (digtal <= 0) {
                dashboard!!.text = ERROR_HINT
            } else {
                var i = 0
                while (i < digtal) {

//                        new Thread(increaseFDRunnable).start();
                    createFDThread()
                    i++
                }
            }
            R.id.bt3 -> {
                val fdFile = File("/proc/" + Process.myPid() + "/fd")
                val files = fdFile.listFiles()
                if (files != null) {
                    dashboard!!.text = "current FD numbler is " + files.size
                } else {
                    dashboard!!.text = "/proc/pid/fd is empty "
                }
                printThread()
            }
            R.id.bt4 -> {
                try {
                    val pro = Runtime.getRuntime().exec("ping -n 20 " + "www.baidu.com")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                showFileContent("/proc/" + Process.myPid() + "/status")
            }
            R.id.bt5 -> if (digtal <= 0) {
                dashboard!!.text = ERROR_HINT
            } else {
                var i = 0
                while (i < digtal) {
                    Thread(emptyRunnable).start()
                    i++
                }
            }
            R.id.bt6 -> {
                val stringBuilder = StringBuilder()
                stringBuilder.append("Java Heap Max : ").append(Runtime.getRuntime().maxMemory() / UNIT_M).append(" MB\r\n")
                stringBuilder.append("Current used  : ").append((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / UNIT_M).append(" MB\r\n")
                dashboard!!.text = stringBuilder.toString()
            }
            R.id.bt7 -> if (digtal <= 0) {
                dashboard!!.text = ERROR_HINT
            } else {
                val bytes = ByteArray(digtal)
                heap.add(bytes)
            }
            R.id.bt8 -> {
                heap = ArrayList()
                System.gc()
            }
        }
    }

    private fun showFileContent(path: String) {
        if (TextUtils.isEmpty(path)) {
            return
        }
        try {
            val randomAccessFile = RandomAccessFile(path, "r")
            val stringBuilder = StringBuilder()
            var s: String? = null;
            while (randomAccessFile.readLine() != null) {
                stringBuilder.append(s).append("\r\n")
            }
            dashboard!!.text = stringBuilder.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun printThread() {
        val stacks = Thread.getAllStackTraces()
        val set: Set<Thread> = stacks.keys
        Log.e("liao", "thread = " + set.size)
        for (key in set) {
            val stackTraceElements = stacks[key]!!
            Log.d("liao", "---- print thread: " + key.name + " start ----")
            for (st in stackTraceElements) {
                Log.d("liao", "StackTraceElement: $st")
            }
            Log.d("liao", "---- print thread: " + key.name + " end ----")
        }
    }

    companion object {
        private const val ERROR_HINT = "Error ! please input a number in upper EditText First"
        const val UNIT_M = 1024 * 1024.toFloat()
    }
}