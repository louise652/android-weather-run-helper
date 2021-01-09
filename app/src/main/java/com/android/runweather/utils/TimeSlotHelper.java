package com.android.runweather.utils;

import com.android.runweather.models.Hourly;
import com.android.runweather.models.WeatherVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Utility to display optimal time to go for a run
 * MVP:
 * 1. Should avoid or minimise rain
 * 2. Should aim for close to optimal 'feels like' temp
 * 3. Should minimise wind
 */

public class TimeSlotHelper {

    public static final int NUMBER_OF_RESULTS = 3; //hardcoded for now, in future this will be selectable


    /*
     * Returns the hour with the most optimal conditions
     *
     * @param weatherList
     * @param numResults
     * @return
     */

    public static List<Hourly> getBestTime(WeatherVO weatherList) {

        List<Hourly> hourlyWeatherList = weatherList.getHourly();
        for (int i = 0; i < hourlyWeatherList.size(); i++) {
            setPrecipRank(hourlyWeatherList, i);
        }

        //sort by rank
        Collections.sort(hourlyWeatherList, Comparator.comparingInt(Hourly::getRank));

        return getBestPrecipResults(hourlyWeatherList);

    }

    /*
     * Get the specified number of top results. If there are more results that have the same rank,
     *  also include them for now.
     */
    private static List<Hourly> getBestPrecipResults(List<Hourly> hourlyWeatherList) {

        List<Hourly> result = new ArrayList<>();
        for (int i = 0; i < hourlyWeatherList.size(); i++) {
            Hourly hourlyWeatherItem = hourlyWeatherList.get(i);

            if (i < NUMBER_OF_RESULTS) { //get the top x results regardless of rank
                result.add(hourlyWeatherItem);
            } else if (hourlyWeatherItem.getRank() <= hourlyWeatherList.get(i - 1).getRank()) {
                //we should also include subsequent results if the %precip is the same
                result.add(hourlyWeatherItem);
            } else {
                //we have enough results, no need to continue iterating
                break;
            }
        }
        return result;
    }


    private static void setPrecipRank(List<Hourly> hourlyWeatherList, int i) {
        Hourly hourlyWeather = hourlyWeatherList.get(i);
        int rank = getPrecipRank(hourlyWeather.getPop());
        hourlyWeather.setRank(rank);
    }

    /*
     * Ranks the desirability of a % chance of precipitation
     * @param precip
     * @return
     */
    public static int getPrecipRank(Double precip) {
        int retVal = 0; //optimal
        if (precip <= 0.12) {
            retVal = 0;
        } else if (precip <= 0.24) {
            retVal = 5;
        } else if (precip <= 0.49) {
            retVal = 5;
        } else if (precip <= 0.75) {
            retVal = 8;
        } else {
            retVal = 10; //def don't want
        }


        return retVal;
    }


}

