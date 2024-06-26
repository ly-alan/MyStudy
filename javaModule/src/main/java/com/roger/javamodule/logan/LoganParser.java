package com.roger.javamodule.logan;/*
 * Copyright (c) 2018-present, 美团点评
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import com.roger.javamodule.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * logan库上报的文件解密方法
 */
public class LoganParser {
    private static final String ALGORITHM = "AES";
    private static final String ALGORITHM_TYPE = "AES/CBC/NoPadding";
    private Cipher mDecryptCipher;
    private static byte[] mEncryptKey16 = "0123456789012345".getBytes(); //128位ase加密Key
    private static byte[] mEncryptIv16 = "0123456789012345".getBytes(); //128位aes加密IV

    private static final String OriginFilePath = "D:\\Program Files\\WeChat Files\\wxid_z281q8b7gbie22\\FileStorage\\File\\2024-05\\LocalCache(8)\\LocalCache\\log\\1716134400000";
    private static final String SaveFilePath = "D:\\Program Files\\WeChat Files\\wxid_z281q8b7gbie22\\FileStorage\\File\\2024-05\\LocalCache(8)\\LocalCache\\log\\5-20-17.txt";

    public static void main(String[] args) {
        try {
            new LoganParser(mEncryptKey16, mEncryptIv16)
                    .parse(new FileInputStream(OriginFilePath), new FileOutputStream(SaveFilePath));
        } catch (Exception e) {
            Log.e("liao", "error:" + e);
        }
    }

    public LoganParser(byte[] encryptKey16, byte[] encryptIv16) {
//        mEncryptKey16 = encryptKey16;
//        mEncryptIv16 = encryptIv16;
        initEncrypt();
    }

    private void initEncrypt() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(mEncryptKey16, ALGORITHM);
        try {
            mDecryptCipher = Cipher.getInstance(ALGORITHM_TYPE);
            mDecryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(mEncryptIv16));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public void parse(InputStream is, OutputStream os) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int t = 0;
        try {
            while ((t = is.read(buffer)) >= 0) {
                bos.write(buffer, 0, t);
                bos.flush();
            }
            byte[] content = bos.toByteArray();
            for (int i = 0; i < content.length; i++) {
                byte start = content[i];
                if (start == '\1') {
                    i++;
                    int length = (content[i] & 0xFF) << 24 |
                            (content[i + 1] & 0xFF) << 16 |
                            (content[i + 2] & 0xFF) << 8 |
                            (content[i + 3] & 0xFF);
                    i += 3;
                    int type;
                    if (length > 0) {
                        int temp = i + length + 1;
                        if (content.length - i - 1 == length) { //异常
                            type = 0;
                        } else if (content.length - i - 1 > length && '\0' == content[temp]) {
                            type = 1;
                        } else if (content.length - i - 1 > length && '\1' == content[temp]) { //异常
                            type = 2;
                        } else {
                            i -= 4;
                            continue;
                        }
                        byte[] dest = new byte[length];
                        System.arraycopy(content, i + 1, dest, 0, length);
                        ByteArrayOutputStream uncompressBytesArray = new ByteArrayOutputStream();
                        InflaterInputStream inflaterOs = null;
                        byte[] uncompressByte;
                        try {
                            uncompressBytesArray.reset();
                            inflaterOs = new GZIPInputStream(new CipherInputStream(new ByteArrayInputStream(dest), mDecryptCipher));
                            int e = 0;
                            while ((e = inflaterOs.read(buffer)) >= 0) {
                                uncompressBytesArray.write(buffer, 0, e);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        uncompressByte = uncompressBytesArray.toByteArray();
                        uncompressBytesArray.reset();
                        os.write(uncompressByte);
                        if (inflaterOs != null)
                            inflaterOs.close();
                        i += length;
                        if (type == 1) {
                            i++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
