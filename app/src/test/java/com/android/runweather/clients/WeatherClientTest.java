package com.android.runweather.clients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WeatherClientTest {

    private final WeatherClient weatherClient = new WeatherClient();


    @Test
    public void testWeatherAPIUrl(){
        String expectedPartialURL = "https://api.openweathermap.org/data/2.5/onecall?lat=55.0&lon=5.0&exclude=minutely,daily&APPID=";
        double lat = 55.0;
        double lng = 5.0;
        String actualURL = weatherClient.getURL(lat, lng);

        assert(actualURL.contains(expectedPartialURL));

    }


}
