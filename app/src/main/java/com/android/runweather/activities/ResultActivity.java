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
import com.android.runweather.models.Weather.Hourly;
import com.android.runweather.models.Weather.WeatherVO;
import com.android.runweather.tasks.WeatherTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends ListActivity implements AsyncResponse {

    ImageView image;
    TextView cityText, currentWeather;
    ListView lvWeather;

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
        Double lat = intent.getDoubleExtra("lat", 0.0);
        Double lng = intent.getDoubleExtra("lng", 0.0);


        cityText.setText(city);
        WeatherTask forecastTask = new WeatherTask();
        forecastTask.execute(lat, lng);
        StringBuilder sb = new StringBuilder();
        WeatherVO forecastList = null;

        try {
            forecastList = forecastTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            currentWeather.setText(forecastList.getCurrent().toString());

            List<Hourly> s = forecastList.getHourly();

            WeatherAdapter adapter = new WeatherAdapter(this, s);
            setListAdapter(adapter);
            lvWeather = getListView();

        }


    }

    @Override
    public void processFinish(String output) {

    }
}
