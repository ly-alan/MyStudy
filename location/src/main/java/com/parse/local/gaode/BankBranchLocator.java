package com.parse.local.gaode;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BankBranchLocator {
    private static final String AMAP_API_KEY = "db6a187f7a3fdfd93c4a68abe2d3f6bc";
    private static final String AMAP_API_URL = "https://restapi.amap.com/v3/place/text";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    private static final long sleepTime = 701;//访问有限制，1分钟3次

    public static AmapPoi searchBankBranch(String branchName) {
        try {
            //offset=1标识返回几条结果（1-25）
            String url = String.format("%s?key=%s&keywords=%s" + "&page_size=1",
                    AMAP_API_URL, AMAP_API_KEY,
                    java.net.URLEncoder.encode(branchName, "UTF-8"));
//            System.out.println(url);
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    AmapResponse result = gson.fromJson(responseBody, AmapResponse.class);

                    if (result.pois != null && result.pois.length > 0) {
                        AmapPoi amapPoi1 = result.pois[0];
                        Thread.sleep(sleepTime);
                        System.out.println(new Gson().toJson(amapPoi1));
                        AmapPoi amapPoi2 = searchBankBranch(branchName, amapPoi1.city);
                        System.out.println(new Gson().toJson(amapPoi2));
                        if (amapPoi1.equals(amapPoi2)) {
                            return amapPoi2;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 精确查找
     *
     * @param branchName
     * @param city
     * @return
     */
    public static AmapPoi searchBankBranch(String branchName, String city) {
        try {
            //offset=1标识返回几条结果（1-25）
            String url = String.format("%s?key=%s&keywords=%s&page_size=1&city=%s&citylimit=true",
                    AMAP_API_URL, AMAP_API_KEY,
                    java.net.URLEncoder.encode(branchName, "UTF-8")
                    , city);
//            System.out.println(url);
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    AmapResponse result = gson.fromJson(responseBody, AmapResponse.class);

                    if (result.pois != null && result.pois.length > 0) {
                        return result.pois[0];
                    }
                }
            }
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 金融行业相关类型代码
    public class AmapTypes {
        public static final String BANK = "120000";           // 金融行业-全部
        public static final String BANK_SPECIFIC = "120100";  // 银行
        public static final String ATM = "120200";            // ATM
        public static final String INSURANCE = "120300";      // 保险公司
    }

    // 高德API返回数据结构
    public static class AmapResponse {
        @SerializedName("pois")
        public AmapPoi[] pois;

        @SerializedName("status")
        public String status;
    }


}
