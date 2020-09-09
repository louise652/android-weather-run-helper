package com.android.runweather.utils;


import com.android.runweather.models.WeatherVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WeatherParser {
    public static WeatherVO getWeather(String data) throws JSONException {
        WeatherVO weather = new WeatherVO();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info

        ObjectMapper mapper = new ObjectMapper();

        try {
            weather = mapper.readValue(data, WeatherVO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return weather;
    }


}
