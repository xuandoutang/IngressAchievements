package com.example.amyu.ingress_achievement_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by amyu on 15/01/14.
 */
public class IngressListView extends ViewGroup {

    private int mNumColumns = 6;

    private int mLayoutWidth;

    private ArrayList<Achievement> mAchievementList;

    public IngressListView(Context context) {
        this(context, null);
    }

    public IngressListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IngressListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpAttr(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IngressListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setUpAttr(attrs);
        init();
    }

    private void setUpAttr(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IngressListView);
        mNumColumns = a.getInt(R.styleable.IngressListView_numColumns, 6);

        a.recycle();
    }

    private void init() {
        mAchievementList = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = View.resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = View.resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        this.setMeasuredDimension(width, height);
        mLayoutWidth = width;

        int viewLength = width / mNumColumns;

        int childCount = getChildCount();

        double space = viewLength - (viewLength / 2 + (1.0 * viewLength / 2) * Math.cos(Math.toRadians(30)));
        int viewWidth = (int) (viewLength - space * 2);
        int viewHeight = viewLength;
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).measure(viewWidth, viewHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int column = 1;
        int row = 0;
        int viewLength = mLayoutWidth / mNumColumns;
        /*double space = viewLength - (viewLength / 2 + (1.0 * viewLength / 2) * Math.cos(Math.toRadians(30)));
        int width = (int) (viewLength - space * 2);
        int height = viewLength;*/
        for (int i = 0; i < getChildCount(); i++) {
            AchievementView view = getChildAt(i);

            //Log.d("Log", view.getWidth() + "," + view.getHeight());
            /*view.layout(i * width, row, (i + 1) * width, column * height);*/

            view.layout(i * viewLength, row, (i + 1) * view.getWidth(), column * viewLength);
        }
    }

    @Override
    public int getChildCount() {
        return mAchievementList.size();
    }

    @Override
    public AchievementView getChildAt(int index) {
        return (AchievementView) super.getChildAt(index);
    }

    public void setAchievementList(ArrayList<Achievement> itemList) {
        if (itemList == null) {
            return;
        }
        mAchievementList = itemList;
        syncData();
    }

    private void syncData() {
        removeAllViews();
        for (Achievement item : mAchievementList) {
            AchievementView view = new AchievementView(getContext());
            view.setAchievement(item);
            addView(view);
        }
        invalidate();
    }

    public ArrayList<Achievement> getAchievementList() {
        return mAchievementList;
    }

    public void add(Achievement item) {
        if (item == null) {
            return;
        }
        mAchievementList.add(item);
        invalidate();
    }

    public void addAll(ArrayList<Achievement> itemList) {
        if (itemList == null) {
            return;
        }
        mAchievementList.addAll(itemList);
        invalidate();
    }

}
