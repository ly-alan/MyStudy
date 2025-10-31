package com.parse.local.binlist;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BankBinService {
    private static final String BINLIST_API = "https://lookup.binlist.net/";
    private static final Map<String, BankInfo> localCache = new HashMap<>();
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
    private static final Gson gson = new Gson();

    /**
     * 根据银行卡号查询银行信息
     */
    public static BankInfo getBankInfoByBin(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 6) {
            return null;
        }

        String bin = cardNumber.substring(0, 6);

        // 1. 先查本地缓存
        if (localCache.containsKey(bin)) {
            return localCache.get(bin);
        }

        // 2. 调用免费API
        try {
            Request request = new Request.Builder()
                    .url(BINLIST_API + bin)
                    .header("Accept-Version", "3")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();

                    // 使用Gson解析JSON响应
                    BinlistResult result = gson.fromJson(responseBody, BinlistResult.class);

                    BankInfo bankInfo = new BankInfo(
                            result.bank != null ? result.bank.name : "未知银行",
                            result.scheme != null ? result.scheme : "未知卡组织",
                            result.brand != null ? result.brand : "未知品牌",
                            result.type != null ? result.type : "未知类型",
                            result.country != null ? result.country.name : "未知国家",
                            result.country != null ? result.country.currency : "未知货币"
                    );

                    // 缓存结果（设置缓存有效期，比如24小时）
                    localCache.put(bin, bankInfo);
                    return bankInfo;
                } else {
                    System.out.println("API请求失败: " + response.code());
                }
            }
        } catch (Exception e) {
            System.err.println("查询银行BIN信息异常: " + e.getMessage());
        }

        return null;
    }

    /**
     * 清除缓存（可用于内存管理）
     */
    public static void clearCache() {
        localCache.clear();
    }

    /**
     * 获取缓存大小
     */
    public static int getCacheSize() {
        return localCache.size();
    }

    // Gson解析用的数据类
    public static class BinlistResult {
        @SerializedName("bank")
        public Bank bank;

        @SerializedName("scheme")
        public String scheme;

        @SerializedName("brand")
        public String brand;

        @SerializedName("type")
        public String type;

        @SerializedName("country")
        public Country country;
    }

    public static class Bank {
        @SerializedName("name")
        public String name;
    }

    public static class Country {
        @SerializedName("name")
        public String name;

        @SerializedName("currency")
        public String currency;

        @SerializedName("alpha2")
        public String alpha2; // 国家二字码
    }

    /**
     * 银行信息结果类
     */
    public static class BankInfo {
        private final String bankName;
        private final String scheme;
        private final String brand;
        private final String cardType;
        private final String country;
        private final String currency;

        public BankInfo(String bankName, String scheme, String brand, String cardType,
                        String country, String currency) {
            this.bankName = bankName;
            this.scheme = scheme;
            this.brand = brand;
            this.cardType = cardType;
            this.country = country;
            this.currency = currency;
        }

        // Getters
        public String getBankName() { return bankName; }
        public String getScheme() { return scheme; }
        public String getBrand() { return brand; }
        public String getCardType() { return cardType; }
        public String getCountry() { return country; }
        public String getCurrency() { return currency; }

        @Override
        public String toString() {
            return String.format(
                    "银行: %s, 卡组织: %s, 品牌: %s, 类型: %s, 国家: %s, 货币: %s",
                    bankName, scheme, brand, cardType, country, currency
            );
        }
    }
}
