package cn.com.kelvin.weatherview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.com.kelvin.weatherview.base.BaseWeatherAdapter;
import cn.com.kelvin.weatherview.base.BaseWeatherSurfaceView;

/**
 * Title: mlsz
 * Package cn.com.hcfdata.library.widgets.weather
 * Description: 天气绘制View
 * Copyright: Copyright (c) 2016
 * Company:深圳华成峰数据有限公司
 * author Kelvin (yangjk@szhcf.com.cn)
 * Date: 2016/8/11 0011
 * Time: 上午 11:45
 * version V1.2.2
 */
public class WeatherSurfaceView extends BaseWeatherSurfaceView {

    private static final String TAG = WeatherSurfaceView.class.getSimpleName();

    private int curType = TYPE_WEATHER_SUN;

    public static final int TYPE_WEATHER_SUN = 0;
    public static final int TYPE_WEATHER_CLOUDY = 1;
    public static final int TYPE_WEATHER_RAIN = 2;
    public static final int TYPE_WEATHER_RAINSTORM = 3;
    public static final int TYPE_WEATHER_THUNDERSTORM = 4;
    public static final int TYPE_WEATHER_NIGHT = 5;

    private int touchX, touchY;

    private OnWeatherClickListener onWeatherClickListener;

    public WeatherSurfaceView(Context context) {
        super(context);
    }

    public WeatherSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnWeatherClickListener(OnWeatherClickListener listener){
        this.onWeatherClickListener = listener;
    }

    public void switchWeather(final int type, final String temp) {
        if (curType != type) {
            curType = type;
            BaseWeatherAdapter adapter = newWeatherAdapter(type);
            adapter.setTemperature(this, mPaint, temp);
            setWeatherAdapter(adapter);
        } else {
            // Notify 下温度就行
            BaseWeatherAdapter adapter = getWeatherAdapter();
            if (null != adapter) {
                adapter.setTemperature(this, mPaint, temp);
            } else {
                adapter = newWeatherAdapter(type);
                adapter.setTemperature(this, mPaint, temp);
                touchX = adapter.getTouchLocationLeftTopX(this);
                touchY = adapter.getTouchLocationLeftTopY(this);
                setWeatherAdapter(adapter);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int curX = (int) event.getX();
                int curY = (int) event.getY();
                if (curX > touchX && curY > touchY && curY < this.getHeight()) {
                    if (null != onWeatherClickListener) {
                        if (isWeatherViewShow()) {
                            onWeatherClickListener.onWeatherClick();
                        }
                    }
                }
            }
            break;
        }
        return super.onTouchEvent(event);
    }



    private BaseWeatherAdapter newWeatherAdapter(final int type) {
        BaseWeatherAdapter adapter = null;
        switch (type) {
            case TYPE_WEATHER_SUN: {
                adapter = new SunAdapter(getContext());
            }
            break;

            case TYPE_WEATHER_CLOUDY: {
                adapter = new CloudyAdapter(getContext());
            }
            break;
            case TYPE_WEATHER_RAIN: {
                adapter = new RainAdapter(getContext());
            }
            break;

            case TYPE_WEATHER_RAINSTORM: {
                adapter = new RainStormAdapter(getContext());
            }
            break;

            case TYPE_WEATHER_THUNDERSTORM: {
                adapter = new ThunderStormAdapter(getContext());
            }
            break;

            case TYPE_WEATHER_NIGHT: {
                adapter = new NightAdapter(getContext());
            }
            break;
            default: {
                adapter = new SunAdapter(getContext());
            }
            break;
        }
        return adapter;
    }

    public interface OnWeatherClickListener{
        void onWeatherClick();
    }

}
