package com.example.testjetpack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.testjetpack.databinding.ActivitySplashBinding
import com.example.testjetpack.databinding.SplashData
import com.example.testjetpack.login.LoginActivity
import com.example.testjetpack.security.SecurityFileUtils

class SplashActivity : AppCompatActivity() {

    companion object {
        private val TAG = SplashActivity::class.simpleName;
    }

    private lateinit var binding: ActivitySplashBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        testSecurityFile();

        var handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                binding.splashData = SplashData("" + msg.what);
                if (msg.what > 0) {
                    sendMessageDelayed(Message.obtain(this, msg.what - 1), 1000);
                } else {
//                    openMainActivity();
                    openLoginActivity();
                }
            }
        }
        handler.sendEmptyMessage(3)
    }

    override fun onResume() {
        super.onResume()
    }

    fun openMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
    }

    fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java));
        finish()
    }

    private fun testSecurityFile() {
        var file = getExternalFilesDir("test");
        Log.i(TAG, file!!.path);
        SecurityFileUtils.getInstance(this)?.writeSecurityData(this, file);
        Log.i(TAG, "result = " + SecurityFileUtils.getInstance(this)?.readSecurityData(this, file));

        SecurityFileUtils.getInstance(this)?.putString("test_key", "test_value");
        Log.i("liao", "preference : " + SecurityFileUtils.getInstance(this)?.getSting("test_key", "default"));
    }


}