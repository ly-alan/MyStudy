package com.android.commonlib.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加解密
 * Created by yong.liao on 2018/5/29 0029.
 */

public class CryptUtils {

    private static final String AES_CIPHER_MODE = "AES/CFB/NoPadding";//使用AES加密填充
    private static final String DES_CIPHER_MODE = "DES/ECB/PKCS5Padding";//使用DES加密填充
    public static final String RSA_CIPHER_MODE = "RSA/ECB/PKCS1Padding";//AES加密填充方式

    /**
     * 对给定的字符串进行base64编码操作
     */
    public static String enCryptBase64(String inputData) {
        return Base64.encodeToString(inputData.getBytes(), Base64.DEFAULT);
    }


    /**
     * 对给定的字符串进行base64解密操作
     */
    public static String deCryptBase64(String encodedString) {
        return new String(Base64.decode(encodedString, Base64.DEFAULT));
    }


    /**
     * 对字符串加密
     *
     * @param key  密钥
     * @param data 源字符串
     * @return 加密后的字符串
     */
    public static String enCryptAES(String data, String key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(AES_CIPHER_MODE);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字符串解密
     *
     * @param key  密钥
     * @param data 已被加密的字符串
     * @return 解密得到的字符串
     */
    public static String deCryptAES(String data, String key) throws Exception {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(AES_CIPHER_MODE);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String enCryptMD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * MD5加密
     * @param file
     * @return
     */
    public static String enCryptMD5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 加密
     *
     * @param data   要加密的字符串
     * @param encKey 密钥 ID 对应的密钥
     * @return
     */
    public static String enCryptDES(String data, String encKey) {
        byte[] encryptedString = null;
        try {
            // 初始化密钥
            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("utf-8"), "DES");
            // 选择使用 DES 算法，ECB 方式，填充方式为 PKCS5Padding
            Cipher cipher = Cipher.getInstance(DES_CIPHER_MODE);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // 获取加密后的字符串
            encryptedString = cipher.doFinal(data.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toHexStr(encryptedString);
    }

    /**
     * 解码
     *
     * @param data   密文
     * @param encKey 秘钥
     * @return
     */
    public static String deCryptDES(String data, String encKey) {
        byte[] decryptedString = null;
        try {
            // 初始化密钥
            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("utf-8"), "DES");
            // 选择使用 DES 算法，ECB 方式，填充方式为 PKCS5Padding
            Cipher cipher = Cipher.getInstance(DES_CIPHER_MODE);
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // 获取解密后的字符串
            decryptedString = cipher.doFinal(hexToBytes(data));
            return new String(decryptedString, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换字节码
     *
     * @param hexStr 密文
     * @return
     */
    public static byte[] hexToBytes(String hexStr) {
        int len = hexStr.length();
        hexStr = hexStr.toUpperCase(Locale.getDefault());
        byte[] des;
        if (len % 2 != 0 || len == 0) {
            return null;
        } else {
            int halfLen = len / 2;
            des = new byte[halfLen];
            char[] tempChars = hexStr.toCharArray();
            for (int i = 0; i < halfLen; ++i) {
                char c1 = tempChars[i * 2];
                char c2 = tempChars[i * 2 + 1];
                int tempI = 0;
                if (c1 >= '0' && c1 <= '9') {
                    tempI += ((c1 - '0') << 4);
                } else if (c1 >= 'A' && c1 <= 'F') {
                    tempI += (c1 - 'A' + 10) << 4;
                } else {
                    return null;
                }
                if (c2 >= '0' && c2 <= '9') {
                    tempI += (c2 - '0');
                } else if (c2 >= 'A' && c2 <= 'F') {
                    tempI += (c2 - 'A' + 10);
                } else {
                    return null;
                }
                des[i] = (byte) tempI;
            }
            return des;
        }
    }

    /**
     * 转换为16位字符串
     *
     * @param key
     * @return
     */
    public static String toHexStr(byte[] key) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < key.length; i++) {
            b.append(toHexStr(key[i]));
        }
        return b.toString();
    }

    public static String toHexStr(byte bValue) {
        int i, j;
        char c1, c2;
        String s;
        i = (bValue & 0xf0) >>> 4;
        j = bValue & 0xf;
        if (i > 9)
            c1 = (char) (i - 10 + 'A');
        else
            c1 = (char) (i + '0');
        if (j > 9)
            c2 = (char) (j - 10 + 'A');
        else
            c2 = (char) (j + '0');
        s = String.valueOf(c1) + String.valueOf(c2);
        return s;
    }
}
