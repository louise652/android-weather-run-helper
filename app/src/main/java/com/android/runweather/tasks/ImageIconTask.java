package com.android.runweather.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.android.runweather.clients.WeatherClient;

import static com.android.runweather.utils.Constants.ZERO;


/**
 * Task to retrieve the icon for a given weather condition
 */
public class ImageIconTask extends AsyncTask<String, Void, Drawable> {

    /*
     * Call out to the weather client to retrieve image for a meteorology code
     */
    @Override
    protected Drawable doInBackground(String... params) {

        return (new WeatherClient()).getImage(params[ZERO]);

    }

}