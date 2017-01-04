package cn.com.kelvin.weatherview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import cn.com.kelvin.main.R;
import cn.com.kelvin.util.DisplayUtil;
import cn.com.kelvin.util.StringUtil;
import cn.com.kelvin.weatherview.base.BaseWeatherAdapter;
import cn.com.kelvin.weatherview.interpolators.DownParabolaInterpolator;

/**
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:05
 * version V1.2.2
 */
public class SunAdapter extends BaseWeatherAdapter {

    private static final String TAG = SunAdapter.class.getSimpleName();

    private Context context;

    private Matrix sunshineMatrix = new Matrix();

    private Bitmap mSun, mCloud, mSunshine;

    private static final int SLEEP_TIME = 2000;

    private static final int MAX_ROTATE_ANGLE = 30;

    private static final int ALPHA_START = 255;

    private static final float DURATION = 250.0f;

    private float curDuration = 0.0f;

    private String temperature = "";

    private int mSunshineRPX = 0, mSunshineRPY = 0, mSunshineTPX = 0, mSunshineTPY = 0;

    private int mCloudLeftP = 0, mCloudTopP = 0;
    private int mSunLeftP = 0, mSunTopP = 0;

    private int mTempTextLeftP = 0, mTempTextTopP = 0;

    private int mTempTextWidth;
    private int mTempTextHeight;

    private final float density;

    private Interpolator rotateInterpolator = new DecelerateInterpolator();

    private Interpolator alphaInterpolator = new DownParabolaInterpolator();

    public SunAdapter(Context context) {
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
            curDuration++;
            if (curDuration > DURATION) {
                curDuration = 0;
            }
            final float interpolator = curDuration / DURATION;
            checkInit(view, paint);
            //画阳光效果
            paint.setAlpha((int) (ALPHA_START * alphaInterpolator.getInterpolation(interpolator)));
            sunshineMatrix.reset();
            sunshineMatrix.postRotate(-(MAX_ROTATE_ANGLE * rotateInterpolator.getInterpolation(interpolator)), mSunshineRPX, mSunshineRPY);
            sunshineMatrix.postTranslate(mSunshineTPX, mSunshineTPY);
            canvas.drawBitmap(mSunshine, sunshineMatrix, paint);

            paint.setAlpha(ALPHA_START);
            //画云
            canvas.drawBitmap(mCloud, mCloudLeftP, mCloudTopP, paint);
            //天气图标
            canvas.drawBitmap(mSun, mSunLeftP, mSunTopP, paint);
            if (!TextUtils.isEmpty(temperature)) {
                //画文字
                paint.setColor(Color.WHITE);
                paint.setTextSize(TEMP_TEXT_SIZE);
                canvas.drawText(temperature, mTempTextLeftP, mTempTextTopP, paint);
            }
            if (curDuration == 0.0f) {
                // 播过一遍后就停一下
                Thread.sleep(SLEEP_TIME);
            }
        }
    }

    @Override
    public int getTouchLocationLeftTopX(SurfaceView view) {
        if (null == mSun) {
            // 太阳参数
            mSun = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws1);
            mSunLeftP = (int) (view.getWidth() - mSun.getWidth() - 15 * density);
            mSunTopP = (int) ((view.getHeight() - mSun.getHeight()) / 2 - 3 * density);
        }
        return mSunLeftP;
    }

    @Override
    public int getTouchLocationLeftTopY(SurfaceView view) {
        if (null == mSun) {
            // 太阳参数
            mSun = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws1);
            mSunLeftP = (int) (view.getWidth() - mSun.getWidth() - 15 * density);
            mSunTopP = (int) ((view.getHeight() - mSun.getHeight()) / 2 - 3 * density);
        }
        return mSunTopP;
    }

    /**
     * 检查是否需要初始化，因为现在是在线程里进行的，所以可以把一些耗时的操作比如资源的初始化放在这里
     *
     * @param view
     */
    private void checkInit(SurfaceView view, Paint paint) {
        if (null == mSun) {
            // 太阳参数
            mSun = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws1);
            mSunLeftP = (int) (view.getWidth() - mSun.getWidth() - 15 * density);
            mSunTopP = (int) ((view.getHeight() - mSun.getHeight()) / 2 - 3 * density);
        }
        if (null == mCloud) {
            // 云参数
            mCloud = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_sun_cloud);
            mCloudLeftP = view.getWidth() - mCloud.getWidth();
            mCloudTopP = (view.getHeight() - mCloud.getHeight()) / 2;
        }
        if (null == mSunshine) {
            // 阳光参数
            mSunshine = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_sunshine);
            mSunshineRPX = mSunshine.getWidth() / 2;
            mSunshineRPY = mSunshine.getHeight() / 2;
            mSunshineTPX = view.getWidth() - mSunshine.getWidth();
            mSunshineTPY = (view.getHeight() - mSunshine.getHeight()) / 2;
        }
        if (mTempTextLeftP == 0 || mTempTextTopP == 0) {
            // 温度参数
            final int leftHeight = (int) (view.getHeight() - mSunTopP - mSun.getHeight() - 2 * density);
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
            mTempTextLeftP = mSunLeftP + (mSun.getWidth() - mTempTextWidth) / 2;
            mTempTextTopP = mSunTopP + mSun.getHeight() + mTempTextHeight;
        }
    }
}
