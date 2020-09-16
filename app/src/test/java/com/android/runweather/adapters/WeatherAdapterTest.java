package com.android.runweather.adapters;

import android.content.Context;

import com.android.runweather.models.Weather.Hourly;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class WeatherAdapterTest {
    public WeatherAdapter weatherAdapter;
    @Mock
    Context mockContext;

    java.util.List<Hourly> list = new ArrayList<>();

    @Before
    public void setup() {

        weatherAdapter = new WeatherAdapter(mockContext, list);
    }

    @Test
    public void testGetValidDateString() {
        String expectedTime = "16:00";
        String actualTime = weatherAdapter.getDateString("1599836400");
        assertEquals("should display valid time", expectedTime, actualTime);
    }

    @Test
    public void testInvalidDateString() {
        String expectedTime = weatherAdapter.getDateString("Error getting time");
        String actualTime = weatherAdapter.getDateString("");

        assertEquals("should display error", expectedTime, actualTime);
    }
}
