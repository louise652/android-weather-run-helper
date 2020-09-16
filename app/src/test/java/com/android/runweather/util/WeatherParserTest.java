package com.android.runweather.util;

import com.android.runweather.models.Weather.WeatherVO;
import com.android.runweather.utils.WeatherParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@RunWith(JUnit4.class)
public class WeatherParserTest {
    String data;

    @Before
    public void setup() throws IOException {
        File file = new File("src/test/java/resources/exampleWeatherResponse.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        data = stringBuilder.toString();


    }

    /**
     * Test a selection of attributes to ensure that response parses correctly
     */

    @Test

    public void testValidWeatherData() {

        WeatherVO actualVO = WeatherParser.getWeather(data);

        assert (actualVO.getLat() == 54.63);
        assert (actualVO.getLon() == -5.92);
        assert (actualVO.getCurrent()).getWeather().get(0).description.equals("light rain");
        assert (actualVO.getHourly()).get(0).getPop() == 0.66;

    }

}
