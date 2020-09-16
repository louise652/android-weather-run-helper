package com.android.runweather.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FormattingUtilsTest {

    @Test
    public void testGetValidDateString() {
        String expectedTime = "4:00PM";
        String actualTime = FormattingUtils.formatDateTime("1599836400");
        assertEquals("should display valid time", expectedTime, actualTime);
    }

    @Test
    public void testInvalidDateString() {
        String expectedTime = "Error getting time";
        String actualTime = FormattingUtils.formatDateTime("");

        assertEquals("should display error", expectedTime, actualTime);
    }

    @Test
    public void testTempString() {
        String expectedTemp = "22.2" + "\u2103";
        String actual = FormattingUtils.formatTemperature(22.2345);

        assertEquals("should format temp", expectedTemp, actual);
    }
}
