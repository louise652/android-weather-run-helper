package com.android.runweather.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.android.runweather.utils.FormattingUtils.getDate;
import static com.android.runweather.utils.FormattingUtils.getHourOfDayFromTime;
import static com.android.runweather.utils.FormattingUtils.sdf;
import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FormattingUtilsTest {

    @Test
    public void testGetValidDateString() {
        String expectedTime = "4:00PM";
        String actualTime = sdf.format(getDate(1599836400));
        assertEquals("should display valid time", expectedTime, actualTime);
    }

    @Test
    public void testTempString() {
        String expectedTemp = "22.2" + "\u2103";
        String actual = FormattingUtils.formatTemperature(22.2345);

        assertEquals("should format temp", expectedTemp, actual);
    }

    @Test
    public void testGetHourFromDate(){
        int expectedHour = 16;
        int actualHour = getHourOfDayFromTime(getDate(1599836400));
        assertEquals("should display correct hour", expectedHour, actualHour);
    }
}
