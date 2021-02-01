package com.roger.tvmodule;


import androidx.fragment.app.Fragment;

/**
 * Create by Roger on 2019/5/17
 */
public class FragmentFactory {

    private static final String FRAGMENT_PATH = "com.roger.tvmodule.fragment.";

    /**
     * @param fragmentName fragment全路径名
     * @return
     */
    public static Fragment creatFragment(String fragmentName) {
        Fragment fragment = null;
        try {
            Class classForName = Class.forName(FRAGMENT_PATH + fragmentName);
            fragment = (Fragment) classForName.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
