package com.android.runweather.utils;


import com.android.runweather.models.CurrentWeather.CurrentWeatherResultVO;
import com.android.runweather.models.FutureWeather.FutureWeatherResultVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;

public class WeatherParser {
    public static FutureWeatherResultVO getFutureWeather(String data) throws JSONException {
        FutureWeatherResultVO forecastList = new FutureWeatherResultVO();
        ObjectMapper mapper = new ObjectMapper();
        try {
            forecastList = mapper.readValue(data, FutureWeatherResultVO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return forecastList;
    }

    public static CurrentWeatherResultVO getCurrentWeather(String data) throws JSONException {
        CurrentWeatherResultVO currentWeather = new CurrentWeatherResultVO();
        ObjectMapper mapper = new ObjectMapper();

        try {
            currentWeather = mapper.readValue(data, CurrentWeatherResultVO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentWeather;
    }


}
