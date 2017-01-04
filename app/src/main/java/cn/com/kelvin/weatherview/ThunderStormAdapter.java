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
 * Description: 雷雨
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:05
 * version V1.2.2
 */
public class ThunderStormAdapter extends BaseWeatherAdapter {

    private static final String TAG = ThunderStormAdapter.class.getSimpleName();

    private Context context;

    private Bitmap mThunderStorm, mCloud, mRaindrop1,
            mRaindrop2, mRaindrop3, mRaindrop4, mRaindrop5,
            mThunder, mThunderSmall;


    private static final int ALPHA_START = 255;
    private static final int ALPHA_END = 125;
    private static final int SLEEP_TIME = 200;

    private String temperature = "";

    private int mCloudLeftP = 0, mCloudTopP = 0;
    private int mRainLeftP = 0, mRainTopP = 0;


    private int mRaindropLeftP = 0;
    private int mRaindropTopP = 0;

    private int mTempTextLeftP = 0, mTempTextTopP = 0;

    private int mTempTextWidth;
    private int mTempTextHeight;

    private final float density;

    private int mRaindropStep = 0;

    private int mThunderLeftP = 0;
    private int mThunderTopP = 0;
    private int mSmallThunderLeftP = 0;
    private int mSmallThunderTopP = 0;


    public ThunderStormAdapter(Context context) {
        this.context = context;
        density = DisplayUtil.getDensity(context);
    }

    @Override
    public void setTemperature(SurfaceView view, Paint paint, String temperature) {
        this.temperature = temperature;
        mTempTextLeftP = 0;
        mTempTextTopP = 0;
    }

    int thunder_step = 0;
    @Override
    public void onDrawWeather(SurfaceView view, Canvas canvas, Paint paint) throws Exception {
        if (canvas != null) {

            checkInit(view, paint);

            //闪电效果
            thunder_step ++;
            switch (thunder_step){
                case 1:{
                    paint.setAlpha(ALPHA_START);
                    canvas.drawBitmap(mThunderSmall, mSmallThunderLeftP,
                            mSmallThunderTopP, paint);
                }
                break;
                case 2:{
                    paint.setAlpha(ALPHA_END);
                    canvas.drawBitmap(mThunderSmall, mSmallThunderLeftP,
                            mSmallThunderTopP, paint);
                    canvas.drawBitmap(mThunder, mThunderLeftP,
                            mThunderTopP, paint);
                }
                break;
                case 3:{
                    paint.setAlpha(ALPHA_START);
                    canvas.drawBitmap(mThunder, mThunderLeftP,
                            mThunderTopP, paint);
                }
                break;
                case 4:{
                    paint.setAlpha(ALPHA_END);
                    canvas.drawBitmap(mThunderSmall, mSmallThunderLeftP,
                            mSmallThunderTopP, paint);
                    canvas.drawBitmap(mThunder, mThunderLeftP,
                            mThunderTopP, paint);
                }
                break;
                case 5:{
                    paint.setAlpha(ALPHA_START);
                    canvas.drawBitmap(mThunderSmall, mSmallThunderLeftP,
                            mSmallThunderTopP, paint);
                }
                break;
                case 6:{
                    paint.setAlpha(ALPHA_END);
                    canvas.drawBitmap(mThunderSmall, mSmallThunderLeftP,
                            mSmallThunderTopP, paint);
                }
                break;

                //以下7,8,9,10不画闪电图标，主要实现类似闪电图标休眠效果
                case 7:{}
                break;
                case 8:{}
                break;
                case 9:{}
                break;
                case 10:{
                    thunder_step = 0;
                }
                break;
                default:{
                    paint.setAlpha(0);
                }
            }

            switch (mRaindropStep){
                case 1:{
                    canvas.drawBitmap(mRaindrop1, mRaindropLeftP, mRaindropTopP, paint);
                }
                break;
                case 2:{
                    canvas.drawBitmap(mRaindrop2, mRaindropLeftP, mRaindropTopP, paint);
                }
                break;
                case 3:{
                    canvas.drawBitmap(mRaindrop3, mRaindropLeftP, mRaindropTopP, paint);
                }
                break;
                case 4:{
                    canvas.drawBitmap(mRaindrop4, mRaindropLeftP, mRaindropTopP, paint);
                }
                break;
                case 5:{
                    canvas.drawBitmap(mRaindrop5, mRaindropLeftP, mRaindropTopP, paint);
                    mRaindropStep = 0;
                }
                break;
                default:{
                    canvas.drawBitmap(mRaindrop1, mRaindropLeftP, mRaindropTopP, paint);
                }
            }

            mRaindropStep++;

            paint.setAlpha(ALPHA_START);
            //画云
            canvas.drawBitmap(mCloud, mCloudLeftP, mCloudTopP, paint);
            //天气图标
            canvas.drawBitmap(mThunderStorm, mRainLeftP, mRainTopP, paint);
            if (!TextUtils.isEmpty(temperature)) {
                //画文字
                paint.setColor(Color.WHITE);
                paint.setTextSize(TEMP_TEXT_SIZE);
                canvas.drawText(temperature, mTempTextLeftP, mTempTextTopP, paint);
            }

            Thread.sleep(SLEEP_TIME);
        }
    }

    @Override
    public int getTouchLocationLeftTopX(SurfaceView view) {
        if (null == mThunderStorm) {
            // 雨参数
            mThunderStorm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws5);
            mRainLeftP = (int) (view.getWidth() - mThunderStorm.getWidth() - 15 * density);
            mRainTopP = (int) ((view.getHeight() - mThunderStorm.getHeight()) / 2 - 3 * density);
        }
        return mRainLeftP;
    }

    @Override
    public int getTouchLocationLeftTopY(SurfaceView view) {
        if (null == mThunderStorm) {
            // 雨参数
            mThunderStorm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws5);
            mRainLeftP = (int) (view.getWidth() - mThunderStorm.getWidth() - 15 * density);
            mRainTopP = (int) ((view.getHeight() - mThunderStorm.getHeight()) / 2 - 3 * density);
        }
        return mRainTopP;
    }

    /**
     * 检查是否需要初始化，因为现在是在线程里进行的，所以可以把一些耗时的操作比如资源的初始化放在这里
     *
     * @param view SurfaceView
     */
    private void checkInit(SurfaceView view, Paint paint) {
        if (null == mThunderStorm) {
            // 雨参数
            mThunderStorm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ws5);
            mRainLeftP = (int) (view.getWidth() - mThunderStorm.getWidth() - 15 * density);
            mRainTopP = (int) ((view.getHeight() - mThunderStorm.getHeight()) / 2 - 3 * density);
        }
        if (null == mCloud) {
            // 云参数
            mCloud = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_cloudy_cloud);
            mCloudLeftP = view.getWidth() - mCloud.getWidth();
            mCloudTopP = (view.getHeight() - mCloud.getHeight()) / 2;
        }

        if (null == mRaindrop1) {
            // 雨滴
            mRaindrop1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain1);
            mRaindropLeftP = view.getWidth() - mRaindrop1.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop2) {
            mRaindrop2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain2);
            mRaindropLeftP = view.getWidth() - mRaindrop2.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop3) {
            mRaindrop3 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain3);
            mRaindropLeftP = view.getWidth() - mRaindrop3.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop4) {
            mRaindrop4 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain4);
            mRaindropLeftP = view.getWidth() - mRaindrop4.getWidth();
            mRaindropTopP = 0;
        }

        if (null == mRaindrop5) {
            mRaindrop5 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_rain5);
            mRaindropLeftP = view.getWidth() - mRaindrop5.getWidth();
            mRaindropTopP = 0;
        }

        if(null == mThunder){
            mThunder = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_thunder);
            mThunderLeftP = view.getWidth() - mCloud.getWidth()/2;
            mThunderTopP = 0;
        }

        if(null == mThunderSmall){
            mThunderSmall = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_thunder_small);
            mSmallThunderLeftP = (int)(view.getWidth() - mCloud.getWidth()/2 - 20*density);
            mSmallThunderTopP = view.getHeight() - mThunderSmall.getHeight();
        }

        if (mTempTextLeftP == 0 || mTempTextTopP == 0) {
            // 温度参数
            final int leftHeight = view.getHeight() - mRainTopP - mThunderStorm.getHeight() / 2;
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
            mTempTextLeftP = mRainLeftP + (mThunderStorm.getWidth() - mTempTextWidth) / 2;
            mTempTextTopP = mRainTopP + mThunderStorm.getHeight() + mTempTextHeight;
        }
    }
}
