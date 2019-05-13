package com.android.commonlib.net;

import com.android.commonlib.net.RetrofitService;

import java.util.HashMap;
import java.util.Map;

/**
 * 亲求参数
 * Created by yong.liao on 2018/5/28 0028.
 */

public class RequestMap {

    /**
     * 获取正在上映的电影
     * @return
     */
    public static Map<String,String> getInTheaters(){
        Map<String, String> map = new HashMap<String, String>();
        map.put(RetrofitService.PATH_ACTION, "in_theaters");
        map.put("city","北京");//上映城市(default: 北京)中文，如: “北京” 或者数字ID: 108288
        return map;
    }
}
