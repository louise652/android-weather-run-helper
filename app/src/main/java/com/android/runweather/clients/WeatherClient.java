package com.android.runweather.clients;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.runweather.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HTTP call to the openweatherapi with the city passed in as a param
 */
public class WeatherClient {
    public static final String EXCLUSIONS = "&exclude=minutely,daily";
    private static final String UNITS = "&units=metric";
    private static final String BASE_URL_WEATHER = "https://api.openweathermap.org/data/2.5/onecall";
    private static final String APP_ID = "&APPID=" + BuildConfig.WEATHER_KEY;
    private static final String IMG_URL = "https://openweathermap.org/img/wn/";

    private final OkHttpClient client = new OkHttpClient();

    /*
    * get response from OpenWeatherAPI
     */
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /*
     * OpenweatherAPI url
     */
    public String getURL(double lat, double lng) {

        return (BASE_URL_WEATHER + "?lat=" + lat + "&lon=" + lng +
                EXCLUSIONS + APP_ID + UNITS);

    }

    /*
     * call out to the weather API and get back results in a string
     */
    public String getWeather(double lat, double lng) {
        String result = "";
        try {
            result = run(getURL(lat, lng));


        } catch (Throwable t) {
            //connection error
            Log.e("WeatherClient connection issue", t.getMessage());

        }

        return result;

    }

    /*
     * Returns an image based on the weather conditions
     */
    public Drawable getImage(String code) {
        try (InputStream stream = (InputStream) new URL(IMG_URL + code + ".png").getContent()) {

            return Drawable.createFromStream(stream, "src");

        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;

    }
}