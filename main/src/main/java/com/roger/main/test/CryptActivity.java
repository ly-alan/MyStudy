package com.roger.main.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.commonlib.base.BaseActivity;
import com.android.commonlib.utils.ToastUtils;
import com.roger.main.R;

import org.apache.commons.codec.binary.Base64;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * 测试生成加密串
 */
public class CryptActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_crypt);
        EditText etTime = findViewById(R.id.et_crypt_time);
        EditText etName = findViewById(R.id.et_crypt_name);
        EditText etResult = findViewById(R.id.et_crypt_result);
        Button btnOk = findViewById(R.id.ok_crypt);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTime.getText().length() != 10 && etTime.getText().length() != 13) {
                    //10位，s的时间戳。13位，毫秒
                    ToastUtils.showToast("请输入正确时间戳");
                    etResult.setText("");
                    return;
                }
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    name = "axzk";
                }
                etResult.setText(getEncrypt(etTime.getText().toString(), name));
            }
        });
    }

    private static String getEncrypt(String text1, String text2) {
        String time = new BigInteger(text1, 10).toString(36);
        System.out.println("time = " + time);
        String name = new BigInteger(text2, 36).toString(26);
        System.out.println("name = " + name);
        String result = new StringBuffer(time + "-" + name).reverse().toString();
        System.out.println("result = " + result);
        try {
            String base64 = new String(new Base64().encode(result.getBytes(StandardCharsets.UTF_8)));
            return base64;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

}