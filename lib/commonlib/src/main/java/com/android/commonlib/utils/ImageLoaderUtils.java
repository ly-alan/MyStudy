package com.android.commonlib.utils;

import android.content.Context;
import android.graphics.Bitmap;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Px;

import com.android.commonlib.R;
import com.android.commonlib.base.BaseApplication;
import com.android.commonlib.view.glide.GlideCircleTransform;
import com.android.commonlib.view.glide.GlideCornerTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * 图片加载
 */

public class ImageLoaderUtils {

    /**
     * 默认圆角弧度 px
     */
    public static final int DEFAULT_CORNER = 10;

    /**
     * 请求添加header,一般用于ip请求
     *
     * @param url
     * @return
     */
    private static GlideUrl addHeader(String url) {
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        builder.addHeader("host", "your host");
        return new GlideUrl(url, builder.build());
    }

    /**
     * 加载图片
     *
     * @param url
     * @param target
     */
    public static void loadImage(String url, ImageView target) {
        loadImage(BaseApplication.getInstance(), url, target);
    }

    /**
     * @param context 如果是activity glide会与其生命周期关联,在onStop()中取消加载图片,如果
     *                想要始终加载图片则需要传入Application实例
     * @param url     url
     * @param target  ImageView
     */
    public static void loadImage(Context context, String url, ImageView target) {
        loadImage(context, url, R.drawable.goods_pic_default, R.drawable.goods_pic_error, target);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param place   正在加载时的图片
     * @param error   加载失败的图片
     * @param target  imageview
     */
    public static void loadImage(Context context, String url, @DrawableRes int place, @DrawableRes int error, ImageView target) {
        if (TextUtils.isEmpty(url)) {
            System.err.print("load image error , null url");
            return;
        }
        RequestOptions defaultOptions = new RequestOptions()
                .dontAnimate()
                .placeholder(place)
                .error(error);
        Glide.with(context)
                .load(url)
                .apply(defaultOptions)
                .into(target);
    }

    /**
     * @param url
     * @param target 图片加载监听
     */
    public static void loadImage(String url, SimpleTarget<Bitmap> target) {
        loadImage(BaseApplication.getInstance(), url, target);
    }

    /**
     * @param context
     * @param url
     * @param target  图片加载监听
     */
    public static void loadImage(Context context, String url, SimpleTarget<Bitmap> target) {
        if (TextUtils.isEmpty(url)) {
            System.err.print("load image error , null url");
            return;
        }
        RequestOptions defaultOptions = new RequestOptions()
                .dontAnimate()
                .placeholder(R.drawable.goods_pic_default)
                .error(R.drawable.goods_pic_error);
        Glide.with(context).asBitmap().load(url).apply(defaultOptions).into(target);
    }


    /**
     * 加载圆形图片
     *
     * @param url
     * @param target
     */
    public static void loadCircleImage(String url, ImageView target) {
        loadCircleImage(BaseApplication.getInstance(), url, target);
    }

    /**
     * 加载圆形图片
     *
     * @param context 如果是activity glide会与其生命周期关联,在onStop()中取消加载图片,如果
     *                想要始终加载图片则需要传入Application实例
     * @param url     url
     * @param target  ImageView
     */
    public static void loadCircleImage(Context context, String url, ImageView target) {
        loadCircleImage(context, url, R.drawable.goods_pic_default, R.drawable.goods_pic_error, target, 0, 0);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param target
     * @param ringWidth 圆环宽度
     * @param ringColor 圆环颜色
     */
    public static void loadCircleImage(String url, ImageView target, @Px int ringWidth, @ColorInt int ringColor) {
        loadCircleImage(BaseApplication.getInstance(), url, target, ringWidth, ringColor);
    }

    /**
     * 加载圆形图片
     *
     * @param context   如果是activity glide会与其生命周期关联,在onStop()中取消加载图片,如果
     *                  想要始终加载图片则需要传入Application实例
     * @param url       url
     * @param target    ImageView
     * @param ringWidth 圆环宽度
     * @param ringColor 圆环颜色
     */
    public static void loadCircleImage(Context context, String url, ImageView target,
                                       @Px int ringWidth, @ColorInt int ringColor) {
        loadCircleImage(context, url, R.drawable.goods_pic_default, R.drawable.goods_pic_error, target, ringWidth, ringColor);
    }

    /**
     * 加载圆形图片
     *
     * @param context
     * @param url
     * @param place   正在加载时的图片
     * @param error   加载失败的图片
     * @param target  imageview
     */
    public static void loadCircleImage(Context context, String url, @DrawableRes int place, @DrawableRes int error,
                                       ImageView target, @Px int ringWidth, @ColorInt int ringColor) {
        if (TextUtils.isEmpty(url)) {
            System.err.print("load image error , null url");
            return;
        }
        RequestOptions circleOptions = new RequestOptions()
                .dontAnimate()
                .transform(new GlideCircleTransform(ringWidth, ringColor))
                .placeholder(place)
                .error(error);
        Glide.with(context)
                .load(url)
                .apply(circleOptions)
                .into(target);
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param target
     */
    public static void loadCornerImage(String url, ImageView target) {
        loadCornerImage(BaseApplication.getInstance(), url, target, DEFAULT_CORNER);
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param corner 圆角弧度
     * @param target
     */
    public static void loadCornerImage(String url, ImageView target, @Px int corner) {
        loadCornerImage(BaseApplication.getInstance(), url, target, corner);
    }

    /**
     * @param context 如果是activity glide会与其生命周期关联,在onStop()中取消加载图片,如果
     *                想要始终加载图片则需要传入Application实例
     * @param url     url
     * @param target  ImageView
     */
    public static void loadCornerImage(Context context, String url, ImageView target, @Px int corner) {
        loadCornerImage(context, url, R.drawable.goods_pic_default, R.drawable.goods_pic_error, target, corner);
    }

    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param place   正在加载时的图片
     * @param error   加载失败的图片
     * @param target  target
     * @param corner  圆角弧度
     */
    public static void loadCornerImage(Context context, String url, @DrawableRes int place, @DrawableRes int error,
                                       ImageView target, @Px int corner) {
        if (TextUtils.isEmpty(url)) {
            System.err.print("load image error , null url");
            return;
        }
        RequestOptions cornerOptions = new RequestOptions()
                .dontAnimate()
                .transform(new RoundedCorners(corner))
                .placeholder(place)
                .error(error);
        Glide.with(context)
                .load(url)
                .apply(cornerOptions)
                .into(target);
    }
}
