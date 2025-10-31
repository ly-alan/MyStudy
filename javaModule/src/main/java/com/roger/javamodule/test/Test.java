package com.roger.javamodule.test;

public class Test {
    public static void main(String[] args) throws Exception {
        getPassword();
    }

    private static final long CARD_ID_DATA_LENGTH = 52;

    static final int CARD_PW2_DATA_LEN = (int) CARD_ID_DATA_LENGTH;

    public static void getPassword(){
        System.out.println(decryptPass2(874905404126682l));
    }

    public static long decryptPass2(long passwd) {
        return encryptPass2(passwd);
    }

    public static long encryptPass2(long passwd) {
        return xor(passwd,CARD_PW2_DATA_LEN,4,13);
    }


    public static long xor(long v, int dataLen, int xorLen, int xorConst) {
        long oldValue = v;
        long newValue = 0;

        long xorMask = 0xF;

        for (int i = 0; i < (dataLen / xorLen); i++) {
            long factor = (oldValue & xorMask);
            if (i != ((dataLen / xorLen) - 1)) {
                factor = factor ^ xorConst;
            }
            newValue = (newValue | (factor << (i * xorLen)));
            oldValue = oldValue >> xorLen;
        }
        return newValue;
    }
}
