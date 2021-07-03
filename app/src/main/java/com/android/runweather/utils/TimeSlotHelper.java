package com.android.runweather.utils;

import android.content.SharedPreferences;

import com.android.runweather.models.Hourly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.android.runweather.utils.Constants.PREF_A;
import static com.android.runweather.utils.Constants.PREF_B;
import static com.android.runweather.utils.Constants.PREF_C;
import static com.android.runweather.utils.Constants.PREF_D;
import static com.android.runweather.utils.Constants.RAIN;
import static com.android.runweather.utils.Constants.SUNLIGHT;
import static com.android.runweather.utils.Constants.TEMP;
import static com.android.runweather.utils.Constants.WIND;
import static com.android.runweather.utils.Constants.ZERO;


/**
 * Utility to display optimal time to go for a run
 * MVP:
 * 1. Should be daylight
 * 1. Should avoid or minimise rain
 * 2. Should aim for close to optimal 'feels like' temp
 * 3. Should minimise wind
 */

public class TimeSlotHelper {
    private static final int THREE = 3;
    private static final int FIVE = 5;

    /*
     * Returns the hours sorted by the most optimal conditions
     *
     * @param weatherList
     * @param numResults
     * @return
     */

    public static List<Hourly> getBestTime(List<Hourly> hourlyWeatherList, SharedPreferences weatherPrefs, int sunrise, int sunset) {

        for (Hourly item : hourlyWeatherList) {
            int tempRank = getTempRank(item.getFeels_like());
            item.setTempRank(tempRank);
        }
        Comparator<Hourly> comparator = getHourlyComparator(weatherPrefs);
        hourlyWeatherList.sort(comparator);
        return hourlyWeatherList;
    }

    /*
     * Returns a comparator which will be used to order/sort the results based on user prefs
     */
    private static Comparator<Hourly> getHourlyComparator(SharedPreferences weatherPrefs) {
        //initialise the comparator with the top user pref for weather conditions
        Comparator<Hourly> comparator = orderByPreference();
        Map<String, ?> prefsKeys = weatherPrefs.getAll();

        //loop through each non primary pref and add the comparator to order appropriately
        ArrayList<String> prefsString = new ArrayList<>(Arrays.asList(PREF_B, PREF_C, PREF_D));

        for (String pref : prefsString) {
            for (Map.Entry<String, ?> entry : prefsKeys.entrySet()) {
                if (entry.getKey().equals(pref)) { //loop through secondary prefs and order
                    comparator = orderBySecondaryPreferences((String) entry.getValue(), comparator);
                }
            }
        }
        return comparator;
    }

    /*
     * First order by PREF_A
     */
    private static Comparator<Hourly> orderByPreference() {

        Comparator<Hourly> comparator = Comparator.comparing(hourly -> !hourly.isDaylight());
        switch (PREF_A) {
            case SUNLIGHT:
                comparator = Comparator.comparing(hourly -> !hourly.isDaylight());
                break;
            case RAIN:
                comparator = Comparator.comparing(Hourly::getPop);
                break;
            case TEMP:
                comparator = Comparator.comparing(Hourly::getTempRank);
                break;
            case WIND:
                comparator = Comparator.comparing(Hourly::getWind_speed);
                break;
        }
        return comparator;
    }


    /*
     * Order by other non primary prefs
     */
    private static Comparator<Hourly> orderBySecondaryPreferences(String pref, Comparator<Hourly> comparator) {

        switch (pref) {
            case SUNLIGHT:
                comparator = comparator.thenComparing(hourly -> !hourly.isDaylight());
                break;
            case RAIN:
                comparator = comparator.thenComparing(Hourly::getPop);
                break;
            case TEMP:
                comparator = comparator.thenComparing(Hourly::getTempRank);
                break;
            case WIND:
                comparator = comparator.thenComparing(Hourly::getWind_speed);
                break;
        }
        return comparator;
    }



    /*
     * Ranks the desirability of a 'feels like' temp
     * @param temp
     * @return
     */
    public static int getTempRank(Double temp) {

        double lowerIdealTemp = 12;
        double upperIdealTemp = 15;

        double lowerOKTemp = 7;
        double upperOKTemp = 11.9;


        int retVal; //optimal run temp
        if (lowerIdealTemp <= temp && temp <= upperIdealTemp) {
            retVal = ZERO;
        } else if (lowerOKTemp <= temp && temp <= upperOKTemp) { // this is alright
            retVal = THREE;
        } else {
            retVal = FIVE; //too hot/cold
        }
        return retVal;
    }

}

