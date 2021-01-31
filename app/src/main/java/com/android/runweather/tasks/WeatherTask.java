package com.android.runweather.tasks;

import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;
import com.android.runweather.models.WeatherVO;
import com.android.runweather.utils.WeatherParser;

import static com.android.runweather.utils.Constants.*;


/**
 * See https://github.com/survivingwithandroid/Swa-app/blob/master/WeatherApp/src/com/survivingwithandroid/weatherapp/JSONWeatherParser.java
 */
public class WeatherTask extends AsyncTask<Double, Void, WeatherVO> {

    /**
     * Call out to weather api client
     *
     * @param params longitude, latitude
     * @return weather POJO
     */
    @Override
    protected WeatherVO doInBackground(Double... params) {

        String weatherResult = ((new WeatherClient()).getWeather(params[ZERO], params[ONE]));

        //parse returned json String into POJO
        return WeatherParser.getWeather(weatherResult);

    }

}