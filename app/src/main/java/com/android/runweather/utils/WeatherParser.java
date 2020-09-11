package com.android.runweather.utils;


import com.android.runweather.models.Weather.WeatherVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;

import java.io.IOException;

public class WeatherParser {
    public static WeatherVO getWeather(String data) throws JSONException {
        WeatherVO forecasts = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            forecasts = mapper.readValue(data, WeatherVO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return forecasts;
    }


}
