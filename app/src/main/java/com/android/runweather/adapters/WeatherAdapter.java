package com.android.runweather.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.runweather.R;
import com.android.runweather.models.Weather.Hourly;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Adapter to display the hourly weather results
 */
public class WeatherAdapter extends ArrayAdapter<Hourly> {
    Context context;
    java.util.List<Hourly> items;

    public WeatherAdapter(Context context, java.util.List<Hourly> items) {
        super(context, R.layout.weather_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Hourly item = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.weather_item, parent, false);

        initAndSetWeatherListItem(item, itemView);

        return itemView;
    }

    /**
     * Instantiates the HourlyWeather UI components and sets the values
     *
     * @param item     hourly weather object
     * @param itemView UI view
     */
    @SuppressLint("SetTextI18n")
    private void initAndSetWeatherListItem(Hourly item, View itemView) {

        //instantiate UI components
        TextView dt_txt = itemView.findViewById(R.id.dt_txt);
        TextView description = itemView.findViewById(R.id.description);
        TextView precip = itemView.findViewById(R.id.precip);
        TextView clouds = itemView.findViewById(R.id.clouds);
        TextView id = itemView.findViewById(R.id.id);
        TextView icon = itemView.findViewById(R.id.icon);
        TextView temp = itemView.findViewById(R.id.temp);
        TextView humidity = itemView.findViewById(R.id.humidity);
        TextView feelsLike = itemView.findViewById(R.id.feelsLike);
        TextView speed = itemView.findViewById(R.id.speed);

        //set UI text fields
        dt_txt.setText(getDateString(Integer.toString(item.getDt())));
        description.setText(item.getWeather().get(0).getDescription());
        precip.setText(Double.toString(item.getPop()));
        clouds.setText(Integer.toString(item.getClouds()));
        id.setText(Integer.toString(item.getWeather().get(0).getId()));
        icon.setText(item.getWeather().get(0).getIcon());
        temp.setText(String.valueOf(item.getTemp()));
        humidity.setText(Integer.toString(item.getHumidity()));
        feelsLike.setText(String.valueOf(item.getFeels_like()));
        speed.setText(String.valueOf(item.getWind_speed()));
    }

    /**
     * Format the timestamp and get the time
     *
     * @param strDate time since epoch
     * @return time
     */
    private String getDateString(String strDate) {
        long longDate = Long.parseLong(strDate) * 1000;
        Date date = new Date();
        date.setTime(longDate);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
        return sdf.format(date);
    }
}
