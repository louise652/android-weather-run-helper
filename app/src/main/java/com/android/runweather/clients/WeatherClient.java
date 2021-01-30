package com.android.runweather.clients;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.runweather.BuildConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP call to the openweatherapi with the city passed in as a param
 */
public class WeatherClient {
    public static final String EXCLUSIONS = "&exclude=minutely,daily";
    private static final String UNITS = "&units=metric";
    private static final String BASE_URL_WEATHER = "https://api.openweathermap.org/data/2.5/onecall";
    private static final String APP_ID = "&APPID=" + BuildConfig.WEATHER_KEY;
    private static final String IMG_URL = "https://openweathermap.org/img/wn/";

    /**
     * Calls out and returns either the current or future weather
     *
     * @param lat latitude
     * @param lng longitude
     * @return endpoint
     */
    public URL getURL(double lat, double lng) {
        try {
            return new URL(BASE_URL_WEATHER + "?lat=" + lat + "&lon=" + lng +
                    EXCLUSIONS + APP_ID + UNITS);
        } catch (MalformedURLException e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * call out to the weather API and get back results in a string
     *
     * @param lat latitude
     * @param lng longitude
     * @return api result
     */
    public String getWeather(double lat, double lng) {
        InputStream inputStream;
        String result = "";
        try {

            //make the connection
            HttpURLConnection connection = (HttpURLConnection) (getURL(lat, lng)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //append result to a string
            StringBuilder sb = new StringBuilder();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append("\r\n");

            inputStream.close();
            connection.disconnect();
            result = sb.toString();
        } catch (Throwable t) {
            //connection error
            Log.e("WeatherClient connection issue", t.getMessage());

        }

        return result;

    }

    /**
     * Returns an image based on the weather conditions
     *
     * @param code (meteorology)
     * @return byte array of image
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