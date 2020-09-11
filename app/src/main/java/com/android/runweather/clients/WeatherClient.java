package com.android.runweather.clients;

import com.android.runweather.BuildConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP call to the openweatherapi with the city passed in as a param
 */
public class WeatherClient {
    private static final String UNITS = "&units=metric";
    private static final String BASE_URL_WEATHER = "https://api.openweathermap.org/data/2.5/";
    private static final String APP_ID = "&APPID=" + BuildConfig.WEATHER_KEY;
    private static final String IMG_URL = "https://openweathermap.org/img/w/";

    /**
     * Calls out and returns either the current or future weather
     *
     * @param endpoint weather for current or forecast for future
     * @return endpoint
     */
    public URL getURL(double lat, double lng, String endpoint) {
        try {
            return new URL(BASE_URL_WEATHER + endpoint + "?lat=" + lat + "&lon=" + lng +
                    "&exclude=minutely,daily" + APP_ID + UNITS);
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
    public String getWeather(double lat, double lng, String endpoint) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {

            //make the connection
            connection = (HttpURLConnection) (getURL(lat, lng, endpoint)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

//append result to a string
            StringBuilder sb = new StringBuilder();
            inputStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while (  (line = br.readLine()) != null )
                sb.append(line + "\r\n");

            inputStream.close();
            connection.disconnect();
            return sb.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

    /**
     * Returns an image based on the weather conditions
     * @param code (meteorology)
     * @return byte array of image
     */
    public byte[] getImage(String code) {
        HttpURLConnection connection = null ;
        InputStream stream = null;
        try {
            connection = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            // Let's read the response
            stream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( stream.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { stream.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
}