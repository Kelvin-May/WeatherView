package cn.com.kelvin.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * author Kelvin
 * Date: 2015-09-21
 * Time: 11:11
 * version V1.0
 */
public class DisplayUtil {

    /**
     * Covert dp to px
     *
     * @param dp
     * @param context
     * @return pixel
     */
    public static float convertDpToPixel(float dp, Context context) {
        float px = dp * getDensity(context);
        return px;
    }

    /**
     * Covert px to dp
     *
     * @param px
     * @param context
     * @return dp
     */
    public static float convertPixelToDp(float px, Context context) {
        float dp = px / getDensity(context);
        return dp;
    }

    /**
     * Covert px to dp
     *
     * @param px
     * @return dp
     */
    public static float convertPixelToDp(float px) {
        return convertPixelToDp(px, null);
    }

    /**
     * 取得螢幕密度
     * 120dpi = 0.75
     * 160dpi = 1 (default)
     * 240dpi = 1.5
     *
     * @param
     * @return
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    /**
     * dip 2 px
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        // return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics())+0.5f);
        return (int) (dipValue * getDensity(context) + 0.5f);
    }

    /**
     * px 2 dip
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context,float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    /**
     * 在activity中获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return statusHeight;
    }
}
