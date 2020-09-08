package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.utils.WeatherParser;

import org.json.JSONException;

public class WeatherTask extends AsyncTask<String, Void, WeatherVO> {

    @Override
    protected WeatherVO doInBackground(String... params) {
        WeatherVO weather = new WeatherVO();
        String data = ((new WeatherClient()).getWeather(params[0]));

        try {
            weather = WeatherParser.getWeather(data);

            // Let's retrieve the icon
            weather.iconData = ((new WeatherClient()).getImage(weather.currentCondition.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;

    }
}