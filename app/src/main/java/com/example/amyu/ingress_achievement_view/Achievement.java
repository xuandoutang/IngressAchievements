package com.example.amyu.ingress_achievement_view;

import android.graphics.Bitmap;

/**
 * Created by amyu on 15/01/14.
 */
public class Achievement {

    private int mAchievementType = 0;

    private int mInnerColor = -1;

    private int mOuterColor = -1;

    private int mInnerColorRes = -1;

    private int mOuterColorRes = -1;

    private int mIconResId = -1;

    private Bitmap mIconBitmap = null;

    public int getAchievementType() {
        return mAchievementType;
    }

    public void setAchievementType(int achievementType) {
        mAchievementType = achievementType;
    }

    public int getInnerColor() {
        return mInnerColor;
    }

    public void setInnerColor(int innerColor) {
        mInnerColor = innerColor;
    }

    public int getOuterColor() {
        return mOuterColor;
    }

    public void setOuterColor(int outerColor) {
        mOuterColor = outerColor;
    }

    public int getInnerColorRes() {
        return mInnerColorRes;
    }

    public void setInnerColorRes(int innerColorRes) {
        mInnerColorRes = innerColorRes;
    }

    public int getOuterColorRes() {
        return mOuterColorRes;
    }

    public void setOuterColorRes(int outerColorRes) {
        mOuterColorRes = outerColorRes;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        mIconResId = iconResId;
    }

    public Bitmap getIconBitmap() {
        return mIconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        mIconBitmap = iconBitmap;
    }
}
