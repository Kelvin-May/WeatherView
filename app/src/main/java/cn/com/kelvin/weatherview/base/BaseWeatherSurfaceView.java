package cn.com.kelvin.weatherview.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * author Kelvin
 * Date: 2016/8/12 0012
 * Time: 上午 11:20
 * version V1.2.2
 */
public class BaseWeatherSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private BaseWeatherAdapter mWeatherAdapter;

    private boolean isRunning = true;

    private boolean isStart = false;

    private View maskView;

    private SurfaceHolder surfaceHolder;

    protected Paint mPaint;

    private int background_color; //天气View的背景色

    public BaseWeatherSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public BaseWeatherSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWeatherSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        background_color = context.getResources().getColor(android.R.color.holo_blue_bright);
        new Thread(mDrawRunnable).start();
    }

    private Runnable mDrawRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                Canvas canvas = null;
                if (isStart && null != mWeatherAdapter) {
                    synchronized (BaseWeatherSurfaceView.this) {
                        try {
                            canvas = surfaceHolder.lockCanvas();
                            if (null != canvas) {
                                canvas.drawColor(background_color);
                                if (null == mPaint) {
                                    mPaint = new Paint();
                                    mPaint.setAntiAlias(true);
                                    mPaint.setFilterBitmap(true);
                                }
                                mWeatherAdapter.onDrawWeather(BaseWeatherSurfaceView.this, canvas, mPaint);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (canvas != null) {
                                try {
                                    surfaceHolder.unlockCanvasAndPost(canvas);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    synchronized (BaseWeatherSurfaceView.this) {
                        try {
                            canvas = surfaceHolder.lockCanvas();
                            // 这里画一下的原因就是防止第一次显示的时候黑框;
                            if (null != canvas) {
                                canvas.drawColor(background_color);
                                surfaceHolder.unlockCanvasAndPost(canvas);
                                canvas = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (canvas != null) {
                                try {
                                    surfaceHolder.unlockCanvasAndPost(canvas);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        destroySurface();
    }

    /**
     * 销毁SurfaceView
     */
    public void destroySurface() {
        isRunning = false;
        synchronized (mDrawRunnable) {
            mDrawRunnable.notify();
        }
    }


    public void setMaskView(View maskView) {
        this.maskView = maskView;
    }

    public void setWeatherAdapter(final BaseWeatherAdapter adapter) {
        this.mWeatherAdapter = adapter;
        changeState(true);
        setVisibility(View.VISIBLE);
    }

    public BaseWeatherAdapter getWeatherAdapter() {
        return mWeatherAdapter;
    }

    private AlphaAnimation maskAnim;

    public void changeState(boolean start) {
        if (null != mWeatherAdapter && isStart != start) {
            isStart = start;
            synchronized (mDrawRunnable) {
                mDrawRunnable.notify();
            }
            if (null != maskView) {
                if (isStart) {
                    if (null == maskAnim) {
                        maskAnim = new AlphaAnimation(1.0f, 0.0f);
                        maskAnim.setDuration(1000);
                        maskAnim.setFillAfter(true);
                    }
                    maskView.clearAnimation();
                    maskView.startAnimation(maskAnim);
                } else {
                    maskView.clearAnimation();
                    maskView.setAlpha(1.0f);
                }
            }
        }
    }

    public boolean isWeatherViewShow() {

        return getVisibility() == View.VISIBLE && maskView.getAlpha() == 1.0f;
    }
}
