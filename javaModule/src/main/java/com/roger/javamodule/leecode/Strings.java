package com.roger.javamodule.leecode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Strings {


    public static void main(String[] args) {
//        longestPalindrome("babad");
//        longestPalindrome("cbbd");
    }

    public String reverseWords(String s) {
        if (s.isEmpty()) {
            return s;
        }
        s = s.trim();
        List<String> list = Arrays.asList(s.split("\\s+"));//\\s+会把多个连续的空格当成一个空格
//        List<String> list = Arrays.asList(s.split(" "));
        Collections.reverse(list);
        return String.join(" ", list);
    }

    /**
     * 编写一个函数来查找字符串数组中的最长公共前缀。
     * <p>
     * 如果不存在公共前缀，返回空字符串 ""。
     *
     * @param strs
     * @return
     */
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            //判断为空
            return "";
        }
        for (int n = 0; n < strs[0].length(); n++) {
            //以第一个字符串的长度去比较所有的字符串
            char c = strs[0].charAt(n);
            for (int i = 0; i < strs.length; i++) {
                if (strs[i].length() == n || strs[i].charAt(n) != c) {
                    return strs[0].substring(0, n);
                }
            }
        }
        //数组的几个值全部相等或者只有长度为1
        return strs[0];
    }

    /**
     * 输入：s = "babad"
     * 输出："bab"
     * 解释："aba" 同样是符合题意的答案。
     * 示例 2：
     * <p>
     * 输入：s = "cbbd"
     * 输出："bb"
     * <p>
     * 输入"ac"
     * 输出”a“
     * <p>
     * 输入”a“
     * 输出”a“
     *
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 2) return s;
        int resIdx = 0, resLen = 1;
        for (int idx = 0; idx < s.length() - 1; idx++) {
            //奇数最长回文长度
            int len1 = longestLen(s, idx, idx);
            //偶数最长回文长度
            int len2 = longestLen(s, idx, idx + 1);
            if (len1 > resLen) {
                resLen = len1;
                resIdx = idx - resLen / 2;
            }
            if (len2 > resLen) {
                resLen = len2;
                resIdx = idx - (resLen / 2 - 1);
            }
        }
        return s.substring(resIdx, resIdx + resLen);
    }

    //左右探索最长回文子串长度
    private static int longestLen(String s, int left, int right) {
        while (left >= 0 && right < s.length()) {
            if (s.charAt(left) != s.charAt(right)) break;
            left--;
            right++;
        }
        return right - left - 1;
    }


}
