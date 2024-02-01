package com.roger.javamodule.test;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;

import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public class Main2 {
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final int KEY_SIZE = 1024;

    public static Map<String, String> createKeys() {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }
        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(KEY_SIZE);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            e.getMessage();
        }
        byte[] resultDatas = out.toByteArray();
        try {
            out.close();
        } catch (Exception e) {
            e.getMessage();
        }
        return resultDatas;
    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            str += Integer.toHexString(ch);
        }
        return str;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> keyMap = createKeys();
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVQEDqFU18mP9g1wdg9rkPS45VEj98vdLQntD/FtUnDkdmGYbjmy+LWT/C6W27wXZbzaIS3N3WT+VxHxULREgolh8WweItakgTDQnKm1jGqgEUSb/7vmPTrqGf4p9ZQRZxRneyXh9zku/8MYB83Dl+x7+3jT4SPY0tZmXlKJXeGQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJVAQOoVTXyY/2DXB2D2uQ9LjlUSP3y90tCe0P8W1ScOR2YZhuObL4tZP8LpbbvBdlvNohLc3dZP5XEfFQtESCiWHxbB4i1qSBMNCcqbWMaqARRJv/u+Y9OuoZ/in1lBFnFGd7JeH3OS7/wxgHzcOX7Hv7eNPhI9jS1mZeUold4ZAgMBAAECgYAtlfd9x83n/Y8a0/KUAieZ7Vjm0PEFN7FZOPjawO2PxUO3gpp5kF+gLz1TWhrDGx78gT08h6tWFwccWwMBcvSavy/i7VJLrB86L9OfiNLdUZRqctg91X5k0+9rTTr1gQLlwm42M7G22Yc9wFPauxIocLLtdmFCnKYD1arsufZj9QJBAO3kY5wM+qGOBqC5wN+5kKVzDI0ZGrsoWGgXWGLug05m7wzxNE6rk3h6QaqS3YguLgn1WzgsrcygOzpqNsMTDj8CQQCgnJYZ2/1OsbTAv7iKssoSZWLRWUDH8GR34+cI7XWotUeOiPpsTYOLmqj5HVZ+wuWVRRH/eU3bT3ER0dSFua2nAkEAr1PNKhzStzDCp8Bfa0QLXDTvwioHbnqstYLHLJrXAXiRpHs87uI7XObV8M4vIYfPfJ1tLniXWyGYpVPUqm8n5wJAcmxcO3wQ0NmlrpRoAtgxQj3c0szFbCwFR6yj0iZqZpEPs6o+zp3Ck9jRfPi7wH9VVKzqj5l6MD4+jbVxtPt9jQJAexZ0at+3yW2v+jJp8mMgrIZoQaHkmTMPKAyskoyBEyO5rJO/AfN9pKZtnkssaklTC4r1qSK02jg6uEr6laIGtA==";
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
        System.out.println("私钥加密——公钥解密");
        String str = "{\"domainId\":\"guanbi\",\"externalUserId\":\"optnetpublic\"}";
        String url1 = "http://51.222.82.100/page/h3f9a8544fd634baa8eb4e00?";
        System.out.println("\r明文：\r\n" + str);
        String encodedData = privateEncrypt(str, getPrivateKey(privateKey));
        System.out.println("密文：\r\n" + toHexString(encodedData));
        String url2 = url1 + "provider=guanbi&ssoToken=" + toHexString(encodedData);
        System.out.println("免密地址: \r\n" + url2);
        String decodedData = publicDecrypt(encodedData, getPublicKey(publicKey));
        System.out.println("解密后文字: \r\n" + decodedData);
    }
}