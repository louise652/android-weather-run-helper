package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.models.Weather.WeatherVO;
import com.android.runweather.utils.WeatherParser;

import org.json.JSONException;

/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class WeatherTask extends AsyncTask<Double, Void, WeatherVO> {


    @Override
    protected WeatherVO doInBackground(Double... params) {
        WeatherVO weatherObj = null;

        String weatherResult = ((new WeatherClient()).getWeather(params[0], params[1], "onecall"));

        try {

            weatherObj = WeatherParser.getWeather(weatherResult);


            // TODO: get appropiate weather icon
            //  weather.iconData = ((new WeatherClient()).getImage(weather.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherObj;

    }


}