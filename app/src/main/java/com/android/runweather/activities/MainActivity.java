package com.android.runweather.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.utils.LocationUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void getWeather(View view) {
        String s = LocationUtil.getInstance(this).checkLocationPermission();

        if (s.isEmpty()) {
            //manually elicit location
        }

    }
}