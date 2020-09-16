package com.android.runweather.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.runweather.R;
import com.android.runweather.adapters.WeatherAdapter;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.Weather.Current;
import com.android.runweather.models.Weather.Hourly;
import com.android.runweather.models.Weather.WeatherVO;
import com.android.runweather.tasks.WeatherTask;
import com.android.runweather.utils.FormattingUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends ListActivity implements AsyncResponse {

    ImageView image;
    TextView cityText, currentDate, currentTemp, currentFeels, sunrise, sunset, clouds, currentDesc;
    ListView lvWeather;

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

        //Set the hourly weather list of cards
        List<Hourly> hourlyList = weatherList.getHourly();

        //set the adapter to display each item
        WeatherAdapter adapter = new WeatherAdapter(this, hourlyList);
        setListAdapter(adapter);
        lvWeather = getListView();
    }

    private void setCurrentWeatherFields(WeatherVO weatherList) {
        Current currentWeatherVO = weatherList.getCurrent();
        currentDate.setText(FormattingUtils.formatDateTime(Integer.toString(currentWeatherVO.getDt())));
        currentTemp.setText(FormattingUtils.formatTemperature(currentWeatherVO.getTemp()));
        currentFeels.setText(FormattingUtils.formatTemperature(currentWeatherVO.getFeels_like()));
        sunrise.setText(currentWeatherVO.getSunrise());
        sunset.setText(currentWeatherVO.getSunset());
        clouds.setText(currentWeatherVO.getClouds());
        currentDesc.setText(currentWeatherVO.getWeather().get(0).description);
    }

    private void initComponents() {
        image = findViewById(R.id.condIcon);
        cityText = findViewById(R.id.cityText);
        currentDate = findViewById(R.id.currentDate);
        currentTemp = findViewById(R.id.currentTemp);
        currentFeels = findViewById(R.id.currentFeels);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        clouds = findViewById(R.id.clouds);
        currentDesc = findViewById(R.id.currentDesc);
    }

    @Override
    public void processFinish(String output) {

    }
}
