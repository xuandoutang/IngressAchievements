package com.example.amyu.ingress_achievement_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by amyu on 15/01/14.
 */
public class IngressListView extends ViewGroup {

    /**
     * 列の数
     */
    private int mNumColumns = 6;

    /**
     * 一つのAchievementに対するマージンのパーセンテージ
     */
    private final static double MARGIN_PERCENT = 0.05;

    private int mChildrenCount;

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
     * 1つのAchievementの横の長さは
     * (このViewの全体の横幅 - AchievementView同士のMargin - AchievementViewの半分の横幅) / 列数
     * {@inheritDoc}
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //描画する部分の最大の横幅
        final int width = (int) (MeasureSpec.getSize(widthMeasureSpec) - (MeasureSpec.getSize(widthMeasureSpec) / mNumColumns) / (2 + 0.5 + MARGIN_PERCENT * mNumColumns + MARGIN_PERCENT / 2));

        //一つのAchievementViewの横幅 高さは横幅により決定(wrap)
        int viewWidth = (int) ((2 * width) / (2 * mNumColumns + 2 * mNumColumns * MARGIN_PERCENT + MARGIN_PERCENT));
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }
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
            int margin = (int) (width * MARGIN_PERCENT);

            int oddMargin = (int) (width * MARGIN_PERCENT / 2);

            if (column % 2 == 0) {
                view.layout(
                        row * width + margin * row,
                        column * height - topSpace + margin * column,
                        (row + 1) * width + margin * row,
                        (column + 1) * height - topSpace + margin * column);
            } else {
                view.layout(
                        row * width + width / 2 + margin * row + oddMargin,
                        column * height - topSpace + margin * column,
                        (row + 1) * width + width / 2 + margin * row + oddMargin,
                        (column + 1) * height - topSpace + margin * column);
            }
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

    public void addAllView(List<AchievementView> viewList) {
        if (viewList == null) {
            return;
        }
        for (AchievementView view : viewList) {
            addView(view);
        }
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (!(child instanceof AchievementView)) {
            throw new IllegalArgumentException("AchievementView以外が来てるよーーーーー");
        }
        ((AchievementView) (child)).setOnClickListener(mOnClickListener);
        super.addView(child, index, params);
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

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(AchievementView view, int position);
    }


}
