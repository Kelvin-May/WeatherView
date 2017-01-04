package cn.com.kelvin.weatherview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.animation.Interpolator;

import cn.com.kelvin.main.R;
import cn.com.kelvin.util.DisplayUtil;
import cn.com.kelvin.util.StringUtil;
import cn.com.kelvin.weatherview.base.BaseWeatherAdapter;
import cn.com.kelvin.weatherview.interpolators.CloudyParabolaInterpolator;

/**
 * Description: 暴雨
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:05
 * version V1.2.2
 */
public class RainStormAdapter extends BaseWeatherAdapter {

    private static final String TAG = RainStormAdapter.class.getSimpleName();

    private Context context;

    private Bitmap mRain, mCloud, mRaindrop0,
            mRaindrop1, mRaindrop2, mRaindrop3, mRaindrop4;


    private static final int ALPHA_START = 255;

    private static final float DURATION = 300.0f;
    private static final float TRANS_X = 50.0f;

    private float curDuration = 0;

    private String temperature = "";

    private int mCloudLeftP = 0, mCloudTopP = 0;
    private int mRainLeftP = 0, mRainTopP = 0;


    private int mRaindropLeftP = 0;
    private int mRaindropTopP = 0;

    private int mTempTextLeftP = 0, mTempTextTopP = 0;

    private int mTempTextWidth;
    private int mTempTextHeight;

    private final float density;

    private Matrix matrix = new Matrix();
    private Interpolator transInterpolator = new CloudyParabolaInterpolator();


    public RainStormAdapter(Context context) {
        this.context = context;
        density = DisplayUtil.getDensity(context);
    }

    @Override
    public void setTemperature(SurfaceView view, Paint paint, String temperature) {
        this.temperature = temperature;
        mTempTextLeftP = 0;
        mTempTextTopP = 0;
    }

    private boolean move = true;

    @Override
    public void onDrawWeather(SurfaceView view, Canvas canvas, Paint paint) throws Exception {
        if (canvas != null) {
            curDuration++;
            if (curDuration > DURATION) {
                curDuration = 0;
                move = !move;
            }
            final float interpolator = curDuration / DURATION;

            checkInit(view, paint);

            matrix.reset();
            matrix.postTranslate(mCloudLeftP + (move ? TRANS_X * transInterpolator.getInterpolation(interpolator) : 0), mCloudTopP);
            canvas.drawBitmap(mCloud, matrix, paint);

            canvas.drawBitmap(getRainBitmap(0), mRaindropLeftP, mRaindropTopP, paint);

            paint.setAlpha(ALPHA_START);
            //天气图标
            canvas.drawBitmap(mRain, mRainLeftP, mRainTopP, paint);
            if (!TextUtils.isEmpty(temperature)) {
                //画文字
                paint.setColor(Color.WHITE);
                paint.setTextSize(TEMP_TEXT_SIZE);
                canvas.drawText(temperature, mTempTextLeftP, mTempTextTopP, paint);
            }

//            if (curDuration == 0) {
//                // 播过一遍后就停一下
//                Thread.sleep(SLEEP_TIME);
//            }
        }
    }

    @Override
    public int getTouchLocationLeftTopX(SurfaceView view) {
        if (null == mRain) {
            // 雨参数
            mRain = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws4);
            mRainLeftP = (int) (view.getWidth() - mRain.getWidth() - 15 * density);
            mRainTopP = (int) ((view.getHeight() - mRain.getHeight()) / 2 - 3 * density);
        }
        return mRainLeftP;
    }

    @Override
    public int getTouchLocationLeftTopY(SurfaceView view) {
        if (null == mRain) {
            // 雨参数
            mRain = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws4);
            mRainLeftP = (int) (view.getWidth() - mRain.getWidth() - 15 * density);
            mRainTopP = (int) ((view.getHeight() - mRain.getHeight()) / 2 - 3 * density);
        }
        return mRainTopP;
    }

    private Bitmap getRainBitmap(int step) {
        if (step <= 50) {
            final float p = DURATION / 50;
            if (curDuration < p * step || curDuration > p * (step + 1)) {
                return getRainBitmap(step + 1);
            }
        }
        Log.d(TAG, "mRaindropStep = " + step % 5);
        switch (step % 5) {
            case 0: {
                return mRaindrop0;
            }
            case 1: {
                return mRaindrop1;
            }
            case 2: {
                return mRaindrop2;
            }
            case 3: {
                return mRaindrop3;
            }
            case 4: {
                return mRaindrop4;
            }
            default: {
                return mRaindrop0;
            }
        }
    }


    /**
     * 检查是否需要初始化，因为现在是在线程里进行的，所以可以把一些耗时的操作比如资源的初始化放在这里
     *
     * @param view
     */
    private void checkInit(SurfaceView view, Paint paint) {
        if (null == mRain) {
            // 雨参数
            mRain = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws4);
            mRainLeftP = (int) (view.getWidth() - mRain.getWidth() - 15 * density);
            mRainTopP = (int) ((view.getHeight() - mRain.getHeight()) / 2 - 3 * density);
        }
        if (null == mCloud) {
            // 云参数
            mCloud = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_cloudy_cloud);
            mCloudLeftP = view.getWidth() - mCloud.getWidth();
            mCloudTopP = (view.getHeight() - mCloud.getHeight()) / 2;
        }

        if (null == mRaindrop0) {
            // 雨滴
            mRaindrop0 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain1);
            mRaindropLeftP = view.getWidth() - mRaindrop0.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop1) {
            mRaindrop1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain2);
            mRaindropLeftP = view.getWidth() - mRaindrop1.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop2) {
            mRaindrop2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain3);
            mRaindropLeftP = view.getWidth() - mRaindrop2.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop3) {
            mRaindrop3 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain4);
            mRaindropLeftP = view.getWidth() - mRaindrop3.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop4) {
            mRaindrop4 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain5);
            mRaindropLeftP = view.getWidth() - mRaindrop4.getWidth();
            mRaindropTopP = 0;
        }

        if (mTempTextLeftP == 0 || mTempTextTopP == 0) {
            // 温度参数
            final int leftHeight = view.getHeight() - mRainTopP - mRain.getHeight() / 2;
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
            mTempTextLeftP = mRainLeftP + (mRain.getWidth() - mTempTextWidth) / 2;
            mTempTextTopP = mRainTopP + mRain.getHeight() + mTempTextHeight;
        }
    }
}
