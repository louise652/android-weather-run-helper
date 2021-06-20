package com.android.runweather.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.runweather.R;
import com.android.runweather.adapters.WeatherAdapter;
import com.android.runweather.models.Current;
import com.android.runweather.models.Hourly;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.tasks.ImageIconTask;
import com.android.runweather.tasks.WeatherTask;
import com.android.runweather.utils.FormattingUtils;
import com.android.runweather.utils.LinePagerIndicatorDecoration;
import com.android.runweather.utils.LocationUtil;
import com.android.runweather.utils.TimeSlotHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.android.runweather.utils.Constants.*;
import static com.android.runweather.utils.FormattingUtils.getDate;
import static com.android.runweather.utils.FormattingUtils.getHourOfDayFromTime;
import static com.android.runweather.utils.FormattingUtils.sdf;
import static org.apache.commons.lang3.text.WordUtils.capitalize;

/**
 * Main activity which handles eliciting location service permission from the user and
 * displaying current/future weather conditions
 */

public class MainActivity extends AppCompatActivity {

    String city;
    LatLng coords;
    ImageView currentImg;
    TextView cityText, currentWeatherLabel, currentTemp, currentFeels, sunrise, sunset, clouds, currentDesc, currentWind;
    RecyclerView mRecyclerView;
    SharedPreferences timePrefs, weatherPrefs;
    int sunriseHr, sunsetHr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        initComponents();
        timePrefs = getSharedPreferences(TIME_PREFERENCES, Context.MODE_PRIVATE);
        weatherPrefs = getSharedPreferences(WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        LocationUtil instance = LocationUtil.getInstance(this);
        instance.checkLocationPermission();
        coords = instance.getUserLocationResult();
        if (coords != null) {
            //we have successfully got our coords from locations service
            city = LocationUtil.getInstance(this).getLocFromCoords(coords.latitude, coords.longitude);
            Toast.makeText(this, WEATHER_RESULT_TXT + city, Toast.LENGTH_SHORT).show();
            getWeatherResults();
        } else {
            Toast.makeText(this, LOCATION_ERROR_TXT, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                settingsIntent.putExtra(SUNRISE, sunriseHr);
                settingsIntent.putExtra(SUNSET, sunsetHr);
                startActivity(settingsIntent);
                return true;

            case R.id.menu_help:
                Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(helpIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void initComponents() {

        mRecyclerView = findViewById(R.id.recyclerview_rootview);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // add pager behavior
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

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

    private void getWeatherResults() {
        //get the coords from the main activity and use to call out to weather api
        //kick off async task
        WeatherTask weatherTask = new WeatherTask();

        weatherTask.execute(coords.latitude, coords.longitude);
        WeatherVO weatherList = new WeatherVO();

        try {
            //get result from async weather call
            weatherList = weatherTask.get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            setCurrentWeatherFields(weatherList);
            setFutureResultViews(weatherList);


        }
    }


    private void setFutureResultViews(WeatherVO weatherList) {
        int startTime = timePrefs.getInt(START_TIME_INDEX, ZERO);
        int endTime = timePrefs.getInt(END_TIME_INDEX, TWELVE);

        List<Hourly> hourlyWeatherList = weatherList.getHourly();
        //Set the hourly weather list of cards (default 24hours results)
        List<Hourly> hourlyList = new ArrayList<>();
        for (int result = startTime; result < endTime; result++) {
            hourlyList.add(hourlyWeatherList.get(result));

        }

        WeatherAdapter mainRecyclerAdapter = new WeatherAdapter(this, hourlyList);
        mRecyclerView.setAdapter(mainRecyclerAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            }

        });
        getSuggestedTimeSlot(weatherList);
    }

    private void setCurrentWeatherFields(WeatherVO weatherList) {
        Current currentWeatherVO = weatherList.getCurrent();

        //kick off task to get icon for current weather
        ImageIconTask iconTask = new ImageIconTask();
        iconTask.execute(currentWeatherVO.getWeather().get(ZERO).getIcon());

        try {
            currentImg.setImageDrawable(iconTask.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {

            cityText.setText(city);
            String currentLabel = String.format("Current weather at %s", sdf.format(getDate(currentWeatherVO.getDt())));
            currentWeatherLabel.setText(currentLabel);

            currentTemp.setText(FormattingUtils.formatTemperature(currentWeatherVO.getTemp()));
            currentFeels.setText(FormattingUtils.formatTemperature(currentWeatherVO.getFeels_like()));

            Date sunriseTime = getDate(currentWeatherVO.getSunrise());
            Date sunsetTime = getDate(currentWeatherVO.getSunset());

            sunrise.setText(sdf.format(sunriseTime));
            sunset.setText(sdf.format(sunsetTime));

            //used so that we can set user preferences to show results between sunrise and sunset
            sunriseHr = getHourOfDayFromTime(sunriseTime);
            sunsetHr = getHourOfDayFromTime(sunsetTime);

            clouds.setText(String.format("%s%%", currentWeatherVO.getClouds()));
            currentDesc.setText(capitalize(currentWeatherVO.getWeather().get(ZERO).description));
            currentWind.setText(String.format("%sm/s", currentWeatherVO.wind_speed));

            // pager indicator
            mRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        }
    }


    private void getSuggestedTimeSlot(WeatherVO weatherList) {
        List<Hourly> s = TimeSlotHelper.getBestTime(weatherList);
        s.forEach(x -> System.out.println(x.toString()));
        //do something with this result to display as a suggested timeslot
    }
}