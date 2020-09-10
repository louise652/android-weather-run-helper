package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.List;
import com.android.runweather.models.WeatherResultVO;
import com.android.runweather.models.WeatherUIListVO;
import com.android.runweather.models.WeatherUIVO;
import com.android.runweather.tasks.WeatherTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Activity displays the results of the call to the weather API based on user location
 */
public class ResultActivity extends AppCompatActivity implements AsyncResponse {

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
            WeatherResultVO weather = task.get();
            java.util.List<WeatherUIVO> itemList = new ArrayList();

            for (int i = 0; i < 9; i++) { //only need 3 hour forecasts for next 24 hours (8 entries * 3hrs)

                WeatherUIVO item = new WeatherUIVO();
                List s = weather.getList().get(i);

                item.setTime(s.getDt_txt());
                item.setOverall(s.getWeather().get(0).getMain());
                item.setDetail(s.getWeather().get(0).getDescription());
                item.setIcon(s.getWeather().get(0).getIcon());
                item.setId(s.getWeather().get(0).getId());

                if (s.getRain() != null && !((Double) s.getRain().get_3h()).isNaN()) {
                    item.setRain(s.getRain().get_3h());
                }

                item.setWindDeg((s.getWind().getDeg()));
                item.setWindSpeed(s.getWind().getSpeed());

                item.setClouds(s.getClouds().getAll());

                item.setTemp(s.getMain().getTemp());
                item.setHumidity(s.getMain().getHumidity());
                item.setFeeltemp(s.getMain().getFeels_like());

                sb.append(item.toString() + "\n\n");
                System.out.println("Entry " + i + ": " + item.toString());

                itemList.add(item);
            }

            WeatherUIListVO uiItemList = new WeatherUIListVO();
            uiItemList.setWeatherUIVO(itemList);
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
