package com.android.runweather.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
}
