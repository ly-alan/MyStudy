package com.roger.main.youtube.y2meta;


import android.text.TextUtils;
import android.util.Log;

import com.cv.media.c.trailer.youtube.DefaultYouTubeParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * y2mate.com网站数据爬取
 */
public class Y2MetaParser {

    private static final String TAG = "Y2MetaParser";

    private static final String URL_ANALYZE_AJAX = "https://www.y2mate.com/mates/analyzeV2/ajax";
    private static final String URL_CONVERT_INDEX = "https://www.y2mate.com/mates/convertV2/index";

    /**
     * 获取youtube下载地址
     *
     * @param videoId
     * @return
     */
    public static String getDownloader(String videoId) {
        try {
            String key = requestAjax(URL_ANALYZE_AJAX, DefaultYouTubeParser.YOUTUBE_WEB_URL + videoId);
            if (TextUtils.isEmpty(key)) {
                return "";
            }
            return requestIndex(URL_CONVERT_INDEX, videoId, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * 发送ajax请求
     *
     * @param api_url
     * @param request
     * @return
     * @throws IOException
     */
    private static String requestAjax(String api_url, String request) throws Exception {
        String urlParameters = "k_query=" + request + "&k_page=home&hl=en&k_api=analyze&q_auto=1";
        String result = requestPost(api_url, urlParameters);
        if (TextUtils.isEmpty(result)) {
            return "";
        }

        Log.d(TAG, "Response: " + result);
        // 解析JSON并提取下载链接
        JSONObject jsonObject = new JSONObject(result);
        // 具体的下载链接解析逻辑取决于API的响应结构
        JSONObject linksObject = jsonObject.getJSONObject("links");//links下面有mp4，mp3，other(audio)
        JSONObject mp4Object = linksObject.getJSONObject("mp4");
        Map<String, String> mp4Map = parseMp4Key(mp4Object);
        if (mp4Map.containsKey("720p")) {
            return mp4Map.get("720p");
        }
        if (mp4Map.containsKey("1080p")) {
            return mp4Map.get("1080p");
        }
        if (mp4Map.containsKey("480p")) {
            return mp4Map.get("480p");
        }
        if (mp4Map.containsKey("360p")) {
            return mp4Map.get("360p");
        }
        return "";
    }


    public static Map<String, String> parseMp4Key(JSONObject mp4Object) throws JSONException {
        Map<String, String> mp4Map = new HashMap<>();
        Iterator<String> keys = mp4Object.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            //see Y2MetaModel
            JSONObject value = (JSONObject) mp4Object.getJSONObject(key);
            mp4Map.put(value.getString("q"), value.getString("k"));
        }
        return mp4Map;
    }

    /**
     * 获取下载地址
     *
     * @param api_url
     * @param videoId
     * @param k
     * @return
     * @throws Exception
     */
    private static String requestIndex(String api_url, String videoId, String k) throws Exception {
        String urlParameters = "vid=" + videoId + "&k=" + k;
        String result = requestPost(api_url, urlParameters);
        if (TextUtils.isEmpty(result)) {
            return "";
        }
        // 解析JSON并提取下载链接
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.getString("dlink");
    }


    /**
     * @param api_url       请求url
     * @param urlParameters 请求体
     * @return
     */
    private static String requestPost(String api_url, String urlParameters) {
        try {
            // 构建URL
            URL url = new URL(api_url);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
            connection.setDoOutput(true);

            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = urlParameters.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 获取响应代码
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Log.d(TAG, "request: " + api_url);
                return "";
            }

            // 读取响应
            StringBuilder response = new StringBuilder();
            InputStream in = connection.getInputStream();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in, "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            in.close();
            connection.disconnect();// 关闭连接
            // 输出结果
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
