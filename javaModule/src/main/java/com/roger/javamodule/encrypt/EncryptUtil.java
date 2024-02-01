package com.roger.javamodule.encrypt;

import java.math.BigInteger;

/**
 * @Author Roger
 * @Date 2024/2/1 16:29
 * @Description
 */
class EncryptUtil {

    public static void main(String[] args) {
        System.out.println("1706775624 -》 " + new BigInteger("1706775624",10).toString(36));
        System.out.println("s865i0 -》 " + new BigInteger("s865i0",36).toString(10));
    }
}
