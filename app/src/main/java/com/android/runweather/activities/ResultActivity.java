package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.CurrentWeather.CurrentWeatherResultVO;
import com.android.runweather.models.FutureWeather.FutureWeatherResultVO;
import com.android.runweather.tasks.CurrentWeatherTask;
import com.android.runweather.tasks.FutureWeatherTask;

import java.util.concurrent.ExecutionException;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends AppCompatActivity implements AsyncResponse {

    ImageView image;
    TextView cityText, currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //inflate the UI components
        image = findViewById(R.id.condIcon);
        cityText = findViewById(R.id.cityText);
        currentWeather = findViewById(R.id.currentDetails);

        //get the city from the main activity and set the text box
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");

        cityText.setText(city);
        FutureWeatherTask forecastTask = new FutureWeatherTask();
        CurrentWeatherTask currentTask = new CurrentWeatherTask();
        currentTask.execute(city);
        forecastTask.execute(city);
        StringBuilder sb = new StringBuilder();

        try {
            FutureWeatherResultVO forecastList = forecastTask.get();
            CurrentWeatherResultVO currentWeather = currentTask.get();

            System.out.println("Current act: " + currentWeather.toString());
            System.out.println("Forecast act: " + forecastList.toString());
            this.currentWeather.setText(currentWeather.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void processFinish(String output) {

    }
}
