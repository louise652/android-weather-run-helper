package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.List;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.tasks.WeatherTask;

import java.util.concurrent.ExecutionException;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends AppCompatActivity implements AsyncResponse {
    public String time;
    public String overall;
    public String detail;
    public String icon;
    public int id;
    public double rain;
    public int windDeg;
    public double windSpeed;
    public int clouds;
    public double temp;
    public int humidity;
    public double feeltemp;
    ImageView image;
    TextView cityText, weatherDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //inflate the UI components
        image = findViewById(R.id.condIcon);
        cityText = findViewById(R.id.cityText);
        weatherDetails = findViewById(R.id.weatherDetails);

        //get the city from the main activity and set the text box
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");

        cityText.setText(city);
        WeatherTask task = new WeatherTask();
        task.execute(city);
        StringBuilder sb = new StringBuilder();
        try {
            String st;
            WeatherVO weather = task.get();

            for (int i = 0; i < 9; i++) { //only need 3 hour forecasts for next 24 hours (8 entries * 3hrs)
                List s = weather.getList().get(i);

                time = (s.getDt_txt());
                overall = (s.getWeather().get(0).getMain());
                detail = (s.getWeather().get(0).getDescription());
                icon = (s.getWeather().get(0).getIcon());
                id = (s.getWeather().get(0).getId());

                if (s.getRain() != null && !((Double) s.getRain().get_3h()).isNaN()) {
                    rain = (s.getRain().get_3h());
                }

                windDeg = ((s.getWind().getDeg()));
                windSpeed = (s.getWind().getSpeed());

                clouds = (s.getClouds().getAll());

                temp = (s.getMain().getTemp());
                humidity = (s.getMain().getHumidity());
                feeltemp = (s.getMain().getFeels_like());

                sb.append("\nTIME " + time + " Overall: " + overall + " DEtail: " + detail + " ICON: " + icon + " id: " + id + " rain: " + rain + " winddeg: " + windDeg
                        + " windspeed: " + windSpeed + " clouds: " + clouds + " temp: " + temp + " humiditiy: " + humidity + " feeltemp: " + feeltemp);

                System.out.println("Entry " + i + ": " + sb);
            }

            weatherDetails.setText(sb);
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
