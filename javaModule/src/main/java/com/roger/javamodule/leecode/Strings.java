package com.roger.javamodule.leecode;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.math3.analysis.function.Max;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Strings {


    public static void main(String[] args) {
//        //最长公共前缀，输出fl
//        longestCommonPrefix(new String[]{"flower", "flow", "flight"});
//        //最长回文串，输出bab
//        longestPalindrome("babad");
//        //翻转字符串里面的单词,输出world hello
//        reverseWords("  hello world  ");
//        //查找字串出现的位置，类似str.indexOf(s)
//        kmpSearch("leetcode", "leeto");
//        //翻转字符串
//        reverseString("hellow".toCharArray());
//        //贪心算法
//        arrayPairSum(new int[]{6, 2, 6, 5, 1, 2});
//        //求值
//        twoSum(new int[]{2, 3, 4}, 6);

        minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3});
    }

    public static String reverseWords(String s) {
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
     * 反转字符串
     *
     * @param s
     */
    public static void reverseString(char[] s) {
        for (int i = 0, j = s.length - 1; i < j; i++, j--) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }
    }

    /**
     * 编写一个函数来查找字符串数组中的最长公共前缀。
     * <p>
     * 如果不存在公共前缀，返回空字符串 ""。
     *
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
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


    // 构建部分匹配表（前缀函数）
    public static int[] buildNextTable(String pattern) {
        int m = pattern.length();
        int[] next = new int[m];
        int j = 0; // 前缀的长度

        for (int i = 1; i < m; i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];  // 如果不匹配，回溯
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
        return next;
    }

    /**
     * 实现 strStr()
     * 给你两个字符串haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。如果needle 不是 haystack 的一部分，则返回 -1 。
     *
     * @param text
     * @param pattern
     * @return
     */
    // KMP匹配
    public static int kmpSearch(String text, String pattern) {
        text.indexOf(pattern);
        int[] next = buildNextTable(pattern);
        int n = text.length();
        int m = pattern.length();
        int j = 0;  // 模式字符串的指针

        for (int i = 0; i < n; i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = next[j - 1];  // 如果不匹配，回溯
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == m) {
                return i - m + 1;  // 找到匹配，返回匹配的起始位置
            }
        }
        return -1;  // 如果没有找到匹配，返回-1
    }

    /**
     * 给定长度为 2n 的整数数组 nums ，你的任务是将这些数分成 n 对, 例如 (a1, b1), (a2, b2), ..., (an, bn) ，使得从 1 到 n 的 min(ai, bi) 总和最大。
     * <p>
     * 返回该 最大总和 。
     * <p>
     * 输入：nums = [1,4,3,2]
     * 输出：4
     * 解释：所有可能的分法（忽略元素顺序）为：
     * 1. (1, 4), (2, 3) -> min(1, 4) + min(2, 3) = 1 + 2 = 3
     * 2. (1, 3), (2, 4) -> min(1, 3) + min(2, 4) = 1 + 2 = 3
     * 3. (1, 2), (3, 4) -> min(1, 2) + min(3, 4) = 1 + 3 = 4
     * 所以最大总和为 4
     * <p>
     * 输入：nums = [6,2,6,5,1,2]
     * 输出：9
     * 解释：最优的分法为 (2, 1), (2, 5), (6, 6). min(2, 1) + min(2, 5) + min(6, 6) = 1 + 2 + 6 = 9
     *
     * @param nums
     * @return
     */
    public static int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int sum = 0;
        for (int i = 0; i < nums.length; i = i + 2) {
            sum = sum + nums[i];
        }
        return sum;
    }

    /**
     * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按 非递减顺序排列  ，请你从数组中找出满足相加之和等于目标数 target 的两个数。如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，则 1 <= index1 < index2 <= numbers.length 。
     * <p>
     * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
     * <p>
     *
     * @param numbers
     * @param target
     * @return
     */
    public static int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            if (numbers[left] + numbers[right] < target) {
                left++;
            }
            if (numbers[left] + numbers[right] > target) {
                right--;
                continue;
            }
            if (numbers[left] + numbers[right] == target) {
                return new int[]{left, right};
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * 如果nums没有呗排序的情况
     *
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(nums[i]), map.get(target - nums[i])};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    /**
     * 移除nums中等于val的元素
     *
     * @param nums
     * @param val
     * @return
     */
    public int removeElement(int[] nums, int val) {
        int k = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[k] = nums[i];
                k++;
            }
        }
        return k;
    }

    /**
     * 给定一个二进制数组 nums ， 计算其中最大连续 1 的个数。
     * <p>
     *  
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums = [1,1,0,1,1,1]
     * 输出：3
     * 解释：开头的两位和最后的三位都是连续 1 ，所以最大连续 1 的个数是 3.
     * 示例 2:
     * <p>
     * 输入：nums = [1,0,1,1,0,1]
     * 输出：2
     *
     * @param nums
     * @return
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int n = 0, result = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) {
                n++;
                if (n > result) {
                    result = n;
                }
            } else {
                n = 0;
            }
        }
        return result;
    }

    /**
     * 给定一个含有 n 个正整数的数组和一个正整数 target 。
     * <p>
     * 找出该数组中满足其总和大于等于 target 的长度最小的 子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。
     * <p>
     * 输入：target = 7, nums = [2,3,1,2,4,3]
     * 输出：2
     * 解释：子数组 [4,3] 是该条件下的长度最小的子数组。
     *
     * @param target
     * @param nums
     * @return
     */
    public static int minSubArrayLen(int target, int[] nums) {
        int k = Integer.MAX_VALUE;
        int sum = 0;
        int i = 0, j = 0;
        while (j < nums.length) {
            sum = sum + nums[j];
            while (sum >= target) {
                k = Math.min(k, j - i + 1);
                sum = sum - nums[i];
                i++;
            }
            j++;
        }
        return k == Integer.MAX_VALUE ? 0 : k;
    }

    /**
     * 检测字符串无重复字符的最长子串的长度
     *
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Set<Character> hashSet = new HashSet();
        int maxLength = 0;
        for (int left = 0, right = 0; right < s.length(); right++) {
            char cur = s.charAt(right);
            while (hashSet.contains(cur)) {
                //如果有重复元素，就把这个重复元素之前的都去掉
                hashSet.remove(s.charAt(left));
                left++;
            }
            hashSet.add(cur);
            maxLength = Math.max(maxLength, hashSet.size());
        }
        return maxLength;
    }

}
