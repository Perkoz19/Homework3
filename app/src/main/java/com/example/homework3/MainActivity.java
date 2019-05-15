package com.example.homework3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private String[] answers;
    private SensorManager sm;
    private Sensor sens;
    ImageView img;
    TextView answer;
    private float acc1, acc2, acc3;
    private int index = 0;
    private boolean test = false;
    FlingAnimation fling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context con = getApplicationContext();
        answers = con.getResources().getStringArray(R.array.answersarray);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sm.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION) != null){
            sens = sm.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION); //OK
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_LINEAR_ACCELERATION){
            acc1 = event.values[0];
            acc2 = event.values[1];
            acc3 = event.values[2];
        }
        if(Math.abs(acc1) > 8 || Math.abs(acc2) > 8 || Math.abs(acc3) > 8){
            img = findViewById(R.id.ball);
            img.setImageResource(R.drawable.hw3ball_front);
            answer = findViewById(R.id.answer);
            answer.setText("");
            fling = new FlingAnimation(img, DynamicAnimation.X);
            fling.setStartVelocity(-50*acc1).setMinValue(-300).setMaxValue(300).setFriction(0.01f);
            fling.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE);
            fling.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean b, float v, float v1) {
                    img.setTranslationX(0);
                }
            });
            fling.start();
            index = Math.round(Math.abs(acc1+acc2+acc3)*100)%20;
            test = true;
        }
        else if(test){
            img = findViewById(R.id.ball);
            img.setImageResource(R.drawable.hw3ball_empty);
            answer = findViewById(R.id.answer);
            answer.setText(answers[index]);
            test = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(sens != null){
            sm.registerListener(this,sens,100000);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(sens != null){
            sm.unregisterListener(this);
        }
    }
}
