package com.android.runweather.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FormattingUtils {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("h:mma", Locale.getDefault());

    /*
     * Find the hour of day from a given Date between 00 and 23
     */
    public static int getHourOfDayFromTime(Date date) {
        int hourOfDay;
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        return hourOfDay;
    }

    /*
     * Gets date from millis since epoch
     * @param strDate time since epoch
     * @return
     */
    public static Date getDate(int intDt) {
        return new Date(intDt  * 1000L);
    }

    /*
     * Formats the double temperature fields to 1 decimal place and adds the degrees symbol
     *
     * @param temp temp to formats
     * @return formatted temp
     */
    public static String formatTemperature(double temp) {

        DecimalFormat df = new DecimalFormat("#.#");
        df.format(temp);
        return df.format(temp) + "\u2103";


    }
}
