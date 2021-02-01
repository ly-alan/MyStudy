package com.example.testlogan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.dianping.logan.Logan;
import com.dianping.logan.LoganConfig;
import com.dianping.logan.SendLogRunnable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_init:
                initLogan();
                break;
            case R.id.btn_log:
                handler.sendEmptyMessageDelayed(1,500);
                break;
            case R.id.btn_send:
                sendLog();
                break;
        }
    }

    private void initLogan() {
        LoganConfig config = new LoganConfig.Builder()
                .setCachePath(getApplicationContext().getExternalCacheDir().getAbsolutePath())
                .setPath(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()
                        + File.separator + "logan_v1")
                .setEncryptKey16("0123456789012345".getBytes())
                .setEncryptIV16("0123456789012345".getBytes())
                .setMaxFile(1)
                .build();
        Logan.init(config);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            createLog();
            Logan.f();
            sendEmptyMessageDelayed(0,500);
        }
    };


    private void createLog() {
        Logan.w("test fEDx4GRJdwZfE313wz9-CZMYlBbn6xkCXhE0kEPqu2Mgp6HL8U992bDLLymPuCfXGtkH_xlSB-RZ42VdXp1exiLFmHnnMIe484f7DwqEngwg2KnuqEf5iX9DAVWshFSnaPp2tA-5-B7mmxtxyfml7nJ4Ncpi2wsxXjV7t4fkXUyIWr5b4_577iVk8avsOUqL", 0);
        Logan.w("test 1 fEDx4GRJdwZfE313wz9-CZMYlBbn6xkCXhE0kEPqu2Mgp6HL8U992bDLLymPuCfXGtkH_xlSB-RZ42VdXp1exiLFmHnnMIe484f7DwqEngwg2KnuqEf5iX9DAVWshFSnaPp2tA-5-B7mmxtxyfml7nJ4Ncpi2wsxXjV7t4fkXUyIWr5b4_577iVk8avsOUqL", 1);
        Logan.w("test 2 fEDx4GRJdwZfE313wz9-CZMYlBbn6xkCXhE0kEPqu2Mgp6HL8U992bDLLymPuCfXGtkH_xlSB-RZ42VdXp1exiLFmHnnMIe484f7DwqEngwg2KnuqEf5iX9DAVWshFSnaPp2tA-5-B7mmxtxyfml7nJ4Ncpi2wsxXjV7t4fkXUyIWr5b4_577iVk8avsOUqL", 2);
        Logan.w("test 3 fEDx4GRJdwZfE313wz9-CZMYlBbn6xkCXhE0kEPqu2Mgp6HL8U992bDLLymPuCfXGtkH_xlSB-RZ42VdXp1exiLFmHnnMIe484f7DwqEngwg2KnuqEf5iX9DAVWshFSnaPp2tA-5-B7mmxtxyfml7nJ4Ncpi2wsxXjV7t4fkXUyIWr5b4_577iVk8avsOUqL", 3);
        Log.d("liao", "aaaa");

    }


    /**
     * c	log-content 日志的内容	long one
     * f	flag-key 日志的标记	1
     * l	local-time 日志的当地时间	1539611498547
     * n	threadname_key 写当前日志的线程名	main/thread-24
     * i	threadid_key 写当前日志的线程id	1
     * m	ismain_key 是否在主线程中运行
     */
    private void sendLog() {
        //若没有当天日志，则不会触发回调
        Logan.s(new String[]{"2021-01-11", "2021-01-12"}, new SendLogRunnable() {
            @Override
            public void sendLog(File logFile) {
                finish();
                File output = new File(getApplicationContext().getExternalCacheDir().getAbsolutePath()+"/decrypt");

                Log.i("liao","file = " + logFile.getAbsolutePath());
                try {
                    if (!output.exists()){
                        output.createNewFile();
                    }
                    new LoganParser("0123456789012345".getBytes(),"0123456789012345".getBytes())
                            .parse(new FileInputStream(logFile),
                                    new FileOutputStream(output));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
