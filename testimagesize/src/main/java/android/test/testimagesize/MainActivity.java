package android.test.testimagesize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ImageView imageView2;

    /**
     * 图片的实际尺寸
     */
    private static int WIDTH = 1136;
    private static int HEIGHT = 640;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        imageView2 = findViewById(R.id.image_view_02);
        /**
         * 加载图片结果与计算结果相同，
         * 加载jpg图片时，RGB_565只有ARGB_8888的一半
         * 但是加载png图片时，RGB_565和ARGB_8888的大小一样，一个像素还是占有4位（为啥会这样呢？？？）
         * 新建bitmap时可以在ARGB8888和REG565之间转换，对应的占用内存也会有相应的变化
         */
        testAssetsSize();
        testSizeForHdpi();
        testSizeForXhdpi();
        testSizeForXxhdpi();
    }

    private Bitmap creatBitmap(Bitmap bitmap) {
        Bitmap canvasBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(canvasBmp);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        return canvasBmp;
    }


    private void testAssetsSize() {
        Log.i("liao", "testAssetsSize");
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("jpg_assets.jpg"), null, options);
            Log.i("liao", "width:height = " + bitmap.getWidth() + " : " + bitmap.getHeight());
            imageView.setImageBitmap(bitmap);
            //565，一个像素占2位
            Log.d("liao", "565 jpg calculate = " + bitmap.getWidth() * bitmap.getHeight() * 2);
            Log.d("liao", "scaled bitmap.getByteCount() = " + bitmap.getByteCount());
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("jpg_assets.jpg"), null, options);
            Log.d("liao", "8888 jpg calculate = " + bitmap.getWidth() * bitmap.getHeight() * 4);
            Log.d("liao", "scaled bitmap.getByteCount() = " + bitmap.getByteCount());
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("png_assets.png"), null, options);
            Log.d("liao", "565 png calculate = " + bitmap.getWidth() * bitmap.getHeight() * 2);
            Log.d("liao", "scaled bitmap.getByteCount() = " + bitmap.getByteCount());
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("png_assets.png"), null, options);
            Log.d("liao", "8888 png calculate = " + bitmap.getWidth() * bitmap.getHeight() * 4);
            Bitmap newBitmap = creatBitmap(bitmap);
            imageView2.setImageBitmap(newBitmap);
            Log.d("liao", "new bitmap.getByteCount() = " + newBitmap.getByteCount());
            Log.d("liao", "scaled bitmap.getByteCount() = " + bitmap.getByteCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testSizeForHdpi() {
        Log.i("liao", "test drawable hdpi " + getScreenDpi());
        int defDpi = 240;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.png_h, options);
        Log.i("liao", "width:height = " + bmp.getWidth() + " : " + bmp.getHeight());
        //hdpi文件夹下相当于240dpi，缩放到对应屏幕需要计算scale比例，8888一个像素占4位
        Log.d("liao", "calculate png 8888= " + WIDTH * getScreenDpi() / defDpi
                * HEIGHT * (getScreenDpi() / defDpi) * 4);
        Log.d("liao", "calculate png 8888= " + bmp.getWidth() * bmp.getHeight() * 4);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.png_h, options);
        Log.d("liao", "calculate png 565= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 2);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_h, options);
        Log.d("liao", "calculate jpg 565= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 2);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_h, options);
        Log.d("liao", "calculate jpg 8888= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 4);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
    }

    private void testSizeForXhdpi() {
        Log.i("liao", "test drawable xhdpi " + getScreenDpi());
        float defDpi = 320f;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.png_xh, options);
        Log.i("liao", "width:height = " + bmp.getWidth() + " : " + bmp.getHeight());
        //hdpi文件夹下相当于240dpi，缩放到对应屏幕需要计算scale比例，8888一个像素占4位
        Log.d("liao", "calculate png 8888= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 4);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.png_xh, options);
        Log.d("liao", "calculate png 565= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 2);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_xh, options);
        Log.d("liao", "calculate jpg 565= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 2);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_xh, options);
        Log.d("liao", "calculate jpg 8888= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 4);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
    }

    private void testSizeForXxhdpi() {
        Log.i("liao", "test drawable xxhdpi " + getScreenDpi());
        int defDpi = 480;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.png_xxh, options);
        Log.i("liao", "width:height = " + bmp.getWidth() + " : " + bmp.getHeight());
        //hdpi文件夹下相当于240dpi，缩放到对应屏幕需要计算scale比例，8888一个像素占4位
        Log.d("liao", "calculate png 8888= " + WIDTH * getScreenDpi() / defDpi
                * HEIGHT * (getScreenDpi() / defDpi) * 4);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.png_xxh, options);
        Log.d("liao", "calculate png 565= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 2);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_xxh, options);
        Log.d("liao", "calculate jpg 565= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 2);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_xxh, options);
        Log.d("liao", "calculate jpg 8888= " + WIDTH * (getScreenDpi() / defDpi)
                * HEIGHT * (getScreenDpi() / defDpi) * 4);
        Log.d("liao", "scaled bitmap.getByteCount() = " + bmp.getByteCount());
    }

    /**
     * 获取屏幕密度
     *
     * @return 160/240/320/480
     */
    private int getScreenDpi() {
        int dpi = getResources().getDisplayMetrics().densityDpi;
        return dpi;
    }
}
