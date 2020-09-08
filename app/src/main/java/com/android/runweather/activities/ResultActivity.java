package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.tasks.WeatherTask;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends AppCompatActivity {
    ImageView image;
    TextView cityText, temp, weatherDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //inflate the UI components
        image = findViewById(R.id.condIcon);
        cityText = findViewById(R.id.cityText);
        temp = findViewById(R.id.temp);
        weatherDetails = findViewById(R.id.weatherDetails);

        //get the city from the main activity and set the text box
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");

        cityText.setText(city);
        WeatherTask task = new WeatherTask();
        task.execute(new String[]{city});

    }


}
