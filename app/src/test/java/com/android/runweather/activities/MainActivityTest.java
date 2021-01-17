package com.android.runweather.activities;

import com.android.runweather.models.WeatherVO;
import com.android.runweather.utils.WeatherParser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.android.runweather.utils.FormattingUtils.getDate;
import static com.android.runweather.utils.FormattingUtils.getHourOfDayFromTime;
import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class MainActivityTest {

    WeatherVO weatherResult;

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
        String data = stringBuilder.toString();

        weatherResult = WeatherParser.getWeather(data);
    }

    @Test
    public void testTodayDaylightHour() {

        int expectedSunriseHr = 6;
        int actualSunriseHr = getHourOfDayFromTime(getDate(weatherResult.getCurrent().getSunrise()));

        int expectedSunsetHr = 19;
        int actualSunsetHr = getHourOfDayFromTime(getDate(weatherResult.getCurrent().getSunset()));

        assertEquals("should show hour of sunrise", expectedSunriseHr, actualSunriseHr);
        assertEquals("should show hour of sunset", expectedSunsetHr, actualSunsetHr);
    }


    @Test

    public void testTomorrowDaylightHour() {


    }


}
