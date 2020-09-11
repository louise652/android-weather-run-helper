package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.models.CurrentWeather.CurrentWeatherResultVO;
import com.android.runweather.utils.WeatherParser;

import org.json.JSONException;

/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class CurrentWeatherTask extends AsyncTask<String, Void, CurrentWeatherResultVO> {


    @Override
    protected CurrentWeatherResultVO doInBackground(String... params) {
        CurrentWeatherResultVO currentWeatherObject = new CurrentWeatherResultVO();

        String currentWeatherResult = ((new WeatherClient()).getWeather(params[0], "weather"));
        System.out.println("Current weather Result: " + currentWeatherResult);


        try {

            currentWeatherObject = WeatherParser.getCurrentWeather(currentWeatherResult);


            // TODO: get appropiate weather icon
            //  weather.iconData = ((new WeatherClient()).getImage(weather.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return currentWeatherObject;

    }


}