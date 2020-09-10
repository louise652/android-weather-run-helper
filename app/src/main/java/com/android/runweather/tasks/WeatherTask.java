package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.WeatherResultVO;
import com.android.runweather.utils.WeatherParser;

import org.json.JSONException;

/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class WeatherTask extends AsyncTask<String, Void, WeatherResultVO> {

    public AsyncResponse delegate;

    @Override
    protected WeatherResultVO doInBackground(String... params) {
        WeatherResultVO weather = new WeatherResultVO();
        String data = ((new WeatherClient()).getWeather(params[0]));
        System.out.println("Result: " + data);
        try {
            weather = WeatherParser.getWeather(data);

            // TODO: get appropiate weather icon
            //  weather.iconData = ((new WeatherClient()).getImage(weather.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;

    }


}