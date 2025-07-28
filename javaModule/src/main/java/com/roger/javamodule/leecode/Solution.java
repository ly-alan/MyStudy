package com.roger.javamodule.leecode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Solution {

    public static void main(String[] args) {

        merge(new int[]{1, 2, 3, 0, 0, 0}, 3, new int[]{2, 5, 6}, 3);
        merge(new int[]{4, 5, 6, 0, 0, 0}, 3, new int[]{1, 2, 3}, 3);

//        addBinary("11", "1");
//
//        plusOne(new int[]{0});
//
//        //判断括号是否成对
//        System.out.println("String isValid = " + isValid("}"));
//
//        //判断是否是回文数
//        boolean isPalindrome = isPalindrome(123321);
//        boolean isPalindrome2 = isPalindrome_2(125321);
//        System.out.println(String.format("isPalindrome = %s,isPalindrome2 = %s", isPalindrome, isPalindrome2));

    }


    public static String convert(String s, int numRows) {
        if (s.length() <= 1 || numRows <= 1) {
            return s;
        }
        //有几个v字型
        int numV = s.length() / (numRows + (numRows - 2));
        int numM = s.length() % (numRows + (numRows - 2));//填入几个完整的V后还多几个
        char[] results = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            //计算第i个元素会被填入哪个位置
            int i_n = i / (numRows + (numRows - 2));//第i个元素再第n区间
            int i_index = i % (numRows + (numRows - 2));//是在区间内的第index个元素
            int i_v = i_index < numRows ? i_index : numRows - i_index % numRows;//第i个元素在第几行
            int i_count = i_index < numRows ? 0 : 1;//这一区间内，这一行的位置
            //最终的角标
            int index = 0;
            if (i_v <= numM) {
                if (i_v < numRows) {

                } else {

                }
            } else {

            }
//            results[] =s.charAt(i);
        }
        return results.toString();
    }


    /**
     * 合并两个有序数组
     *
     * @param nums1 初始长度为m+n
     * @param m     有效的数据m个，后面用0补
     * @param nums2
     * @param n
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = 0, j = 0;
        int[] sort = new int[m + n];
        while (i < m || j < n) {
            if (i == m) {
                sort[i + j] = nums2[j];
                j++;
            } else if (j == n) {
                sort[i + j] = nums1[i];
                i++;
            } else if (nums1[i] < nums2[j]) {
                sort[i + j] = nums1[i];
                i++;
            } else {
                sort[i + j] = nums2[j];
                j++;
            }
        }
        for (int k = 0; k != m + n; ++k) {
            nums1[k] = sort[k];
        }

    }

    /**
     * 爬楼梯，每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶
     *
     * @param n
     * @return
     */
    public int climbStairs(int n) {
        //会超时
//        if (n <= 2) {
//            return n;
//        }
//        return climbStairs(n - 1) + climbStairs(n - 2);
        int p = 0, q = 0, r = 1;//第0项
        for (int i = 1; i <= n; i++) {
            p = q;
            q = r;
            r = p + q;
        }
        return r;
    }


    public int mySqrt(int x) {
        int left = 0, right = x, result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (mid * mid <= x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    /**
     * 两个二进制字符串，相加
     *
     * @param a
     * @param b
     * @return
     */
    public static String addBinary(String a, String b) {
        int num_a = a.length();
        int num_b = b.length();
        int length = Math.max(num_a, num_b);
        String result = "";
        int carry = 0;
        for (int i = 0; i <= length - 1; i++) {
            char char_a = num_a - 1 - i >= 0 ? a.charAt(num_a - 1 - i) : '0';
            char char_b = num_b - 1 - i >= 0 ? b.charAt(num_b - 1 - i) : '0';

            if (char_a == '0' && char_b == '0') {
                result = carry + result;
                carry = 0;
            } else if (char_a == '1' && char_b == '1') {
                result = carry + result;
                carry = 1;
            } else {
                if (carry == 0) {
                    result = 1 + result;
                    carry = 0;
                } else {
                    result = 0 + result;
                    carry = 1;
                }
            }
        }
        if (carry == 1) {
            result = carry + result;
        }

        return result;
    }


    /**
     * 给数字加1
     *
     * @param digits
     * @return
     */
    public static int[] plusOne(int[] digits) {
        int length = digits.length;
        for (int i = length - 1; i >= 0; i--) {
            if (digits[i] != 9) {
                //找到一个不为9的数+1后返回
                digits[i] = digits[i] + 1;
                return digits;
            } else {
                //为9，置为0，走下一位
                digits[i] = 0;
            }
        }
        //如果走到这里，说明需要扩充长度，其余位数全为0
        int[] result = new int[length + 1];
        result[0] = 1;
        return result;
    }


    /**
     * 最后一个单词的长度
     *
     * @param s
     * @return
     */
    public int lengthOfLastWord(String s) {
        if (s == null) {
            return 0;
        }
        String[] result = s.trim().split("\\s+");

        return result[result.length - 1].length();
    }


    /**
     * 移除非严格递增排列数组中的重复数
     *
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        int index = 0;
        int length = nums.length;
        for (int j = 0; j < length; j++) {
            if (j > index) {
                if (nums[j] > nums[index]) {
                    index++;
                    nums[index] = nums[j];
                }
            }
        }
        return index + 1;
    }


    /**
     * 判断数字是否是回文数
     *
     * @param x
     * @return
     */
    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        String str = String.valueOf(x);
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (i >= length - i - 1) {
                return true;
            }
            if (str.charAt(i) != str.charAt(length - i - 1)) {
                return false;
            }
        }
        return false;
        //或者直接字符串翻转
//        StringBuilder sb = new StringBuilder(x);
//        return sb.toString().equals(sb.reverse().toString());
    }

    public static boolean isPalindrome_2(int x) {
        // 特殊情况：
        // 如上所述，当 x < 0 时，x 不是回文数。
        // 同样地，如果数字的最后一位是 0，为了使该数字为回文，
        // 则其第一位数字也应该是 0
        // 只有 0 满足这一属性
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        int revertedNumber = 0;
        while (x > revertedNumber) {
            //x>revertedNumber说明一般来说位数大了，说明翻转超过了一半的数字（比较特别的案例 125321）
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        // 当数字长度为奇数时，我们可以通过 revertedNumber/10 去除处于中位的数字。
        // 例如，当输入为 12321 时，在 while 循环的末尾我们可以得到 x = 12，revertedNumber = 123，
        // 由于处于中位的数字不影响回文（它总是与自己相等），所以我们可以简单地将其去除。
        return x == revertedNumber || x == revertedNumber / 10;
    }

    /**
     * 罗马字符转化为数字
     *
     * @param s
     * @return
     */
    public static int romanToInt(String s) {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int result = 0;
        int length = s.length();
        for (int i = 0; i < length; i++) {
            int value = map.get(s.charAt(i));
            if (i < length - 1 && value < map.get(s.charAt(i + 1))) {
                result = result - value;
            } else {
                result = result + value;
            }
        }
        return result;
    }

    /**
     * 验证括号是否成对
     *
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        HashMap<Character, Character> map = new HashMap<Character, Character>() {
            {
                put(')', '(');
                put(']', '[');
                put('}', '{');
            }
        };
        Stack<Character> stack = new Stack<>();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char value = s.charAt(i);
            if (map.containsKey(value)) {
                //右括号,看看栈顶是不是左括号，
                if (stack.isEmpty() || stack.peek() != map.get(value)) {
                    //如果不是对应的左括号，说明不成对，直接返回false
                    return false;
                }
                //如果是对应的左括号，说明是成对的，移除栈顶元素
                stack.pop();
            } else {
                //左括号，直接加进去
                stack.push(value);
            }
        }
        return stack.empty();
    }


    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        } else if (list2 == null) {
            return list1;
        } else if (list1.val < list2.val) {
            list1.next = mergeTwoLists(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoLists(list1, list2.next);
            return list2;
        }

    }

}
