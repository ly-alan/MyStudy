package com.android.commonlib.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageButton;

import com.android.commonlib.R;
import com.android.commonlib.utils.ResUtils;


/**
 * 显示图片或者文字
 * Created by yong.liao on 2018/3/29 0029.
 */
public class ImageTextButton extends AppCompatImageButton {
    /**
     * 文本
     */
    private String mText;
    /**
     * 字体颜色
     */
    private ColorStateList mColor;
    /**
     * 字体大小
     */
    private int mSize;
    /**
     * 画笔
     */
    private TextPaint mPaint;
    /**
     * 文本绘制区域
     */
    private Rect mTextRect;


    public ImageTextButton(Context context) {
        this(context, null);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取ImageTextButton自定义属性
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextButton, 0, 0);
        mText = array.getString(R.styleable.ImageTextButton_text);
        mColor = array.getColorStateList(R.styleable.ImageTextButton_textColor);
        if (mColor == null) {
            mColor = ColorStateList.valueOf(ResUtils.getColor(R.color.black));
        }
        mSize = array.getDimensionPixelSize(R.styleable.ImageTextButton_textSize, ResUtils.getDimensionPixelSize(R.dimen.text_12sp));
        array.recycle();
        //初始化画笔
        mPaint = new TextPaint();
        mPaint.setColor(mColor.getColorForState(getDrawableState(), 0));
        mPaint.setTextSize(mSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mTextRect = new Rect();
    }

    /**
     * 设置文字
     *
     * @param text
     */
    public void setText(String text) {
        this.mText = text;       //设置文字
        requestLayout();
    }

    public String getText() {
        return mText;
    }

    /**
     * 设置文字
     *
     * @param textID
     */
    public void setText(@StringRes int textID) {
        this.mText = ResUtils.getString(textID);       //设置文字
        requestLayout();
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setTextColor(ColorStateList color) {
        this.mColor = color;    //设置文字颜色
        invalidate();
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        //设置文字颜色
        this.mColor = ColorStateList.valueOf(color);
        invalidate();
    }

    /**
     * 设置文本字体大小
     *
     * @param size
     */
    public void setTextSize(int size) {
        this.mSize = size;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mText)) {
            //绘制矩形，方便文字居中
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = getWidth() - getPaddingRight();
            int bottom = getHeight() - getPaddingBottom();
            //可绘制区域
            mTextRect.set(left, top, right, bottom);

            //设置文字颜色大小，位置
            mPaint.setColor(mColor.getColorForState(getDrawableState(), 0));
            mPaint.setTextSize(mSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            //计算文字底部坐标
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            int baseLine = (mTextRect.bottom + mTextRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            //绘制文字
            canvas.drawText(mText, mTextRect.centerX(), baseLine, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mText != null && !"".equals(mText)) {
            setMeasuredDimension(measureWidth(widthMeasureSpec),
                    measureHeight(heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 计算宽度
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        if (mText == null) {
            mText = "";
        }
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = (int) mPaint.measureText(mText) + getPaddingLeft()
                        + getPaddingRight();
                break;
            case MeasureSpec.AT_MOST:
                result = (int) mPaint.measureText(mText) + getPaddingLeft()
                        + getPaddingRight();
                result = Math.min(result, specSize);
                break;
        }
        return result;
    }

    /**
     * 计算高度
     *
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int mAscent = (int) mPaint.ascent();
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = (int) (-mAscent + mPaint.descent()) + getPaddingTop()
                        + getPaddingBottom();
                break;
            case MeasureSpec.AT_MOST:
                result = (int) (-mAscent + mPaint.descent()) + getPaddingTop()
                        + getPaddingBottom();
                result = Math.min(result, specSize);
                break;
        }
        return result;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

}
