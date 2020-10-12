package com.bzc.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class SensorManagerActivity extends AppCompatActivity   implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mMagneticSensor;//磁性传感器
    private Sensor mAccelerometerSensor;//加速计传感器
    private Sensor mGyroscopeSensor;//陀螺仪传感器
    private float mTimestamp;
    private float mAngle[] = new float[3];
    private static final float NS2S = 1.0f / 1000000000.0f;

    private EditText mMagneticSensortime; //采集时间
    private EditText mMagneticSensorvalue; //最大量程
    private EditText mMagneticSensorvalueX;
    private EditText mMagneticSensorvalueY;
    private EditText mMagneticSensorvalueZ;

    private EditText mAccelerometerSensortime; //采集时间
    private EditText mAccelerometerSensorvalue; //最大量程
    private EditText mAccelerometerSensorvalueX;
    private EditText mAccelerometerSensorvalueY;
    private EditText mAccelerometerSensorvalueZ;
    private EditText mAccelerometerSensorvalueCount;

    private EditText mGyroscopeSensortime; //采集时间
    private EditText mGyroscopeSensorvalue; //最大量程
    private EditText mGyroscopeSensorvalueX;
    private EditText mGyroscopeSensorvalueY;
    private EditText mGyroscopeSensorvalueZ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_manager);
        initView();
        initData();
    }

    private void initData() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取所有的传感器并显示展出
        //磁性传感器
        mMagneticSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //加速计传感器
        mAccelerometerSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //陀螺仪传感器
        mGyroscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //注册陀螺仪传感器，并设定传感器向应用中输出的时间间隔类型是SensorManager.SENSOR_DELAY_GAME(20000微秒)
        //SensorManager.SENSOR_DELAY_FASTEST(0微秒)：最快。最低延迟，一般不是特别敏感的处理不推荐使用，该模式可能在成手机电力大量消耗，由于传递的为原始数据，诉法不处理好会影响游戏逻辑和UI的性能
        //SensorManager.SENSOR_DELAY_GAME(20000微秒)：游戏。游戏延迟，一般绝大多数的实时性较高的游戏都是用该级别
        //SensorManager.SENSOR_DELAY_NORMAL(200000微秒):普通。标准延时，对于一般的益智类或EASY级别的游戏可以使用，但过低的采样率可能对一些赛车类游戏有跳帧现象
        //SensorManager.SENSOR_DELAY_UI(60000微秒):用户界面。一般对于屏幕方向自动旋转使用，相对节省电能和逻辑处理，一般游戏开发中不使用
        mSensorManager.registerListener(this, mGyroscopeSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    private void initView() {

        mMagneticSensortime = findViewById(R.id.magneticSensortime);
        mMagneticSensorvalue = findViewById(R.id.magneticSensorvalue);
        mMagneticSensorvalueX = findViewById(R.id.magneticSensorvalueX);
        mMagneticSensorvalueY = findViewById(R.id.magneticSensorvalueY);
        mMagneticSensorvalueZ = findViewById(R.id.magneticSensorvalueZ);

        mAccelerometerSensortime = findViewById(R.id.accelerometerSensortime);
        mAccelerometerSensorvalue = findViewById(R.id.accelerometerSensorvalue);
        mAccelerometerSensorvalueX = findViewById(R.id.accelerometerSensorvalueX);
        mAccelerometerSensorvalueY = findViewById(R.id.accelerometerSensorvalueY);
        mAccelerometerSensorvalueZ = findViewById(R.id.accelerometerSensorvalueZ);
        mAccelerometerSensorvalueCount = findViewById(R.id.accelerometerSensorvalueCount);

        mGyroscopeSensortime = findViewById(R.id.gyroscopeSensortime);
        mGyroscopeSensorvalue = findViewById(R.id.gyroscopeSensorvalue);
        mGyroscopeSensorvalueX = findViewById(R.id.gyroscopeSensorvalueX);
        mGyroscopeSensorvalueY = findViewById(R.id.gyroscopeSensorvalueY);
        mGyroscopeSensorvalueZ = findViewById(R.id.gyroscopeSensorvalueZ);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // x,y,z分别存储坐标轴x,y,z上的加速度
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 根据三个方向上的加速度值得到总的加速度值a
            float a = (float) Math.sqrt(x * x + y * y + z * z);
            mAccelerometerSensorvalueCount.setText(String.valueOf(a));
            // 传感器从外界采集数据的时间间隔为10000微秒
            mAccelerometerSensortime.setText(String.valueOf(mAccelerometerSensor.getMinDelay()));
            // 加速度传感器的最大量程
            mAccelerometerSensorvalue.setText(String.valueOf(event.sensor.getMaximumRange()));

            mAccelerometerSensorvalueX.setText(String.valueOf(x));
            mAccelerometerSensorvalueY.setText(String.valueOf(y));
            mAccelerometerSensorvalueZ.setText(String.valueOf(z));

        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // 三个坐标轴方向上的电磁强度，单位是微特拉斯(micro-Tesla)，用uT表示，也可以是高斯(Gauss),1Tesla=10000Gauss
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            // 手机的磁场感应器从外部采集数据的时间间隔是10000微秒
            mMagneticSensortime.setText(String.valueOf(mMagneticSensor.getMinDelay()));
            // 磁场感应器的最大量程
            mMagneticSensorvalue.setText(String.valueOf(event.sensor.getMaximumRange()));

            mMagneticSensorvalueX.setText(String.valueOf(x));
            mMagneticSensorvalueY.setText(String.valueOf(y));
            mMagneticSensorvalueZ.setText(String.valueOf(z));

        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //从 x、y、z 轴的正向位置观看处于原始方位的设备，如果设备逆时针旋转，将会收到正值；否则，为负值
            if(mTimestamp != 0){
                // 得到两次检测到手机旋转的时间差（纳秒），并将其转化为秒
                final float dT = (event.timestamp - mTimestamp) * NS2S;
                // 将手机在各个轴上的旋转角度相加，即可得到当前位置相对于初始位置的旋转弧度
                mAngle[0] += event.values[0] * dT;
                mAngle[1] += event.values[1] * dT;
                mAngle[2] += event.values[2] * dT;
                // 将弧度转化为角度
                float anglex = (float) Math.toDegrees(mAngle[0]);
                float angley = (float) Math.toDegrees(mAngle[1]);
                float anglez = (float) Math.toDegrees(mAngle[2]);

                mGyroscopeSensortime.setText(String.valueOf(mGyroscopeSensor.getMinDelay()));
                mGyroscopeSensorvalue.setText(String.valueOf(event.sensor.getMaximumRange()));
                mGyroscopeSensorvalueX.setText(String.valueOf(anglex));
                mGyroscopeSensorvalueY.setText(String.valueOf(angley));
                mGyroscopeSensorvalueZ.setText(String.valueOf(anglez));
            }
            //将当前时间赋值给timestamp
            mTimestamp = event.timestamp;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}