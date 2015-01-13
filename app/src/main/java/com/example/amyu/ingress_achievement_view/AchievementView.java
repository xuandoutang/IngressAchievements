package com.example.amyu.ingress_achievement_view;

import android.annotation.TargetApi;
import android.content.Context;
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
     * Achievement Bronze Inner Color
     */
    private final static int COLOR_BRONZE_INNER = 0xFF38190C;

    /**
     * Achievement Bronze Outer Color
     */
    private final static int COLOR_BRONZE_OUTER = 0xFFC67F5D;

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
        setUpPaint();
    }

    /**
     * PaintとPathとかとかのセットアップ
     */
    private void setUpPaint() {
        mOuterCorePaint = new Paint();
        mOuterCorePaint.setAntiAlias(true);
        mOuterCorePaint.setColor(COLOR_BRONZE_OUTER);

        mInnerCorePaint = new Paint();
        mInnerCorePaint.setAntiAlias(true);
        mInnerCorePaint.setColor(COLOR_BRONZE_INNER);

        mIconPaint = new Paint();
        mIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample_icon);
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

        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        int radius = canvas.getWidth() / 2;


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
        mIconMatrix.setTranslate(center - mIconBitmap.getWidth() / 2, center - mIconBitmap.getWidth() / 2);
        int imageSize = (int) (w * (70.0 / 100));
        mIconBitmap = Bitmap.createScaledBitmap(mIconBitmap, imageSize, imageSize, false);
    }
}
