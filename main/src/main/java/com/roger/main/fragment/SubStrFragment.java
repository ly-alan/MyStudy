package com.roger.main.fragment;

import android.util.Log;

import com.android.commonlib.base.BaseFragment;
import com.roger.main.function.LongestCommonSubSequence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Create by Roger on 2019/8/19
 * 字符串字串
 */
public class SubStrFragment extends BaseFragment {

    @Override
    public void setView() {
        super.setView();
        Log.d("liao", Arrays.toString(intersection("495".toCharArray(), "94984".toCharArray())));
        Log.d("liao", Arrays.toString(intersection("1221".toCharArray(), "22".toCharArray())));
        Log.d("liao", Arrays.toString(intersection("132535365".toCharArray(), "123456789".toCharArray())));

        Log.d("liao", "------------------------------------------------------");

//        LongestCommonSubSequence.getLCS("495","94984");
//        LongestCommonSubSequence.getLCS("132535365","123456789");
        LongestCommonSubSequence.getLCS("ABCBDAB","BDCABA");
    }

    /**
     * 寻找两个数组中的公共元素，输出无序
     *
     * @param nums1
     * @param nums2
     * @return
     */
    private char[] intersection(char[] nums1, char[] nums2) {
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();
        for (int i = 0; i < nums1.length; i++) {
            set1.add(nums1[i]);
        }
        for (int j = 0; j < nums2.length; j++) {
            if (set1.contains(nums2[j])) {
                set2.add(nums2[j]);
            }
        }
        char[] ans = new char[set2.size()];
        int k = 0;
        Iterator it = set2.iterator();
        while (it.hasNext()) {
            ans[k] = (char) it.next();
            k++;
        }
        return ans;
    }
}
