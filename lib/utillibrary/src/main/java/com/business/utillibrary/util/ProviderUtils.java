//package com.business.utillibrary.util;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//
//import com.business.utillibrary.constant.MemoryConstants;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * contentProvider处理
// */
//public class ProviderUtils {
//    /**
//     * 发送广播，更新图库
//     */
//    public void allScan(Context context) {
//        context.sendBroadcast(new Intent(
//                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//    }
//
//    /**
//     * @return :List<PictureInfo> 图片信息链表
//     * @description: 通过contentprovider获得sd卡上的图片
//     * @author:lidaqiang
//     */
//    @SuppressWarnings("unused")
//    public static <T> void getExternalImages(Context context, List<T> list) {
//        // 指定要查询的图片uri资源
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        // 获取ContentResolver
//        ContentResolver contentResolver = context.getContentResolver();
//        // 查询的字段
//        String[] projection = {MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.DATE_MODIFIED};
//        // // 条件
//        // String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "+
//        // MediaStore.Images.Media.MIME_TYPE + "=?";
//        // // 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
//        // String[] selectionArgs = {"image/jpeg", "image/png"};
//        // 排序 按修改时间
//        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
//        // 查询sd卡上的图片
//        Cursor cursor = contentResolver.query(uri, projection, null, null,
//                sortOrder);
//        List<PictureInfo> picList = new ArrayList<PictureInfo>();
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                PictureInfo pictureInfo = new PictureInfo();
//                // 获得图片的id
//                String imageID = cursor.getString(cursor
//                        .getColumnIndex(MediaStore.Images.Media._ID));
//                // 获得图片显示的名称
//                String imageName = cursor.getString(cursor
//                        .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
//                pictureInfo.setName(imageName);
//                // 获得图片的大小
//                String imageSize = cursor.getLong(cursor
//                        .getColumnIndex(MediaStore.Images.Media.SIZE) / MemoryConstants.KB) + "kb";
//                pictureInfo.setSize(imageSize);
//                // 获得图片所在的路径(可以使用路径构建URI)
//                String data = cursor.getString(cursor
//                        .getColumnIndex(MediaStore.Images.Media.DATA));
//                pictureInfo.setPath(data);
//                // 获得图片修改时间
//                long modifiedDate = cursor.getLong(cursor
//                        .getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
//                pictureInfo.setData(modifiedDate);
//                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE)) > 0) {
//                    picList.add(pictureInfo);
//                }
//            } while (cursor.moveToNext());
//            // 关闭cursor
//            cursor.close();
//        }
//        return picList;
//    }
//
//
//    /**
//     * 扫描视频文件
//     *
//     * @return 文件信息列表
//     */
//    @SuppressWarnings("unused")
//    public static <T> void getStorageVideo(Context context, List<T> list) {
//        // 指定要查询的视频uri资源
//        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        // 获取ContentResolver
//        ContentResolver contentResolver = context.getContentResolver();
//        // 查询的字段
//        String[] projection = {MediaStore.Video.Media._ID,
//                MediaStore.Video.Media.DISPLAY_NAME,
//                MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE,
//                MediaStore.Video.Media.DATE_MODIFIED};
//        // 排序 按修改时间
//        String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " desc";
//        // 查询sd卡上的视频
//        Cursor cursor = contentResolver.query(uri, projection, null, null,
//                sortOrder);
//        List<PictureInfo> videoList = new ArrayList<PictureInfo>();
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                PictureInfo pictureInfo = new PictureInfo();
//                // 获得视频的id
//                String imageID = cursor.getString(cursor
//                        .getColumnIndex(MediaStore.Images.Media._ID));
//                // 获得视频显示的名称
//                String imageName = cursor.getString(cursor
//                        .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
//                pictureInfo.setName(imageName);
//                // 获得视频的大小
//                String imageSize = cursor.getLong(cursor
//                        .getColumnIndex(MediaStore.Images.Media.SIZE) / MemoryConstants.KB) + "kb";
//                pictureInfo.setSize(imageSize);
//                // 获得视频所在的路径(可以使用路径构建URI)
//                String data = cursor.getString(cursor
//                        .getColumnIndex(MediaStore.Images.Media.DATA));
//                pictureInfo.setPath(data);
//                // 获得视频修改时间
//                long modifiedDate = cursor.getLong(cursor
//                        .getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
//                pictureInfo.setData(modifiedDate);
//                //设置选中状态
//                pictureInfo.setSelectable(false);
//                videoList.add(pictureInfo);
//            } while (cursor.moveToNext());
//            // 关闭cursor
//            cursor.close();
//        }
//        return videoList;
//    }
//
//    public class PictureInfo {
//        /**
//         * 图片名字
//         */
//        private String name;
//        /**
//         * 图片路径
//         */
//        private String path = null;
//        /**
//         * 文件修改时间.
//         */
//        private long data;
//        /**
//         * 图片的大小
//         */
//        private String size;
//        /**
//         * 是否可读.
//         */
//        private String isReadable = null;
//        /**
//         * 是否隐藏.
//         */
//        private String isHidden = null;
//    }
//}
