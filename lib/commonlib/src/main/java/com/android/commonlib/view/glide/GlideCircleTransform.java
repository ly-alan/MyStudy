package com.android.commonlib.view.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.PAINT_FLAGS;

/**
 * 圆形图片（可以带圆环）
 * Created by liaoyong on 2017/9/27.
 */

public class GlideCircleTransform extends BitmapTransformation {

    /**
     * 圆弧宽度
     */
    private float mBorderWidth;

    /**
     * 画圆环
     */
    private Paint mBorderPaint;


    public GlideCircleTransform() {
        this(0, 0);
    }

    public GlideCircleTransform(float borderWidth, int borderColor) {
        this.mBorderWidth = borderWidth;
        if (mBorderWidth > 0) {
            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        //圆形图片
        Bitmap bitmap = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
        if (mBorderWidth > 0) {
            //绘制圆环
            bitmap = circleRing(bitmap, outWidth, outHeight);
        }
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }

    /**
     * 绘制圆环
     *
     * @param source    圆形图片
     * @param outWidth
     * @param outHeight
     * @return
     */
    private Bitmap circleRing(Bitmap source, int outWidth, int outHeight) {
        if (source == null) {
            return null;
        }
        int size = Math.min(outWidth, outHeight);
        Bitmap resultBmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(PAINT_FLAGS | Paint.ANTI_ALIAS_FLAG);
        Canvas canvas = new Canvas(resultBmp);
        canvas.drawBitmap(source, new Rect(0, 0, source.getWidth(), source.getWidth()),
                new Rect((int) mBorderWidth, (int) mBorderWidth, size - (int) mBorderWidth, size - (int) mBorderWidth), paint);
        // 绘制圆环
        canvas.drawCircle(size / 2, size / 2, size / 2 - mBorderWidth / 2, mBorderPaint);
        return resultBmp;
    }
}
