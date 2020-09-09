package com.android.runweather.clients;

import com.android.runweather.BuildConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HTTP call to the openweatherapi with the city passed in as a param
 */
public class WeatherClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private static String APP_ID = "&APPID=" + BuildConfig.WEATHER_KEY;
    private static String IMG_URL = "http://openweathermap.org/img/w/";


    /**
     * call out to the weather API and get back results in a string
     * @param city
     * @return
     */
    public String getWeather(String city) {
        HttpURLConnection connection = null ;
        InputStream inputStream = null;

        try {

            //make the connection
            connection = (HttpURLConnection) (new URL(BASE_URL + city + APP_ID + "&units=metric")).openConnection();
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