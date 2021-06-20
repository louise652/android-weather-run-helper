package com.android.runweather.utils;

import android.content.SharedPreferences;

import com.android.runweather.models.Hourly;
import com.android.runweather.models.WeatherVO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.android.runweather.utils.Constants.PREF_A;
import static com.android.runweather.utils.Constants.RAIN;
import static com.android.runweather.utils.Constants.SUNLIGHT;
import static com.android.runweather.utils.Constants.TEMP;
import static com.android.runweather.utils.Constants.WIND;
import static java.time.ZoneOffset.UTC;


/**
 * Utility to display optimal time to go for a run
 * MVP:
 * 1. Should be daylight
 * 1. Should avoid or minimise rain
 * 2. Should aim for close to optimal 'feels like' temp
 * 3. Should minimise wind
 */

public class TimeSlotHelper {

    public static final int NUMBER_OF_RESULTS = 3; //hardcoded for now, in future this will be selectable

    /*
     * Returns the hours sorted by the most optimal conditions
     *
     * @param weatherList
     * @param numResults
     * @return
     */

    public static List<Hourly> getBestTime(WeatherVO weatherList, SharedPreferences weatherPrefs) {
        List<Hourly> hourlyWeatherList = weatherList.getHourly();
        int sunrise = weatherList.getCurrent().getSunrise();
        int sunset = weatherList.getCurrent().getSunset();

        for (Hourly item : hourlyWeatherList) {
            int tempRank = getTempRank(item.getFeels_like());
            item.setTempRank(tempRank);
            setIsDaylight(item, sunrise, sunset);
        }


        //TODO: user prefs are not ordering correctly

        Comparator<Hourly> comparator = orderByPreference(PREF_A); //initialise the comparator with the top user pref for weather conditions
        Map<String, ?> keys = weatherPrefs.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {

            if (!entry.getKey().equals(PREF_A)) { //loop through secondary prefs and order

                orderBySecondaryPreferences((String) entry.getValue(), comparator);

            }

        }

        hourlyWeatherList.sort(comparator);

        return getBestResults(hourlyWeatherList);

    }

    private static Comparator orderByPreference(String pref) {

        Comparator<Hourly> comparator = Comparator.comparing(hourly -> !hourly.isDaylight());
        switch (pref) {
            case SUNLIGHT:
                comparator = Comparator.comparing(hourly -> !hourly.isDaylight());
                break;
            case RAIN:
                comparator = Comparator.comparing(hourly -> hourly.getPop());
                break;
            case TEMP:
                comparator = Comparator.comparing(hourly -> hourly.getTempRank());
            case WIND:
                comparator = Comparator.comparing(hourly -> hourly.getWind_speed());
        }
        return comparator;
    }

    private static Comparator orderBySecondaryPreferences(String pref, Comparator<Hourly> comparator) {

        switch (pref) {
            case SUNLIGHT:
                comparator = comparator.thenComparing(hourly -> !hourly.isDaylight());
                break;
            case RAIN:
                comparator = comparator.thenComparing(Hourly::getPop);
                break;
            case TEMP:
                comparator = comparator.thenComparing(Hourly::getTempRank);
            case WIND:
                comparator = comparator.thenComparing(Hourly::getWind_speed);
        }
        return comparator;
    }

    /*
     * Sets a flag if the hour is between sunrise and sunset
     */

    private static void setIsDaylight(Hourly item, int sunrise, int sunset) {


        Instant currentTimeInst = Instant.ofEpochSecond(item.getDt());
        Instant sunriseTime = Instant.ofEpochSecond(sunrise);
        Instant sunsetTime = Instant.ofEpochSecond(sunset);

        LocalDateTime currentTime = LocalDateTime.ofInstant(currentTimeInst, UTC);
        boolean isDaylight = (
                currentTime.isAfter(LocalDateTime.ofInstant(sunriseTime, UTC))
                        &&
                        currentTime.isBefore(LocalDateTime.ofInstant(sunsetTime, UTC))
        );

        item.setDaylight(isDaylight);
    }


    /*
     * Get the specified number of top results. If there are more results that have the same conditions,
     *  also include them for now.
     */
    private static List<Hourly> getBestResults(List<Hourly> hourlyWeatherList) {

        List<Hourly> result = new ArrayList<>();
        for (int i = 0; i < hourlyWeatherList.size(); i++) {
            Hourly hourlyWeatherItem = hourlyWeatherList.get(i);

            if (i < NUMBER_OF_RESULTS) { //get the top x results regardless of rank
                result.add(hourlyWeatherItem);
            } else if (hourlyWeatherItem.isDaylight() && hourlyWeatherItem.getPop() <= hourlyWeatherList.get(i - 1).getPop()) {
                //we should also include subsequent results if the %pop is the same
                result.add(hourlyWeatherItem);
            } else {
                //we have enough results, no need to continue iterating
                break;
            }
        }
        return result;
    }


    /*
     * Ranks the desirability of a 'feels like' temp
     * @param temp
     * @return
     */
    public static int getTempRank(Double temp) {

        int retVal; //optimal run temp
        if (12 <= temp && temp <= 15) {
            retVal = 0;
        } else if (7 <= temp && temp <= 11.9) { // this is alright
            retVal = 3;
        } else {
            retVal = 5; //too hot/cold
        }

        return retVal;
    }

}

