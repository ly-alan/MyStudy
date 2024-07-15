package com.roger.main.youtube;

import android.text.TextUtils;
import android.util.Log;


import com.roger.main.youtube.lib.DexHotPlugin;
import com.roger.main.youtube.lib.IYouTubeParser;


import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YouTubeData {
    private static final String TAG = YouTubeData.class.getSimpleName();
    public static final String PARSER_EXT_CLASS_NAME = "com.cv.media.extension.YouTubeParserExt";
    static final String YOUTUBE_VIDEO_INFORMATION_URL = "http://www.youtube.com/get_video_info?html5=1&c=TVHTML5&cver=6.20180913&video_id=";
    static final String YOUTUBE_WEB_URL = "https://www.youtube.com/watch?v=";

    private static IYouTubeParser sDefYouTubeParser;

    static {
        sDefYouTubeParser = new DefaultYouTubeParser();
    }

    public static boolean isYouTubeUrl(String youtubeUrl) {
        //invalidate url
        if (StringUtils.isEmpty(youtubeUrl)
                || (!StringUtils.startsWithIgnoreCase(youtubeUrl, "http") && !StringUtils.startsWithIgnoreCase(youtubeUrl, "https"))) {
            return false;
        }
        String expression = "^.*((youtu.be\\/)|(www.youtube.com\\/)).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeUrl);
        return matcher.matches();
    }

    public static String getYouTubeVideoId(String youtubeUrl) {
        String videoId = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0
                && (StringUtils.startsWithIgnoreCase(youtubeUrl, "http") || StringUtils.startsWithIgnoreCase(youtubeUrl, "https"))) {
            String expression = "^.*((youtu.be\\/)|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(youtubeUrl);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    videoId = groupIndex1;
            }
        }
        return videoId;
    }

    /**
     * Calculate the YouTube URL to load the video.  Includes retrieving a token that YouTube
     * requires to play the video.
     *
     * @param quality  quality of the video.  17=low, 18=high
     * @param fallback whether to fallback to lower quality in case the supplied quality is not available
     * @param videoId  the id of the video
     * @return the url string that will retrieve the video
     * @throws IOException
     */
    public static String calculateYouTubeUrl(String quality, boolean fallback, String videoId) throws IOException {
        String uriStr = null;
        try {
            uriStr = getRealYouTubeUrl(YOUTUBE_VIDEO_INFORMATION_URL + videoId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Return the URI string. It may be null if the format (or a fallback format if enabled)
        // is not found in the list of formats for the video
        String decodeUrl = null;
        if (uriStr != null) {
            decodeUrl = URLDecoder.decode(uriStr, "utf-8");
        }
        return decodeUrl;
    }

    public static int getSupportedFallbackId(int oldId) {
        final int supportedFormatIds[] = {13,  //3GPP (MPEG-4 encoded) Low quality
                17,  //3GPP (MPEG-4 encoded) Medium quality
                18,  //MP4  (H.264 encoded) Normal quality
                22,  //MP4  (H.264 encoded) High quality
                37   //MP4  (H.264 encoded) High quality
        };
        int fallbackId = oldId;
        for (int i = supportedFormatIds.length - 1; i >= 0; i--) {
            if (oldId == supportedFormatIds[i] && i > 0) {
                fallbackId = supportedFormatIds[i - 1];
            }
        }
        return fallbackId;
    }

    public static String getYoutubeWebUrl(String videoId) {
        if (videoId != null && videoId.startsWith("http")) {
            //已经是http开头的youtube地址
            return videoId;
        }
        return YOUTUBE_WEB_URL + videoId;
    }

//    public static OkHttpClient getHttpClient() {
//        OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();
//        //10mb
//        int cacheSize = 10 * 1024 * 1024;
//        File cacheLocation = DirectoryUtils.getOwnCacheDirectory(ContextProvider.getContext(), "hot/okhttp_cache");
//        cacheLocation.mkdirs();
//
//        Cache cache = null;
//        try {
//            cache = new Cache(cacheLocation, cacheSize);
//        } catch (Exception e) {
//            Log.e(TAG, "getHttpClient error", e);
//        }
//        sHttpClient.cache(cache);
////        sHttpClient.addNetworkInterceptor(new StethoInterceptor());
//        return sHttpClient.build();
//    }

    public static String getYoutubeWebKey(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith(YOUTUBE_WEB_URL)) {
            return url.replace(YOUTUBE_WEB_URL, "");
        }
        return url;
    }

    public static String getRealYouTubeUrl(String origUrl) throws Exception {
        IYouTubeParser youTubeParser = DexHotPlugin.create(IYouTubeParser.class, PARSER_EXT_CLASS_NAME);
        if (youTubeParser == null) {
            Log.d(TAG, "youtube: getRealYouTubeUrl from default Youtube parser");
            return sDefYouTubeParser.getRealYouTubeUrl(origUrl);
        } else {
            Log.d(TAG, "youtube: getRealYouTubeUrl from new Youtube parser");
            return youTubeParser.getRealYouTubeUrl(origUrl);
        }
    }
}
