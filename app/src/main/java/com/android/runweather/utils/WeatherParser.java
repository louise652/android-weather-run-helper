package com.android.runweather.utils;


import com.android.runweather.models.WeatherVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Class uses Jackson to process the api result into a POJO
 */
public class WeatherParser {
    public static WeatherVO getWeather(String data) {
        WeatherVO forecasts = new WeatherVO();
        ObjectMapper mapper = new ObjectMapper();

        try {
            forecasts = mapper.readValue(data, WeatherVO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return forecasts;
    }


}
