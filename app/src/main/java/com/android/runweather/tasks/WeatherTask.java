package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.utils.WeatherParser;


/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class WeatherTask extends AsyncTask<Double, Void, WeatherVO> {

    /**
     * Call out to api vi client
     *
     * @param params longitude, latitude
     * @return weather POJO
     */
    @Override
    protected WeatherVO doInBackground(Double... params) {

        String weatherResult = ((new WeatherClient()).getWeather(params[0], params[1]));

        //parse returned json String into POJO
        return WeatherParser.getWeather(weatherResult);

    }

}