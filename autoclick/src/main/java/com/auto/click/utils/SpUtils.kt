package com.auto.click.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @Author Roger
 * @Date 2023/3/22 15:51
 * @Description
 */
object SpUtils {

    private var mSp: SharedPreferences? = null
    const val SP_NAME = "autoclick"

    fun init(context: Context) {
        mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String?, `val`: String?) {
        mSp!!.edit().putString(key, `val`).apply()
    }

    fun putStringCommit(key: String?, `val`: String?) {
        mSp!!.edit().putString(key, `val`).apply()
    }

    fun getString(key: String?, def: String?): String? {
        return mSp!!.getString(key, def)
    }

    fun getBoolean(key: String?, def: Boolean): Boolean {
        return mSp!!.getBoolean(key, def)
    }

    fun putBoolean(key: String?, `val`: Boolean) {
        mSp!!.edit().putBoolean(key, `val`).apply()
    }

    fun remove(key: String?) {
        mSp!!.edit().remove(key).apply()
    }

    fun getSp(): SharedPreferences? {
        return mSp
    }

    fun getLong(key: String?, `val`: Long): Long {
        return mSp!!.getLong(key, `val`)
    }

    fun putLong(key: String?, `val`: Long) {
        mSp!!.edit().putLong(key, `val`).apply()
    }

    fun getInt(key: String?, `val`: Int): Int {
        return mSp!!.getInt(key, `val`)
    }

    fun putInt(key: String?, `val`: Int) {
        mSp!!.edit().putInt(key, `val`).apply()
    }
}