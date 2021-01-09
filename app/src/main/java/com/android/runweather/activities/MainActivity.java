package com.android.runweather.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.text.WordUtils.capitalize;

/**
 * Main activity which handles eliciting location service permission from the user and
 * displaying current/future weather conditions
 */

public class MainActivity extends Activity {

    String city;
    LatLng coords;
    public static final int TWELVE = 12;
    ImageView currentImg;
    TextView cityText, currentWeatherLabel, currentTemp, currentFeels, sunrise, sunset, clouds, currentDesc, currentWind;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initComponents();
        LocationUtil instance = LocationUtil.getInstance(this);
        instance.checkLocationPermission();
        coords = instance.getUserLocationResult();
        if (coords != null) {
            //we have successfully got our coords from locations service
            city = LocationUtil.getInstance(this).getLocFromCoords(coords.latitude, coords.longitude);
            Toast.makeText(this, "Getting weather results for " + city, Toast.LENGTH_SHORT).show();
            getWeatherResults();
        }else{
            Toast.makeText(this, "Could not get your location. Ensure location permission is granted or try later", Toast.LENGTH_LONG).show();
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

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            setCurrentWeatherFields(weatherList);
            setFutureResultViews(weatherList.getHourly());
            getSuggestedTimeSlot(weatherList);

        }
    }



    private void setFutureResultViews(List<Hourly> hourlyWeatherList) {


        //Set the hourly weather list of cards (max 24hours results)
        List<Hourly> hourlyList = new ArrayList<>();
        for (int result = 0; result < TWELVE; result++) {
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
        } finally {


            cityText.setText(city);
            String currentLabel = String.format("Current weather at %s", FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getDt())));
            currentWeatherLabel.setText(currentLabel);

            currentTemp.setText(FormattingUtils.formatTemperature(currentWeatherVO.getTemp()));
            currentFeels.setText(FormattingUtils.formatTemperature(currentWeatherVO.getFeels_like()));
            sunrise.setText(FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getSunrise())));
            sunset.setText(FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getSunset())));
            clouds.setText(String.format("%s%%", currentWeatherVO.getClouds()));
            currentDesc.setText(capitalize(currentWeatherVO.getWeather().get(0).description));
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