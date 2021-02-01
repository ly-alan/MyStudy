package com.roger.tvmodule.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.commonlib.base.BaseFragment;
import com.roger.tvmodule.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class FileFragment extends BaseFragment implements View.OnClickListener {
    private static final String KEY_TAG = "FileFragment";

    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    @Override
    public void initView() {
        super.initView();
        textView = getView().findViewById(R.id.text);
        LinearLayout linearLayout = getView().findViewById(R.id.layout_btn);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            try {
                linearLayout.getChildAt(i).setOnClickListener(this);
            } catch (Exception e) {
                Log.e(KEY_TAG, "setOnClickListener error : " + i + "  : " + e.getMessage());
            }
        }
    }

    public static boolean isAndroidQFileExists(Context context, String path) {
        AssetFileDescriptor afd = null;
        ContentResolver cr = context.getContentResolver();
        try {
            Uri uri = Uri.parse(path);
            afd = cr.openAssetFileDescriptor(uri, "r");
            if (afd == null) {
                return false;
            } else {
                try {
                    afd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            try {
                afd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void onClick(View view) {
        File file = Environment.getExternalStorageDirectory();
        String path = "";
        switch (view.getId()) {
            case R.id.btn_getFile:
                getFileInfo();
                break;
            case R.id.btn_createFile:
                //别人的分享https://zhuanlan.zhihu.com/p/128558892
                //10.0以下无需动态获取权限 ；
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    file = getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
                    path = file.getPath();
                    createFile(path + File.separator + "atest.txt");
                    //10.0以下 未动态获取权限时，无法创建文件，10.0无法创建文件
                    file = Environment.getExternalStoragePublicDirectory("aTestDir");
                    path = file.getPath();
                    createFile(path + File.separator + "atest.txt");
                    //10.0以下 未动态获取权限时，无法创建文件,
                    file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    path = file.getPath();
                    createFile(path + File.separator + "atest.txt");
                    //10.0以下 未动态获取权限时，无法创建文件,10.0无法创建文件
                    file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    path = file.getPath();
                    createFile(path + File.separator + getContext().getPackageName() + File.separator + "atest.txt");
                } else {
                    //android Q以上，
                    //在私有目录下新建文件，可以使用newFile的方式
                    file = getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
                    path = file.getPath();
                    createFile(path + File.separator + "atest.txt");

                    //沙盒目录存储
                    createFile_10("new_text.txt");
                }
                break;
            case R.id.btn_deleteFile:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    //10.0以下无需动态获取权限
                    file = getContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
                    path = file.getPath();
                    deleteFile(path + File.separator + "atest.txt");
                    //10.0以下 未动态获取权限时，无法创建文件
                    file = Environment.getExternalStoragePublicDirectory("aTestDir");
                    path = file.getPath();
                    deleteFile(path + File.separator + "atest.txt");
                    //10.0以下 未动态获取权限时，无法创建文件
                    file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    path = file.getPath();
                    deleteFile(path + File.separator + "atest.txt");
                    //自己再目录下放一个文件，再删除,10.0以下也不能删除成功
                    file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    path = file.getPath();
                    deleteFile(path + File.separator + "_HOT_211_uat.apk");
                }
                break;
            case R.id.btn_writeFile:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    path = file.getPath();
                    copy(path + File.separator + "RED_HOT_211beta10_uat.apk", path + File.separator + "_HOT_211_uat.apk");
                } else {
//                    copy_10();
                }
                break;
            case R.id.btn_readFile:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file2 = Environment.getExternalStoragePublicDirectory("aTestDir");
                    path = file.getPath();
                    copy(path + File.separator + "RED_HOT_211beta10_uat.apk", path + File.separator + "_HOT_211_uat.apk");
                    copy(path + File.separator + "RED_HOT_211beta10_uat.apk", file1.getPath() + File.separator + "atest.txt");
                    copy(path + File.separator + "RED_HOT_211beta10_uat.apk", file2.getPath() + File.separator + "atest.txt");
                } else {
                    readFile_10("new_text.txt");
                }
                break;
        }
    }

    /**
     * 获取文件信息
     * getDataDir:
     * /data/user/0/com.roger.tvmodule
     * getCacheDir:
     * /data/user/0/com.roger.tvmodule/cache
     * getCodeCacheDir:
     * /data/user/0/com.roger.tvmodule/code_cache
     * getExternalCacheDir:
     * /storage/emulated/0/Android/data/com.roger.tvmodule/cache
     * getExternalCacheDirs:
     * /storage/emulated/0/Android/data/com.roger.tvmodule/cache
     * getExternalFilesDir:
     * /storage/emulated/0/Android/data/com.roger.tvmodule/files/myDir
     * /storage/emulated/0/Android/data/com.roger.tvmodule/files/Music
     * /storage/emulated/0/Android/data/com.roger.tvmodule/files/Pictures
     * /storage/emulated/0/Android/data/com.roger.tvmodule/files/Movies
     * getExternalFilesDirs:
     * /storage/emulated/0/Android/data/com.roger.tvmodule/files/Pictures
     * getExternalMediaDirs:
     * /storage/emulated/0/Android/media/com.roger.tvmodule
     * getDataDirectory:
     * /data
     * getDownloadCacheDirectory:
     * /data/cache
     * getExternalStorageDirectory:
     * /storage/emulated/0
     * getExternalStoragePublicDirectory:
     * /storage/emulated/0/myDir
     * /storage/emulated/0/Music
     * /storage/emulated/0/Pictures
     * /storage/emulated/0/Movies
     * /storage/emulated/0/Download
     * /storage/emulated/0/DCIM
     * /storage/emulated/0/Documents
     */
    private void getFileInfo() {
        StringBuilder sb = new StringBuilder();
//        try {
//            sb.append("getDataDir:").append("\n").append(getContext().getDataDir()).append("\n");
//        } catch (Exception e) {
//        }
        sb.append("getCacheDir:").append("\n").append(getContext().getCacheDir()).append("\n");
//        sb.append("getCodeCacheDir:").append("\n").append(getContext().getCodeCacheDir()).append("\n");
        sb.append("getExternalCacheDir:").append("\n").append(getContext().getExternalCacheDir()).append("\n");
//        sb.append("getExternalCacheDirs:").append("\n").append(getExternalCacheDirs());
        sb.append("getExternalFilesDir:").append("\n").append(getExternalFilesDir());
        sb.append("getExternalFilesDirs:").append("\n").append(getExternalFilesDirs());
        sb.append("getExternalMediaDirs:").append("\n").append(getExternalMediaDirs());
//        sb.append("getDataDirectory:").append("\n").append(Environment.getDataDirectory()).append("\n");
//        sb.append("getDownloadCacheDirectory:").append("\n").append(Environment.getDownloadCacheDirectory()).append("\n");
        sb.append("getExternalStorageDirectory:").append("\n").append(Environment.getExternalStorageDirectory()).append("\n");
        sb.append("getExternalStoragePublicDirectory:").append("\n").append(getExternalStoragePublicDirectory()).append("\n");
        textView.setText(sb.toString());
        Log.d("liao", sb.toString());
    }

    /**
     * 在各目录下创建文件
     */
    private void createFile(String path) {
        boolean isSuccess = false;
        File file = new File(path);
        if (!file.exists()) {
        } else {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            isSuccess = file.createNewFile();
        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(KEY_TAG, "create File error : " + e.getMessage());
        }
        Log.d(KEY_TAG, "create File : " + path + " : " + isSuccess);
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void createFile_10(String fileName) {
        //sd卡上共享目录Download，DCI等需要使用MediaStore的方式
        ContentResolver resolver = getContext().getApplicationContext().getContentResolver();
        ContentValues values = new ContentValues();
        //设置文件名称
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
//                   //设置文件类型
        values.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");
//                    values.put(MediaStore.Downloads.MIME_TYPE, "*/*");
        //注意MediaStore.Downloads.RELATIVE_PATH需要targetVersion=29,
        //故该方法只可在Android10的手机上执行。
        //公共文件夹查看,https://developer.android.google.cn/training/data-storage/shared/media
        // 图片（包括照片和屏幕截图），存储在 DCIM/ 和 Pictures/ 目录中。系统将这些文件添加到 MediaStore.Images 表格中。
        //视频，存储在 DCIM/、Movies/ 和 Pictures/ 目录中。系统将这些文件添加到 MediaStore.Video 表格中。
        //音频文件，存储在 Alarms/、Audiobooks/、Music/、Notifications/、Podcasts/ 和 Ringtones/ 目录中，以及位于 Music/ 或 Movies/ 目录中的音频播放列表中。系统将这些文件添加到 MediaStore.Audio 表格中。
        //下载的文件，存储在 Download/ 目录中。在搭载 Android 10（API 级别 29）及更高版本的设备上，这些文件存储在 MediaStore.Downloads 表格中。此表格在 Android 9（API 级别 28）及更低版本中不可用。
//                    values.put(MediaStore.Downloads.RELATIVE_PATH, DIRECTORY_DOWNLOADS + File.separator + "myDir");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, DIRECTORY_DOWNLOADS);
        Uri insertUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        if (insertUri != null) {
            //若生成了uri，则表示该文件添加成功
            //使用流将内容写入该uri中即可
            try {
                OutputStream outputStream = resolver.openOutputStream(insertUri);
                if (outputStream != null) {
                    outputStream.write("ceshiwenzi".getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

////                    //官方示例，
//                    ContentResolver resolver = getContext().getContentResolver();
//// Find all audio files on the primary external storage device.
//// On API <= 28, use VOLUME_EXTERNAL instead.
//                    Uri audioCollection = MediaStore.Audio.Media.getContentUri(
//                            MediaStore.VOLUME_EXTERNAL_PRIMARY);
//// Publish a new song.
//                    ContentValues newSongDetails = new ContentValues();
//                    newSongDetails.put(MediaStore.Audio.Media.DISPLAY_NAME,
//                            "My Song.mp3");
//                    newSongDetails.put(MediaStore.Audio.Media.RELATIVE_PATH, "Video" + File.separator + "myDir" + File.separator);
//
//// Keeps a handle to the new song's URI in case we need to modify it
//// later.
//                    Uri myFavoriteSongUri = resolver
//                            .insert(audioCollection, newSongDetails);
    }

    /**
     * 删除文件
     *
     * @param path
     */
    private void deleteFile(String path) {
        File file = new File(path);
        boolean isSuccess = false;
        if (file.exists()) {
            isSuccess = file.delete();
        }
        Log.d(KEY_TAG, "delete File : " + path + " : " + isSuccess);
    }

    private void writeFile(String path) {

    }

    private void readFile(String path) {

    }

    private void readFile_10(String fileName) {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Downloads._ID, MediaStore.Downloads.MIME_TYPE, MediaStore.Downloads.DISPLAY_NAME, MediaStore.Downloads.RELATIVE_PATH},
                MediaStore.Downloads.DISPLAY_NAME + " = '" + fileName + "'",
                null,
                null);

        List<Uri> fileUri = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int fileId = cursor.getInt(cursor.getColumnIndex(MediaStore.Downloads._ID));
                Uri uri = Uri.withAppendedPath(MediaStore.Downloads.EXTERNAL_CONTENT_URI, "" + fileId);

                String tempPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.RELATIVE_PATH));
                Log.d(KEY_TAG, "uri = " + uri.toString() + "   ,path : " + tempPath);

                fileUri.add(uri);
            }
        }
        for (Uri uri : fileUri) {
            try {
                InputStream inStream = getContext().getContentResolver().openInputStream(uri);
                StringBuilder result = new StringBuilder();
                byte[] buffer = new byte[inStream.available()];
                int s = 0;
//                while ((s = inStream.read(buffer)) != -1) {//使用readLine方法，一次读一行
//                    result.append(new String(buffer));
//                }
//                Log.d(KEY_TAG, "result = " + result.toString());
                inStream.read(buffer);
                Log.d(KEY_TAG, "result = " + new String(buffer));
                inStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * copy包括读和写
     *
     * @param srcPath
     * @param destPath
     */
    private void copy(String srcPath, String destPath) {
        boolean isSuccess = true;
        try {
            File dest = new File(destPath);
            if (dest.exists()) {
                dest.delete();
            }
            FileInputStream inStream = new FileInputStream(new File(srcPath));
            FileOutputStream outStream = new FileOutputStream(new File(destPath));
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }
        Log.d(KEY_TAG, "copy : " + isSuccess);
    }

    private void copy_10(Uri uri, String fileName) {
        try {
            InputStream inStream = getContext().getContentResolver().openInputStream(uri);
            File temDir = getContext().getExternalFilesDir("temp");
            if (inStream != null && temDir != null) {
                File dest = new File(temDir.getPath() + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(dest);

                if (inStream instanceof FileInputStream) {
                    FileInputStream fin = (FileInputStream) inStream;
                    FileChannel inChannel = fin.getChannel();
                    FileChannel outChannel = fos.getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    inChannel.close();
                    outChannel.close();
                } else {
                    BufferedInputStream bis = new BufferedInputStream(inStream);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    byte[] byteArray = new byte[1024];
                    int bytes = 0;
                    while ((bytes = bis.read(byteArray)) != -1) {
                        bos.write(byteArray, 0, bytes);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private StringBuilder getExternalCacheDirs() {
        StringBuilder sb = new StringBuilder();
        File[] files = getContext().getExternalCacheDirs();
        for (File file : files) {
            sb.append(file.getPath());
            sb.append("\n");
        }
        return sb;
    }

    private StringBuilder getExternalFilesDir() {
        StringBuilder sb = new StringBuilder();
        String[] types = new String[]{
                "myDir",
                Environment.DIRECTORY_MUSIC,
//                Environment.DIRECTORY_PODCASTS,
//                Environment.DIRECTORY_RINGTONES,
//                Environment.DIRECTORY_ALARMS,
//                Environment.DIRECTORY_NOTIFICATIONS,
//                Environment.DIRECTORY_PICTURES,
                Environment.DIRECTORY_MOVIES,
        };
        for (String type : types) {
            File file = getContext().getExternalFilesDir(type);
            sb.append(file.getPath());
            sb.append("\n");
        }
        return sb;
    }

    private StringBuilder getExternalFilesDirs() {
        StringBuilder sb = new StringBuilder();
        File[] files = getContext().getExternalFilesDirs(Environment.DIRECTORY_PICTURES);
        for (File file : files) {
            sb.append(file.getPath());
            sb.append("\n");
        }
        return sb;
    }


    private StringBuilder getExternalMediaDirs() {
        StringBuilder sb = new StringBuilder();
        File[] files = getContext().getExternalMediaDirs();
        for (File file : files) {
            sb.append(file.getPath());
            sb.append("\n");
        }
        return sb;
    }


    private StringBuilder getExternalStoragePublicDirectory() {
        StringBuilder sb = new StringBuilder();
        String[] types = new String[]{
                "myDir",
                Environment.DIRECTORY_MUSIC,
//                Environment.DIRECTORY_PICTURES,
//                Environment.DIRECTORY_MOVIES,
                Environment.DIRECTORY_DOWNLOADS,
                Environment.DIRECTORY_DCIM,
                DIRECTORY_DOCUMENTS,
        };
        for (String type : types) {
            File file = Environment.getExternalStoragePublicDirectory(type);
            sb.append(file.getPath());
            sb.append("\n");
        }
        return sb;
    }


}
