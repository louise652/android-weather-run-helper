package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.models.FutureWeather.FutureWeatherResultVO;
import com.android.runweather.utils.WeatherParser;

import org.json.JSONException;

/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class FutureWeatherTask extends AsyncTask<String, Void, FutureWeatherResultVO> {


    @Override
    protected FutureWeatherResultVO doInBackground(String... params) {
        FutureWeatherResultVO futureWeatherObject = new FutureWeatherResultVO();

        String futureWeatherResult = ((new WeatherClient()).getWeather(params[0], "forecast"));
        System.out.println("Future weather Result: " + futureWeatherResult);

        try {

            futureWeatherObject = WeatherParser.getFutureWeather(futureWeatherResult);


            // TODO: get appropiate weather icon
            //  weather.iconData = ((new WeatherClient()).getImage(weather.getIcon()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return futureWeatherObject;

    }


}