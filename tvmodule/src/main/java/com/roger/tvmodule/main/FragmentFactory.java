package com.roger.tvmodule.main;

import android.support.v4.app.Fragment;

/**
 * Create by Roger on 2019/5/17
 */
public class FragmentFactory {

    /**
     * @param fragmentName fragment全路径名
     * @return
     */
    public static Fragment creatFragment(String fragmentName) {
        Fragment fragment = null;
        try {
            Class classForName = Class.forName(fragmentName);
            fragment = (Fragment) classForName.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
