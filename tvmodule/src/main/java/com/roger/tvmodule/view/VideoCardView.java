/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.roger.tvmodule.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import androidx.leanback.widget.BaseCardView;

import com.roger.tvmodule.R;

/**
 * A subclass of {@link BaseCardView} with an {@link ImageView} as its main region. The
 * {@link VideoCardView} is highly customizable and can be used for various use-cases by adjusting
 * the ImageViewCard's type to any combination of Title, Content, Badge or ImageOnly.
 * <p>
 * <h3>Styling</h3> There are two different ways to style the ImageCardView. <br>
 * No matter what way you use, all your styles applied to an ImageCardView have to extend the style
 * {@link R.style#Widget_Leanback_ImageCardViewStyle}.
 * <p>
 * <u>Example:</u><br>
 *
 * <pre>
 * {@code
 * <style name="CustomImageCardViewStyle" parent="Widget.Leanback.ImageCardViewStyle">
 * <item name="cardBackground">#F0F</item>
 * <item name="lbImageCardViewType">Title|Content</item>
 * </style>
 * <style name="CustomImageCardTheme" parent="Theme.Leanback">
 * <item name="imageCardViewStyle">@style/CustomImageCardViewStyle</item>
 * <item name="imageCardViewInfoAreaStyle">@style/ImageCardViewColoredInfoArea</item>
 * <item name="imageCardViewTitleStyle">@style/ImageCardViewColoredTitle</item>
 * </style>}
 * </pre>
 * <p>
 * The first possibility is to set custom Styles in the Leanback Theme's attributes
 * <code>imageCardViewStyle</code>, <code>imageCardViewTitleStyle</code> etc. The styles set here,
 * is the default style for all ImageCardViews.
 * <p>
 * The second possibility allows you to style a particular ImageCardView. This is useful if you
 * want to create multiple types of cards. E.g. you might want to display a card with only a title
 * and another one with title and content. Thus you need to define two different
 * <code>ImageCardViewStyles</code> and two different themes and apply them to the ImageCardViews.
 * You can do this by using a the {@link #VideoCardView(Context)} constructor and passing a
 * ContextThemeWrapper with the custom ImageCardView theme id.
 * <p>
 * <u>Example (using constructor):</u><br>
 *
 * <pre>
 * {@code
 *     new ImageCardView(new ContextThemeWrapper(context, R.style.CustomImageCardTheme));
 * }
 * </pre>
 *
 * <p>
 * You can style all ImageCardView's components such as the title, content, badge, infoArea and the
 * image itself by extending the corresponding style and overriding the specific attribute in your
 * custom ImageCardView theme.
 *
 * <h3>Components</h3> The ImageCardView contains three components which can be combined in any
 * combination:
 * <ul>
 * <li>Title: The card's title</li>
 * <li>Content: A short description</li>
 * <li>Badge: An icon which can be displayed on the right or left side of the card.</li>
 * </ul>
 * In order to choose the components you want to use in your ImageCardView, you have to specify them
 * in the <code>lbImageCardViewType</code> attribute of your custom <code>ImageCardViewStyle</code>.
 * You can combine the following values:
 * <code>Title, Content, IconOnRight, IconOnLeft, ImageOnly</code>.
 * <p>
 * <u>Examples:</u><br>
 *
 * <pre>
 * {@code <style name="CustomImageCardViewStyle" parent="Widget.Leanback.ImageCardViewStyle">
 * ...
 * <item name="lbImageCardViewType">Title|Content|IconOnLeft</item>
 * ...
 * </style>}
 * </pre>
 *
 * <pre>
 * {@code <style name="CustomImageCardViewStyle" parent="Widget.Leanback.ImageCardViewStyle">
 * ...
 * <item name="lbImageCardViewType">ImageOnly</item>
 * ...
 * </style>}
 * </pre>
 *
 * @attr ref androidx.leanback.R.styleable#LeanbackTheme_imageCardViewStyle
 * @attr ref androidx.leanback.R.styleable#lbImageCardView_lbImageCardViewType
 * @attr ref androidx.leanback.R.styleable#LeanbackTheme_imageCardViewTitleStyle
 * @attr ref androidx.leanback.R.styleable#LeanbackTheme_imageCardViewContentStyle
 * @attr ref androidx.leanback.R.styleable#LeanbackTheme_imageCardViewBadgeStyle
 * @attr ref androidx.leanback.R.styleable#LeanbackTheme_imageCardViewImageStyle
 * @attr ref androidx.leanback.R.styleable#LeanbackTheme_imageCardViewInfoAreaStyle
 */
public class VideoCardView extends BaseCardView {


    private static final String ALPHA = "alpha";

    private ImageView mImageView;
    //    private ViewGroup mInfoArea;
    private TextView mTitleView;
    private TextView mSubTitleView;

    private boolean mAttachedToWindow;
    ObjectAnimator mFadeInAnimator;


    /**
     * @see View#View(Context)
     */
    public VideoCardView(Context context) {
        this(context, null);
    }

    /**
     * @see View#View(Context, AttributeSet)
     */
    public VideoCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Deprecated
    public VideoCardView(Context context, int themeResId) {
        this(new ContextThemeWrapper(context, themeResId));
    }

    /**
     * @see View#View(Context, AttributeSet, int)
     */
    public VideoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildImageCardView();
    }

    private void buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        setFocusable(true);
        setFocusableInTouchMode(true);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_card_view, this);
        //todo 上按键选中more按钮
        setNextFocusUpId(R.id.tv_more);

        mImageView = findViewById(R.id.image);
        if (mImageView.getDrawable() == null) {
            mImageView.setVisibility(View.VISIBLE);
        }

        // Set Object Animator for image view.
        mFadeInAnimator = ObjectAnimator.ofFloat(mImageView, ALPHA, 10f);
        mFadeInAnimator.setDuration(
                mImageView.getResources().getInteger(android.R.integer.config_shortAnimTime));

        mTitleView = findViewById(R.id.tv_title);
//        mTitleView = (TextView) inflater.inflate(R.layout.lb_image_card_view_themed_title,
//                mInfoArea, false);
//        mInfoArea.addView(mTitleView);

        mSubTitleView = findViewById(R.id.tv_sub_title);
    }

    /**
     * Returns the main image view.
     */
    public final ImageView getMainImageView() {
        return mImageView;
    }

    /**
     * Enables or disables adjustment of view bounds on the main image.
     */
    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (mImageView != null) {
            mImageView.setAdjustViewBounds(adjustViewBounds);
        }
    }

    /**
     * Sets the ScaleType of the main image.
     */
    public void setMainImageScaleType(ScaleType scaleType) {
        if (mImageView != null) {
            mImageView.setScaleType(scaleType);
        }
    }

    /**
     * Sets the image drawable with fade-in animation.
     */
    public void setMainImage(Drawable drawable) {
        setMainImage(drawable, true);
    }

    /**
     * Sets the image drawable with optional fade-in animation.
     */
    public void setMainImage(Drawable drawable, boolean fade) {
        if (mImageView == null) {
            return;
        }

        mImageView.setImageDrawable(drawable);
        if (drawable == null) {
            mFadeInAnimator.cancel();
            mImageView.setAlpha(1f);
            mImageView.setVisibility(View.INVISIBLE);
        } else {
            mImageView.setVisibility(View.VISIBLE);
            if (fade) {
                fadeIn();
            } else {
                mFadeInAnimator.cancel();
                mImageView.setAlpha(1f);
            }
        }
    }

    /**
     * Sets the layout dimensions of the ImageView.
     */
    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mImageView.setLayoutParams(lp);
    }

    /**
     * Returns the ImageView drawable.
     */
    public Drawable getMainImage() {
        if (mImageView == null) {
            return null;
        }

        return mImageView.getDrawable();
    }

    /**
     * Sets the title text.
     */
    public void setTitleText(CharSequence text) {
        if (mTitleView == null) {
            return;
        }
        mTitleView.setText(text);
    }

    public void setSunTitleText(String text) {
        if (mSubTitleView == null) {
            return;
        }
        mSubTitleView.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        mSubTitleView.setText(text);
    }

//    /**
//     * Returns the info area background drawable.
//     */
//    public Drawable getInfoAreaBackground() {
//        if (mInfoArea != null) {
//            return mInfoArea.getBackground();
//        }
//        return null;
//    }
//
//    /**
//     * Sets the info area background drawable.
//     */
//    public void setInfoAreaBackground(Drawable drawable) {
//        if (mInfoArea != null) {
//            mInfoArea.setBackground(drawable);
//        }
//    }
//
//    /**
//     * Sets the info area background color.
//     */
//    public void setInfoAreaBackgroundColor(@ColorInt int color) {
//        if (mInfoArea != null) {
//            mInfoArea.setBackgroundColor(color);
//        }
//    }

    private void fadeIn() {
        mImageView.setAlpha(0f);
        if (mAttachedToWindow) {
            mFadeInAnimator.start();
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
//        if (mImageView.getAlpha() == 0) {
//            fadeIn();
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttachedToWindow = false;
//        mFadeInAnimator.cancel();
//        mImageView.setAlpha(1f);
        super.onDetachedFromWindow();
    }
}
