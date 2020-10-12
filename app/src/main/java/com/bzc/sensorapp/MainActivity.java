package com.bzc.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private SensorManager mSensorManager;
    private ListView mShowSensorInfoList;//展示手机支持的所有传感器设备
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    //进行传感器数据的获取
    private void initData() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SensorManagerActivity.class);
                startActivity(intent);
            }
        });
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取所有的传感器并显示展出
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        List<String> sensorInfo = getSensorInfo(sensorList);
        //展示界面上面
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,sensorInfo);
        mShowSensorInfoList.setAdapter(adapter);
    }

    //初始化控件元素
    private void initView() {
        mShowSensorInfoList = findViewById(R.id.showSensorInfo);
        mImageView = findViewById(R.id.addimageview);
    }

    private List<String> getSensorInfo(List<Sensor> allSensors){
        List<String> infoList = new ArrayList<>();
        for(Sensor sensor : allSensors) {
            StringBuilder stringBuilder = new StringBuilder();
            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    stringBuilder.append("加速度传感器(Accelerometer sensor)\n");
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    stringBuilder.append("陀螺仪传感器(Gyroscope sensor)\n");
                    break;
                case Sensor.TYPE_LIGHT:
                    stringBuilder.append("光线传感器(Light sensor)\n");
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    stringBuilder.append("磁场传感器(Magnetic field sensor)\n");
                    break;
                case Sensor.TYPE_ORIENTATION:
                    stringBuilder.append("方向传感器(Orientation sensor)\n");
                    break;
                case Sensor.TYPE_PRESSURE:
                    stringBuilder.append("气压传感器(Pressure sensor)\n");
                    break;
                case Sensor.TYPE_PROXIMITY:
                    stringBuilder.append("距离传感器(Proximity sensor)\n");
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    stringBuilder.append("温度传感器(Temperature sensor)\n");
                    break;
                default:
                    stringBuilder.append("其他传感器\n");
                    break;
            }
            stringBuilder.append("设备名称：" + sensor.getName() + "\n设备版本：" + sensor.getVersion() + "\n供应商："
                    + sensor.getVendor() );
            infoList.add(stringBuilder.toString());
        }
        return infoList;
    }
}