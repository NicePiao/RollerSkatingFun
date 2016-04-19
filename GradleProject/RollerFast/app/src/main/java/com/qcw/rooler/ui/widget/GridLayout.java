/*
 * Copyright (C) 2014 Baidu Inc. All rights reserved.
 */
package com.qcw.rooler.ui.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.qcw.rooler.util.DisplayUtils;

/**
 * 自定义平分gridlayout
 */
public class GridLayout extends ViewGroup {

    // private static final String TAG = "GridLayout";

    /** 默认列数 */
    private static final int DEFAULT_COLUMN_COUNT = 3;
    /** 默认列间距，单位：dp */
    private static final int DEFAULT_H_SPACING = 1;
    /** 默认行间距，单位：dp */
    private static final int DEFAULT_V_SPACING = 1;

    /** 列数 */
    private int mColumnCount;
    /** 列间距，单位：px */
    private int mHorizontalSpacing;
    /** 行间距，单位：px */
    private int mVerticalSpacing;
    
    private RectF mEmptyAreaRect;
    private Paint mEmptyAreaPaint;
    private Integer mEmptyAreaColor = null;

    /**
     * @param context context
     * @param attrs attributes
     */
    public GridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化参数
        init();
    }
    
    public GridLayout(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        // 初始化参数
        mColumnCount = DEFAULT_COLUMN_COUNT;
        mHorizontalSpacing = DisplayUtils.dip2px(getContext(), DEFAULT_H_SPACING);
        mVerticalSpacing = DisplayUtils.dip2px(getContext(), DEFAULT_V_SPACING);
        
        mEmptyAreaRect = new RectF();
        mEmptyAreaPaint = new Paint();
    }
    
    public void setEmptyAreaColor(int color) {
        mEmptyAreaColor = color;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        // LogUtil.d(TAG, "onMeasure. width = " + width);
        final int count = getChildCount();
        // LogUtil.d(TAG, "onMeasure. count = " + count);

        int cw = (width - mHorizontalSpacing * (mColumnCount - 1)) / mColumnCount;
        // LogUtil.d(TAG, "onMeasure. cw = " + cw);
        int goneNum = 0;
        int currX = getPaddingLeft();
        int currY = getPaddingTop();
        int childHeight = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                childHeight = child.getMeasuredHeight();
                child.measure(MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));
                // LogUtil.d(TAG, "onMeasure. chile h = " + childHeight);
                final int columnIndex = (i - goneNum) % mColumnCount;
                if (columnIndex == 0) { // 每行第一个
                    currX = getPaddingLeft();
                    if (i != 0) {
                        currY += childHeight + mVerticalSpacing;
                    }
                } else {
                    currX += cw + mHorizontalSpacing;
                }
                LayoutParams lp;
                if (child.getLayoutParams() != null && child.getLayoutParams() instanceof LayoutParams) {
                    lp = (LayoutParams) child.getLayoutParams();
                } else {
                    lp = new LayoutParams(0, 0);
                    child.setLayoutParams(lp);
                }
                lp.mX = currX;
                lp.mY = currY;
                lp.width = cw;
                lp.height = childHeight;
                // LogUtil.d(TAG, "onMeasure. index = " + (i - goneNum) +
                // ", x = " + currX + ", y = " + currY);
            } else {
                goneNum++;
            }
        }
        int delta = (count - goneNum) / mColumnCount + ((count - goneNum) % mColumnCount == 0 ? 0 : 1);
        int height = childHeight * delta + mVerticalSpacing * (delta - 1);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // LogUtil.d(TAG, "onLayout. l = " + l + ", t = " + t + ", r = " + r +
        // ", b = " + b);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                // LogUtil.d(TAG, "onLayout. index = " + i + ", x = " + lp.mX +
                // ", w = " + lp.width + ", y = " + lp.mY
                // + ", h = " + lp.height);
                child.layout(lp.mX, lp.mY, lp.mX + lp.width, lp.mY + lp.height);
            }
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getChildCount() % mColumnCount == 0 || mEmptyAreaColor == null) {
            return;
        }
        
        View lastChild = getChildAt(getChildCount() - 1);
        float left = lastChild.getLeft() + lastChild.getWidth() + mHorizontalSpacing;
        float top = lastChild.getTop();
        float right = getLeft() + getWidth();
        float bottom = getTop() + getHeight();
        mEmptyAreaRect.set(left, top, right, bottom);
        mEmptyAreaPaint.setColor(mEmptyAreaColor);
        canvas.drawRect(mEmptyAreaRect, mEmptyAreaPaint);
    }

    /**
     * @param columnCount
     *            the mColumnCount to set
     */
    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }

    /**
     * @param horizontalSpacing
     *            the mHorizontalSpacing to set
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
    }

    /**
     * @param verticalSpacing
     *            the mVerticalSpacing to set
     */
    public void setVerticalSpacing(int verticalSpacing) {
        this.mVerticalSpacing = verticalSpacing;
    }

    /**
     * @author kanfei
     * @since Aug 27, 2014
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {

        /** View 的x坐标 */
        int mX;
        /** View 的y坐标 */
        int mY;

        /**
         * @param width width
         * @param height height
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }

        /**
         * @param source source
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * @param c context
         * @param attrs attributes
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
    }
}