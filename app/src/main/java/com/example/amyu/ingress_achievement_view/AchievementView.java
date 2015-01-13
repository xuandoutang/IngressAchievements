package com.example.amyu.ingress_achievement_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by amyu on 15/01/13.
 */
public class AchievementView extends View {

    /**
     * BRONZE
     */
    public final static int BRONZE = 0;

    /**
     * SILVER
     */
    public final static int SILVER = 1;

    /**
     * GOLD
     */
    public final static int GOLD = 2;

    /**
     * PLATINUM
     * TODO 描画の対応
     */
    private final static int PLATINUM = 3;

    /**
     * ONYX
     * TODO 描画の対応
     */
    private final static int ONYX = 4;

    /**
     * Achievement Bronze Inner Color
     */
    private final static int COLOR_BRONZE_INNER = 0xFF38190C;

    /**
     * Achievement Bronze Outer Color
     */
    private final static int COLOR_BRONZE_OUTER = 0xFFC67F5D;

    /**
     * Achievement Silver Inner Color
     */
    private final static int COLOR_SILVER_INNER = 0xFF2C3333;

    /**
     * Achievement Silver Outer Color
     */
    private final static int COLOR_SILVER_OUTER = 0xFF8FA7AD;

    /**
     * Achievement Gold Inner Color
     */
    private final static int COLOR_GOLD_INNER = 0xFF573C1D;

    /**
     * Achievement Gold Outer Color
     */
    private final static int COLOR_GOLD_OUTER = 0xFFE9C06D;

    /**
     * 内側のColor
     * デフォルトはBronze
     */
    private int mInnerColor = COLOR_BRONZE_INNER;

    /**
     * 外枠のColor
     * デフォルトはBronze
     */
    private int mOuterColor = COLOR_BRONZE_OUTER;

    /**
     * 外枠の太さ
     */
    private double mOuterWidth;

    /**
     * 頂点の数
     */
    private final static int VERTEX_NUM = 6;

    /**
     * 内部に表示させる画像
     */
    private Bitmap mIconBitmap;

    /**
     * {@link #mIconBitmap}の移動用Matrix
     * TODO Rectの方がいいのかな?
     */
    private Matrix mIconMatrix;

    /**
     * 外枠の描画用Paint
     */
    private Paint mOuterCorePaint;

    /**
     * 内側の描画用Paint
     */
    private Paint mInnerCorePaint;

    /**
     * {@link #mIconBitmap}の描画用Paint
     */
    private Paint mIconPaint;

    /**
     * 内側の座標のPath
     */
    private Path mInnerPath;

    /**
     * 外枠の座標のPath
     */
    private Path mOuterPath;

    /**
     * 実績の種類
     * デフォルトはBronze
     */
    private int mAchievementType = BRONZE;

    /**
     * {@link android.content.res.Resources}
     */
    private Resources mResources;

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     */
    public AchievementView(Context context) {
        this(context, null, 0);
    }

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     * @param attrs
     */
    public AchievementView(Context context, AttributeSet attrs) {
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
    public AchievementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mResources = getResources();

        setUpAttr(attrs);
        setUpPaint();
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
    @TargetApi(Build.VERSION_CODES.L)
    public AchievementView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setUpAttr(attrs);
        setUpPaint();
    }

    private void setUpAttr(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AchievementView);
        mIconBitmap = BitmapFactory.decodeResource(mResources, a.getResourceId(R.styleable.AchievementView_icon, 0));
        int type = a.getInt(R.styleable.AchievementView_achievement, 0);
        setAchievementType(type);

        a.recycle();
    }

    /**
     * PaintとPathとかとかのセットアップ
     */
    private void setUpPaint() {
        mOuterCorePaint = new Paint();
        mOuterCorePaint.setAntiAlias(true);

        mInnerCorePaint = new Paint();
        mInnerCorePaint.setAntiAlias(true);

        mIconPaint = new Paint();
        mIconMatrix = new Matrix();

        mInnerPath = new Path();
        mOuterPath = new Path();
    }

    /**
     * {@inheritDoc}
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(0, 0, 0, 0);

        mOuterCorePaint.setColor(mOuterColor);
        mInnerCorePaint.setColor(mInnerColor);

        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        int radius = canvas.getWidth() / 2;

        //TODO PlatinumとOnyxの時の処理
        for (int i = 0; i < VERTEX_NUM + 1; i++) {
            double cos = Math.cos(Math.toRadians(60 * i + 30));
            double sin = Math.sin(Math.toRadians(60 * i + 30));

            double innerX = centerX + (radius - mOuterWidth) * cos;
            double innerY = centerY + (radius - mOuterWidth) * sin;
            mInnerPath.lineTo((float) innerX, (float) innerY);

            double outerX = centerX + radius * cos;
            double outerY = centerY + radius * sin;
            mOuterPath.lineTo((float) outerX, (float) outerY);
        }
        canvas.drawPath(mOuterPath, mOuterCorePaint);
        canvas.drawPath(mInnerPath, mInnerCorePaint);

        if (mIconBitmap != null) {
            canvas.drawBitmap(mIconBitmap, mIconMatrix, mIconPaint);
        }
    }

    /**
     * 小さい方に長さに合わせる
     * {@inheritDoc}
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minLength = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(minLength, minLength);
    }

    /**
     * 外枠の大きさは全体の1辺のの長さの3%とする
     * {@link #mIconBitmap} がNullじゃなかった場合､1辺の長さの70%にリサイズ
     * {@inheritDoc}
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mOuterWidth = w * (3.0 / 100);

        if (mIconBitmap == null) {
            return;
        }
        int center = w / 2;
        int imageSize = (int) (w * (70.0 / 100));
        mIconBitmap = Bitmap.createScaledBitmap(mIconBitmap, imageSize, imageSize, false);
        mIconMatrix.setTranslate(center - mIconBitmap.getWidth() / 2, center - mIconBitmap.getWidth() / 2);

    }

    /**
     * 内側の色 {@link #mInnerColor} のリソースIDから指定
     *
     * @param resId 色のリソースID
     */
    public void setInnerColorResource(int resId) {
        int color = mResources.getColor(resId);
        if (mInnerColor == color) {
            return;
        }
        mInnerColor = color;
        invalidate();
    }

    /**
     * 外枠の色 {@link #mOuterColor} のリソースIDから指定
     *
     * @param resId 色のリソースID
     */
    public void setOuterColorResource(int resId) {
        int color = mResources.getColor(resId);
        if (mOuterColor == color) {
            return;
        }
        mOuterColor = color;
        invalidate();
    }

    /**
     * 内側の色 {@link #mInnerColor} を指定
     *
     * @param color 16進数の色コード
     */
    public void setInnerColor(int color) {
        if (mInnerColor == color) {
            return;
        }
        mInnerColor = color;
        invalidate();
    }

    /**
     * 外枠の色 {@link #mOuterColor} を指定
     *
     * @param color 16進数の色コード
     */
    public void setOuterColor(int color) {
        if (mOuterColor == color) {
            return;
        }
        mOuterColor = color;
        invalidate();
    }

    /**
     * アイコン {@link #mIconBitmap} の設定
     *
     * @param bitmap 設定するBitmap
     */
    public void setIconBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        mIconBitmap = bitmap;
        invalidate();
    }

    /**
     * アイコン {@link #mIconBitmap} の設定
     *
     * @param resId drawableのリソースから指定
     */
    public void setIconResource(int resId) {
        if (resId == 0) {
            return;
        }
        mIconBitmap = BitmapFactory.decodeResource(mResources, resId);
        invalidate();
    }

    /**
     * Achievementのタイプの指定
     * TODO PlatinumとOnyxの対応
     *
     * @param type {@link #BRONZE}, {@link #SILVER}, {@link #GOLD} から指定
     */
    public void setAchievementType(int type) {
        mAchievementType = type;
        switch (type) {
            case BRONZE:
                mInnerColor = COLOR_BRONZE_INNER;
                mOuterColor = COLOR_BRONZE_OUTER;
                break;
            case SILVER:
                mInnerColor = COLOR_SILVER_INNER;
                mOuterColor = COLOR_SILVER_OUTER;
                break;
            case GOLD:
                mInnerColor = COLOR_GOLD_INNER;
                mOuterColor = COLOR_GOLD_OUTER;
                break;
        }
        invalidate();
    }

}
