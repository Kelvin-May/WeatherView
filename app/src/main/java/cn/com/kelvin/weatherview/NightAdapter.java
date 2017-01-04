package cn.com.kelvin.weatherview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.SurfaceView;

import cn.com.kelvin.main.R;
import cn.com.kelvin.util.DisplayUtil;
import cn.com.kelvin.util.StringUtil;
import cn.com.kelvin.weatherview.base.BaseWeatherAdapter;

/**
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:05
 * version V1.2.2
 */
public class NightAdapter extends BaseWeatherAdapter {

    private static final String TAG = NightAdapter.class.getSimpleName();

    private Context context;

    private Bitmap mNight, mStar;

    private static final int ALPHA_START = 255;
    private static final int SLEEP_TIME = 2000;

    private String temperature = "";

    private int mNightLeftP = 0, mNightTopP = 0;

    private int mTempTextLeftP = 0, mTempTextTopP = 0;

    private int mTempTextWidth;
    private int mTempTextHeight;

    private int mLeftStarLeftP = 0;
    private int mLeftStarTopP = 0;

    private int mRightStarLeftP = 0;
    private int mRightStarTopP = 0;

    private final float density;

    public NightAdapter(Context context) {
        this.context = context;
        density = DisplayUtil.getDensity(context);
    }

    @Override
    public void setTemperature(SurfaceView view, Paint paint, String temperature) {
        this.temperature = temperature;
        mTempTextLeftP = 0;
        mTempTextTopP = 0;
    }


    @Override
    public void onDrawWeather(SurfaceView view, Canvas canvas, Paint paint) throws Exception {
        if (canvas != null) {

            checkInit(view, paint);
            paint.setAlpha(ALPHA_START);
            //天气图标
            canvas.drawBitmap(mNight, mNightLeftP, mNightTopP, paint);

            //star图标
            canvas.drawBitmap(mStar, mLeftStarLeftP, mLeftStarTopP, paint);

            canvas.drawBitmap(mStar, mRightStarLeftP, mRightStarTopP, paint);

            if (!TextUtils.isEmpty(temperature)) {
                //画文字
                paint.setColor(Color.WHITE);
                paint.setTextSize(TEMP_TEXT_SIZE);
                canvas.drawText(temperature, mTempTextLeftP, mTempTextTopP, paint);
            }
        }
    }

    @Override
    public int getTouchLocationLeftTopX(SurfaceView view) {
        if (null == mNight) {
            // 雨参数
            mNight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws6);
            mNightLeftP = (int) (view.getWidth() - mNight.getWidth() - 15 * density);
            mNightTopP = (int) ((view.getHeight() - mNight.getHeight()) / 2 - 3 * density);
        }
        return mNightLeftP;
    }

    @Override
    public int getTouchLocationLeftTopY(SurfaceView view) {
        if (null == mNight) {
            // 雨参数
            mNight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws6);
            mNightLeftP = (int) (view.getWidth() - mNight.getWidth() - 15 * density);
            mNightTopP = (int) ((view.getHeight() - mNight.getHeight()) / 2 - 3 * density);
        }
        return mNightTopP;
    }

    /**
     * 检查是否需要初始化，因为现在是在线程里进行的，所以可以把一些耗时的操作比如资源的初始化放在这里
     *
     * @param view SurfaceView
     */
    private void checkInit(SurfaceView view, Paint paint) {
        if (null == mNight) {
            mNight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws6);
            mNightLeftP = (int) (view.getWidth() - mNight.getWidth() - 15 * density);
            mNightTopP = (int) ((view.getHeight() - mNight.getHeight()) / 2 - 3 * density);
        }

        if(null == mStar){
            mStar = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_night_star);
            mRightStarLeftP = (int)(view.getWidth() - mStar.getWidth()- 15 * density);
            mRightStarTopP = 0;

            mLeftStarLeftP = (int)(view.getWidth() - mNight.getWidth() - mStar.getWidth()-15 * density);
            mLeftStarTopP = (view.getHeight() - mNight.getHeight()) / 2 + mStar.getHeight();

        }

        if (mTempTextLeftP == 0 || mTempTextTopP == 0) {
            // 温度参数
            final int leftHeight = view.getHeight() - mNightTopP - mNight.getHeight() / 2;
            while (TEMP_TEXT_SIZE > 0) {
                // 如果整体高度比较小，那么就把字缩小点
                paint.setTextSize(TEMP_TEXT_SIZE);
                mTempTextHeight = StringUtil.getTextHeight(temperature, paint);
                if (leftHeight < mTempTextHeight) {
                    TEMP_TEXT_SIZE--;
                } else {
                    break;
                }
            }
            mTempTextWidth = StringUtil.getTextWidth(temperature, paint);
            mTempTextLeftP = mNightLeftP + (mNight.getWidth() - mTempTextWidth) / 2;
            mTempTextTopP = mNightTopP + mNight.getHeight() + mTempTextHeight;
        }
    }
}
