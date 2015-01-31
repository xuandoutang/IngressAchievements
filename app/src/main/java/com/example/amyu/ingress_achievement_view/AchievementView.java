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
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

    private Bitmap mResizeBitmap;

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
     * 六角形の部分のClickListenerを付けるためのRegion
     * そのうち変数名変えようかなー
     */
    private Region mRegion;

    /**
     * Touch部分のRegionにセットするRectFのアレをセットするClip
     */
    private Region mClipRegion;

    /**
     * Touch部分のRectF
     */
    private RectF mRectF;

    /**
     * {@link com.example.amyu.ingress_achievement_view.AchievementView.OnClickListener} のOnClickListenerのセット
     * 名前が紛らわしい感がパない
     */
    private OnClickListener mOnClickListener;

    /**
     * constructor
     * {@inheritDoc}
     *
     * @param context
     */
    public AchievementView(Context context) {
        this(context, null);
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
        setUp();
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
    public AchievementView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setUpAttr(attrs);
        setUp();
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
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AchievementView);
        mIconBitmap = BitmapFactory.decodeResource(mResources, a.getResourceId(R.styleable.AchievementView_icon, 0));
        int type = a.getInt(R.styleable.AchievementView_achievement, 0);
        setAchievementType(type);

        a.recycle();
    }


    /**
     * PaintとPathとかとかのセットアップ
     */
    private void setUp() {
        //外枠のPaint
        mOuterCorePaint = new Paint();
        mOuterCorePaint.setAntiAlias(true);

        //内側のPaint
        mInnerCorePaint = new Paint();
        mInnerCorePaint.setAntiAlias(true);

        //アイコンがあった場合の画像のPaint
        mIconPaint = new Paint();
        mIconMatrix = new Matrix();

        //パス!!!!
        mInnerPath = new Path();
        mOuterPath = new Path();

        //六角形の部分のTouchListenerのみを拾ってOnClickListenerに渡すためのやつ
        mRegion = new Region();
        mClipRegion = new Region();
        mRectF = new RectF();
    }


    /**
     * {@inheritDoc}
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(0, 0, 0, 0);

        mIconPaint.reset();
        mInnerPath.reset();
        mOuterPath.reset();
        mOuterCorePaint.reset();
        mInnerCorePaint.reset();

        mOuterCorePaint.setColor(mOuterColor);
        mInnerCorePaint.setColor(mInnerColor);

        //padding
        int width = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        int height = canvas.getHeight() - getPaddingTop() - getPaddingBottom();

        int centerX = width / 2;
        int centerY = height / 2;


        int radius = height / 2;


        //TODO PlatinumとOnyxの時の処理
        for (int i = 0; i < VERTEX_NUM + 1; i++) {

            double cos = Math.cos(Math.toRadians(60 * i + 30));
            double sin = Math.sin(Math.toRadians(60 * i + 30));

            double innerX = centerX + (radius - mOuterWidth) * cos + getPaddingLeft();
            double innerY = centerY + (radius - mOuterWidth) * sin + getPaddingTop();

            mInnerPath.lineTo((float) innerX, (float) innerY);

            double outerX = centerX + radius * cos + getPaddingLeft();
            double outerY = centerY + radius * sin + getPaddingTop();
            mOuterPath.lineTo((float) outerX, (float) outerY);

        }


        mRectF.setEmpty();
        mInnerPath.computeBounds(mRectF, true);
        mRegion.setEmpty();
        mClipRegion.setEmpty();
        mClipRegion.set((int) mRectF.left, (int) mRectF.top, (int) mRectF.right, (int) mRectF.bottom);
        mRegion.setPath(mInnerPath, mClipRegion);
        canvas.drawPath(mOuterPath, mOuterCorePaint);
        canvas.drawPath(mInnerPath, mInnerCorePaint);

        if (mResizeBitmap != null) {
            canvas.drawBitmap(mResizeBitmap, mIconMatrix, mIconPaint);
        }
    }

    /**
     * wrapなときに六角形の形に合うように整形
     * {@inheritDoc}
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean canResizeWidth;

        boolean canResizeHeight;

        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int resizeWidth = widthSpecSize;
        int resizeHeight = heightSpecSize;

        canResizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
        canResizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

        double ratioX = Math.cos(Math.toRadians(30));

        if (canResizeWidth) {
            resizeWidth = (int) (heightSpecSize * ratioX);
        }

        if (canResizeHeight) {
            resizeHeight = (int) (widthSpecSize / ratioX);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(resizeWidth, resizeHeight);
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
        if (w <= 0 || h <= 0) {
            return;
        }

        mOuterWidth = h * (3.0 / 100);

        if (mIconBitmap == null) {
            return;
        }
        int centerY = h / 2;
        int centerX = w / 2;
        int imageSize = (int) (h * (70.0 / 100));
        mResizeBitmap = Bitmap.createScaledBitmap(mIconBitmap, imageSize, imageSize, false);
        mIconMatrix.setTranslate(centerX - mResizeBitmap.getWidth() / 2, centerY - mResizeBitmap.getHeight() / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mRegion.contains((int) event.getX(), (int) event.getY())) {
                    if (mOnClickListener == null) {
                        return true;
                    }
                    mOnClickListener.onClick(this);
                }
                break;
        }

        return false;
    }

    /**
     * 内側の色 {@link #mInnerColor} のリソースIDから指定
     *
     * @param resId 色のリソースID
     */
    public void setInnerColorRes(int resId) {
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
    public void setOuterColorRes(int resId) {
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
     * 内側の色 {@link #mInnerColor} を返す
     *
     * @return {@link #mInnerColor}
     */
    public int getInnerColor() {
        return mInnerColor;
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
     * 外枠の色 {@link #mOuterColor} を返す
     *
     * @return {@link #mOuterColor}
     */
    public int getOuterColor() {
        return mOuterColor;
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
    public void setIconRes(int resId) {
        if (resId == 0) {
            return;
        }
        mIconBitmap = BitmapFactory.decodeResource(mResources, resId);
        invalidate();
    }

    /**
     * Achievementのタイプ {@link #mAchievementType} の指定
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

    /**
     * Achievementのタイプ {@link #mAchievementType} の取得
     *
     * @return {@link #mAchievementType}
     */
    public int getAchievementType() {
        return mAchievementType;
    }

    /**
     * {@link com.example.amyu.ingress_achievement_view.AchievementView.OnClickListener} のセット
     *
     * @param listener {@link com.example.amyu.ingress_achievement_view.AchievementView.OnClickListener}
     */
    public void setOnClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        mOnClickListener = listener;
    }

    /**
     * このViewの専用のClickListener
     */
    public interface OnClickListener {
        public void onClick(AchievementView view);
    }

}
