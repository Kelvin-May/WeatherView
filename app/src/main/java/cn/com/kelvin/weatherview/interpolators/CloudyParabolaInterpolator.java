package cn.com.kelvin.weatherview.interpolators;

import android.view.animation.Interpolator;

/**
 * Title: mlsz
 * Package cn.com.hcfdata.library.widgets.weather
 * Description: 云朵飘动专用的抛物线
 * Copyright: Copyright (c) 2016
 * Company:深圳华成峰数据有限公司
 * author Kelvin (yangjk@szhcf.com.cn)
 * Date: 2016/8/12 0012
 * Time: 下午 2:58
 * version V1.2.2
 */
public class CloudyParabolaInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        return 4 * (input * input - input);
    }
}
