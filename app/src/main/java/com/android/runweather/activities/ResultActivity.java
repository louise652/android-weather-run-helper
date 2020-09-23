package com.android.runweather.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.runweather.R;
import com.android.runweather.adapters.WeatherAdapter;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.Current;
import com.android.runweather.models.Hourly;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.tasks.ImageIconTask;
import com.android.runweather.tasks.WeatherTask;
import com.android.runweather.utils.CirclePagerIndicatorDecoration;
import com.android.runweather.utils.FormattingUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends Activity implements AsyncResponse {

    public static final int TWENTY_FOUR = 24;
    ImageView currentImg;
    TextView cityText, currentWeatherLabel, currentTemp, currentFeels, sunrise, sunset, clouds, currentDesc, currentWind;
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initComponents();

        //get the city from the main activity and set the text box
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        cityText.setText(city);

        getWeatherResults(intent);


    }

    private void getWeatherResults(Intent intent) {
        //get the coords from the main activity and use to call out to weather api
        Double lat = intent.getDoubleExtra("lat", 0.0);
        Double lng = intent.getDoubleExtra("lng", 0.0);

        //kick off async task
        WeatherTask weatherTask = new WeatherTask();

        weatherTask.execute(lat, lng);
        WeatherVO weatherList = new WeatherVO();

        try {
            //get result from async weather call
            weatherList = weatherTask.get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            setWeatherResultViews(weatherList);

        }
    }

    private void setWeatherResultViews(WeatherVO weatherList) {
        setCurrentWeatherFields(weatherList);

        //Set the hourly weather list of cards (max 24hours results)
        List<Hourly> hourlyList = new ArrayList<>();
        for (int result = 0; result < TWENTY_FOUR; result++) {
            hourlyList.add(weatherList.getHourly().get(result));

        }

        // List<Hourly> hourlyList = weatherList.getHourly();

        WeatherAdapter mainRecyclerAdapter = new WeatherAdapter(this, hourlyList);
        mRecyclerView.setAdapter(mainRecyclerAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }

        });
    }

    private void setCurrentWeatherFields(WeatherVO weatherList) {
        Current currentWeatherVO = weatherList.getCurrent();

        //kick off task to get icon for current weather
        ImageIconTask iconTask = new ImageIconTask();
        iconTask.execute(currentWeatherVO.getWeather().get(0).getIcon());

        try {
            currentImg.setImageDrawable(iconTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentLabel = String.format("Current weather at %s", FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getDt())));
        currentWeatherLabel.setText(currentLabel);

        currentTemp.setText(FormattingUtils.formatTemperature(currentWeatherVO.getTemp()));
        currentFeels.setText(FormattingUtils.formatTemperature(currentWeatherVO.getFeels_like()));
        sunrise.setText(FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getSunrise())));
        sunset.setText(FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getSunset())));
        clouds.setText(String.format("%s%%", currentWeatherVO.getClouds()));
        currentDesc.setText(WordUtils.capitalize(currentWeatherVO.getWeather().get(0).description));
        currentWind.setText(String.format("%sm/s", currentWeatherVO.wind_speed));
    }

    private void initComponents() {

        mRecyclerView = findViewById(R.id.recyclerview_rootview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());

        currentImg = findViewById(R.id.condIcon);
        currentWeatherLabel = findViewById(R.id.currentWeatherLabel);
        cityText = findViewById(R.id.cityText);
        currentTemp = findViewById(R.id.currentTemp);
        currentFeels = findViewById(R.id.currentFeels);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        clouds = findViewById(R.id.clouds);
        currentDesc = findViewById(R.id.currentDesc);
        currentWind = findViewById(R.id.currentWind);
    }

    @Override
    public void processFinish(String output) {

    }
}
