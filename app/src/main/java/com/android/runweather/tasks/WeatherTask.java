package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.interfaces.AsyncResponse;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.utils.WeatherParser;

import org.json.JSONException;

/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class WeatherTask extends AsyncTask<String, Void, WeatherVO> {

    public AsyncResponse delegate;

    @Override
    protected WeatherVO doInBackground(String... params) {
        WeatherVO weather = new WeatherVO();
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