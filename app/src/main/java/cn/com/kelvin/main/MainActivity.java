package cn.com.kelvin.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.com.kelvin.weatherview.WeatherSurfaceView;

public class MainActivity extends Activity {

    private WeatherSurfaceView mWeatherSurfaceView;

    private Button mChangeBtn;

    private int selectType = -1;

    private static final String[] WEATHERS = {
            "晴",
            "多云",
            "小雨",
            "暴雨",
            "雷电雨",
            "夜晚"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeatherSurfaceView.changeState(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWeatherSurfaceView.changeState(false);
    }

    private void initUI() {
        mWeatherSurfaceView = (WeatherSurfaceView) findViewById(R.id.id_main_activity_weather);
        mChangeBtn = (Button) findViewById(R.id.id_main_activity_change_weather);
        mChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectAlert();
            }
        });
    }

    private void initData() {
//        mWeatherSurfaceView.switchWeather(WeatherSurfaceView.TYPE_WEATHER_RAINSTORM, "25°C");
//        mWeatherSurfaceView.changeState(true);
        mChangeBtn.setText("请点击选择天气");
    }

    private void showSelectAlert() {
        final int markType = selectType;
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("请选择天气：")
                .setSingleChoiceItems(WEATHERS,
                        selectType,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectType = which;
                            }
                        })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWeatherSurfaceView.setVisibility(View.VISIBLE);
                        mWeatherSurfaceView.switchWeather(selectType, "25°C");

                        String title = "您选择了：" + WEATHERS[selectType];
                        mChangeBtn.setText(title);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectType = markType;
                    }
                }).create();
        dialog.show();
    }

}
