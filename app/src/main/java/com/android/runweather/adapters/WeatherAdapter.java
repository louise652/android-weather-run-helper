package com.android.runweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.runweather.R;
import com.android.runweather.models.Weather.Hourly;

import lombok.NonNull;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        Hourly item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.weather_item, parent, false);
        TextView dt_txt = itemView.findViewById(R.id.dt_txt);
        dt_txt.setText(Integer.toString(item.getDt()));

        TextView description = itemView.findViewById(R.id.description);
        description.setText(item.getWeather().get(0).getMain() + ": " + item.getWeather().get(0).getDescription());

        TextView pop = itemView.findViewById(R.id.pop);
        pop.setText(String.valueOf(item.getPop()));


        TextView clouds = itemView.findViewById(R.id.clouds);
        clouds.setText(Integer.toString(item.getClouds()));

        TextView id = itemView.findViewById(R.id.id);
        id.setText(Integer.toString(item.getWeather().get(0).getId()));

        TextView icon = itemView.findViewById(R.id.icon);
        icon.setText(item.getWeather().get(0).getIcon());


        TextView temp = itemView.findViewById(R.id.temp);
        temp.setText(String.valueOf(item.getTemp()));

        TextView humidity = itemView.findViewById(R.id.humidity);
        humidity.setText(Integer.toString(item.getHumidity()));

        TextView feelsLike = itemView.findViewById(R.id.feelsLike);
        feelsLike.setText(String.valueOf(item.getFeels_like()));

        TextView speed = itemView.findViewById(R.id.speed);
        speed.setText(String.valueOf(item.getWind_speed()));


        //todo:datetime
        return itemView;
    }
}
