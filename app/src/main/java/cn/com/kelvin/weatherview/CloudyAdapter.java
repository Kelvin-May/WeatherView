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
import android.view.animation.Interpolator;

import cn.com.kelvin.main.R;
import cn.com.kelvin.util.DisplayUtil;
import cn.com.kelvin.util.StringUtil;
import cn.com.kelvin.weatherview.base.BaseWeatherAdapter;
import cn.com.kelvin.weatherview.interpolators.CloudyParabolaInterpolator;

/**
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:05
 * version V1.2.2
 */
public class CloudyAdapter extends BaseWeatherAdapter {

    private static final String TAG = CloudyAdapter.class.getSimpleName();

    private Context context;

    private Bitmap mCloudy, mCloud;

    private static final float DURATION = 300.0f;
    private static final float TRANS_X = 50.0f;

    private float curDuration = 0;


    private static final int ALPHA_START = 255;
    private static final int SLEEP_TIME = 500;

    private String temperature = "";

    private int mCloudLeftP = 0, mCloudTopP = 0;
    private int mCloudyLeftP = 0, mCloudyTopP = 0;


    private int mTempTextLeftP = 0, mTempTextTopP = 0;

    private int mTempTextWidth;
    private int mTempTextHeight;

    private final float density;
    private Matrix matrix = new Matrix();
    private Interpolator transInterpolator = new CloudyParabolaInterpolator();



    public CloudyAdapter(Context context) {
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
            matrix.reset();
            matrix.postTranslate(mCloudLeftP + TRANS_X * transInterpolator.getInterpolation(interpolator), mCloudTopP);
            canvas.drawBitmap(mCloud, matrix, paint);

            paint.setAlpha(ALPHA_START);
            //天气图标
            canvas.drawBitmap(mCloudy, mCloudyLeftP, mCloudyTopP, paint);
            if (!TextUtils.isEmpty(temperature)) {
                //画文字
                paint.setColor(Color.WHITE);
                paint.setTextSize(TEMP_TEXT_SIZE);
                canvas.drawText(temperature, mTempTextLeftP, mTempTextTopP, paint);
            }

            if (curDuration == 0) {
                // 播过一遍后就停一下
                Thread.sleep(SLEEP_TIME);
            }
        }
    }

    @Override
    public int getTouchLocationLeftTopX(SurfaceView view) {
        if (null == mCloudy) {
            mCloudy = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws2);
            mCloudyLeftP = (int) (view.getWidth() - mCloudy.getWidth() - 15 * density);
            mCloudyTopP = (int) ((view.getHeight() - mCloudy.getHeight()) / 2 - 3 * density);
        }

        return mCloudyLeftP;
    }

    @Override
    public int getTouchLocationLeftTopY(SurfaceView view) {
        if (null == mCloudy) {
            mCloudy = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws2);
            mCloudyLeftP = (int) (view.getWidth() - mCloudy.getWidth() - 15 * density);
            mCloudyTopP = (int) ((view.getHeight() - mCloudy.getHeight()) / 2 - 3 * density);
        }

        return mCloudyTopP;
    }

    /**
     * 检查是否需要初始化，因为现在是在线程里进行的，所以可以把一些耗时的操作比如资源的初始化放在这里
     *
     * @param view
     */
    private void checkInit(SurfaceView view, Paint paint) {
        if (null == mCloudy) {
            // 雨参数
            mCloudy = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws2);
            mCloudyLeftP = (int) (view.getWidth() - mCloudy.getWidth() - 15 * density);
            mCloudyTopP = (int) ((view.getHeight() - mCloudy.getHeight()) / 2 - 3 * density);
        }
        if (null == mCloud) {
            // 云参数
            mCloud = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_cloudy_cloud);
            mCloudLeftP = view.getWidth() - mCloud.getWidth();
            mCloudTopP = (view.getHeight() - mCloud.getHeight()) / 2;
        }
        if (mTempTextLeftP == 0 || mTempTextTopP == 0) {
            // 温度参数
            final int leftHeight = view.getHeight() - mCloudyTopP - mCloudy.getHeight() / 2;
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
            mTempTextLeftP = mCloudyLeftP + (mCloudy.getWidth() - mTempTextWidth) / 2;
            mTempTextTopP = mCloudyTopP + mCloudy.getHeight() + mTempTextHeight;
        }
    }
}
