/*
 *    Copyright (C) 2015 Yuki Mima
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.amyu.ingress_achievement_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class IngressListView extends ViewGroup {

    /**
     * 列の数
     */
    private int mNumColumns = 6;

    /**
     * 一つのAchievementに対するPaddingのパーセンテージ
     */
    private final static double PADDING_PERCENT = 0.1;

    /**
     * {@link com.example.amyu.ingress_achievement_view.IngressListView.OnItemClickListener} のOnItemClickListenerよ
     */
    private OnItemClickListener mOnItemClickListener;

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     */
    public IngressListView(Context context) {
        this(context, null);
    }

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     * @param attrs
     */
    public IngressListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public IngressListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpAttr(attrs);
        init();
    }

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IngressListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setUpAttr(attrs);
        init();
    }

    /**
     * AttributeSetからのデータの取得
     * {@inheritDoc}
     *
     * @param attrs
     */
    private void setUpAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IngressListView);
        mNumColumns = a.getInt(R.styleable.IngressListView_numColumns, 6);

        a.recycle();
    }

    /**
     * 初期化
     */
    private void init() {

    }

    /**
     * {@inheritDoc}
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);

        int viewWidth = (int) (width / (mNumColumns + 0.5));

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }
    }

    private int mViewWidth;

    private int mViewHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * AchievementViewをそれぞれ配置する場所を決める
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int column = 0;
        int row = 0;
        for (int i = 0; i < getChildCount(); i++) {
            AchievementView view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            int topSpace = (int) (Math.sin(Math.toRadians(30)) / 2 * height * column);

            int padding = (int) (width * PADDING_PERCENT)/2;


            if (column % 2 == 0) {
                view.layout(
                        row * width,
                        column * height - topSpace,
                        (row + 1) * width,
                        (column + 1) * height - topSpace);
            } else {
                view.layout(
                        row * width + width / 2,
                        column * height - topSpace,
                        (row + 1) * width + width / 2,
                        (column + 1) * height - topSpace);
            }
            view.setPadding(padding, padding, padding, padding);

            row++;
            if (row == mNumColumns) {
                column++;
                row = 0;
            }
        }


    }

    /**
     * {@inheritDoc}
     *
     * @param index
     * @return
     */
    @Override
    public AchievementView getChildAt(int index) {
        return (AchievementView) super.getChildAt(index);
    }

    /**
     * {@link com.example.amyu.ingress_achievement_view.AchievementView} 以外のViewが来たら落とす感じで
     * あと {@link com.example.amyu.ingress_achievement_view.AchievementView.OnClickListener} をセットする感じで
     * で､superにセットして上げる感じ
     *
     * @param child  {@link com.example.amyu.ingress_achievement_view.AchievementView}
     * @param index  {@inheritDoc}
     * @param params {@inheritDoc}
     */
    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (!(child instanceof AchievementView)) {
            throw new IllegalArgumentException("AchievementView以外が来てるよーーーーー");
        }
        ((AchievementView) (child)).setOnClickListener(mOnClickListener);
        super.addView(child, index, params);
    }

    /**
     * Viewが重なった部分のTouchの透過
     * falseを返すことにより､下のViewにTouchEventが渡るため,いい感じに良い
     * {@inheritDoc}
     *
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)  {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)  {
        return true;
    }

    /**
     * すべてのaaaaaねみいいいいいいいい
     * @param viewList ほげ
     */
    public void addAllView(List<AchievementView> viewList) {
        if (viewList == null) {
            return;
        }
        for (AchievementView view : viewList) {
            addView(view);
        }
    }

    /**
     * 表示する列数 {@link #mNumColumns} のセット
     *
     * @param numColumns 列数
     */
    public void setNumColumns(int numColumns) {
        if (numColumns < 0) {
            return;
        }

        mNumColumns = numColumns;
        requestLayout();
    }

    public void showBackground() {
        setBackground(new BitmapDrawable(getResources(), getBackgroundBitmap(mViewWidth, mViewHeight)));
    }

    public void hideBackground(){
        setBackground(null);
    }

    /**
     * あのりすなーせっとするやつ
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * あのくりっくのやつ
     */
    private AchievementView.OnClickListener mOnClickListener = new AchievementView.OnClickListener() {
        @Override
        public void onClick(AchievementView view) {
            if (mOnItemClickListener == null) {
                return;
            }
            int position = indexOfChild(view);
            mOnItemClickListener.onItemClick(view, position);
        }
    };

    /**
     * あのアレよ､アレ
     */
    public interface OnItemClickListener {
        public void onItemClick(AchievementView view, int position);
    }

    /**
     * あのAchievementの後ろに表示されてる背景を作成
     * Utilsで持たせた方がいい感
     *
     * @param maxWidth  画面の横幅
     * @param maxHeight 画面の縦幅
     * @return 結合させたBitmap
     * @hide
     */
    private Bitmap getBackgroundBitmap(int maxWidth, int maxHeight) {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.achievement_background);
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();

        Bitmap backgroundBitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBitmap);

        for (int i = 0; i < maxHeight / bitmapHeight + 1; i++) {
            for (int j = 0; j < maxWidth / bitmapWidth + 1; j++) {
                canvas.drawBitmap(bitmap, j * bitmapWidth, i * bitmapHeight, null);
            }
        }
        bitmap.recycle();
        return backgroundBitmap;
    }
}
