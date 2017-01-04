package cn.com.kelvin.weatherview.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

/**
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:03
 * version V1.2.2
 */
public abstract class BaseWeatherAdapter {

    protected int TEMP_TEXT_SIZE = 28;

    public abstract void setTemperature(SurfaceView view, Paint paint, String temperature);

    public abstract void onDrawWeather(SurfaceView view, Canvas canvas, Paint paint) throws Exception;

    public abstract int getTouchLocationLeftTopX(SurfaceView view);
    public abstract int getTouchLocationLeftTopY(SurfaceView view);

}
