package com.android.runweather.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormattingUtils {

    /**
     * Format the timestamp and get the time
     *
     * @param strDate time since epoch
     * @return time
     */
    public static String formatDateTime(String strDate) {
        try {
            long longDate = Long.parseLong(strDate) * 1000;
            Date date = new Date();
            date.setTime(longDate);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
            return sdf.format(date);
        } catch (NumberFormatException ex) {
            return "Error getting time";

        }
    }

    /**
     * Formats the double temperature feilds to 1 decimal place and adds the degrees symbol
     *
     * @param temp temp to formats
     * @return formatted temp
     */
    public static String formatTemperature(double temp) {

        DecimalFormat df = new DecimalFormat("#.#");
        df.format(temp);
        String tempStr = df.format(temp) + "\u2103";
        return tempStr;


    }
}
