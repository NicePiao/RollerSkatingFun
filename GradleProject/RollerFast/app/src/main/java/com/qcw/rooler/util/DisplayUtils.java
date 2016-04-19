/*
 * Copyright (C) 2013 Baidu Inc. All rights reserved.
 */
package com.qcw.rooler.util;

import android.content.Context;
import android.util.DisplayMetrics;

public final class DisplayUtils {

    /**  */
    private static final float DELTA = 0.5f;

    /**
     * default constructor
     */
    private DisplayUtils() {
        super();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context {@linkplain android.content.Context}
     * @param dpValue dp
     * @return px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + DELTA);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context {@linkplain android.content.Context}
     * @param pxValue px
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) { // NO_UCD (unused code)
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + DELTA);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue   px
     * @param fontScale ({@link android.util.DisplayMetrics DisplayMetrics}
     *                  类中属性scaledDensity)
     * @return sp
     */
    public static int px2sp(float pxValue, float fontScale) { // NO_UCD (unused code)
        return (int) (pxValue / fontScale + DELTA);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue   sp
     * @param fontScale ({@link android.util.DisplayMetrics DisplayMetrics}
     *                  类中属性scaledDensity)
     * @return px
     */
    public static int sp2px(float spValue, float fontScale) { // NO_UCD (unused code)
        return (int) (spValue * fontScale + DELTA);
    }

    private static DisplayMetrics sDisplayMetrics;

    /**
     * 初始化DisplayMetrics
     *
     * @param context Context
     */
    private static void initDisplayMetrics(Context context) {
        if (null == sDisplayMetrics) {
            sDisplayMetrics = context.getResources().getDisplayMetrics();
        }
    }

    /**
     * 得到显示宽度
     *
     * @param context Context
     * @return 宽度
     */
    public static int getDisplayWidth(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 得到显示高度
     *
     * @param context Context
     * @return 高度
     */
    public static int getDisplayHeight(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.heightPixels;
    }
}