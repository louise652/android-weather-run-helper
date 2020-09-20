package com.android.runweather.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.runweather.R;
import com.android.runweather.models.Hourly;
import com.android.runweather.tasks.ImageIconTask;
import com.android.runweather.utils.FormattingUtils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.concurrent.ExecutionException;

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

        ImageIconTask icontask = new ImageIconTask();
        icontask.execute(item.getWeather().get(0).getIcon());


        //instantiate UI components
        TextView dt_txt = itemView.findViewById(R.id.dt_txt);
        TextView description = itemView.findViewById(R.id.description);
        TextView precip = itemView.findViewById(R.id.precip);
        TextView clouds = itemView.findViewById(R.id.clouds);
        TextView temp = itemView.findViewById(R.id.temp);
        TextView feelsLike = itemView.findViewById(R.id.feelsLike);
        TextView speed = itemView.findViewById(R.id.speed);

        ImageView img = itemView.findViewById(R.id.condIconHourly);

        //get icon for weather condition
        try {
            Drawable d = icontask.get();
            //set Img
            img.setBackground(d);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //set UI text fields
        dt_txt.setText(FormattingUtils.formatDateTime(Integer.toString(item.getDt())));
        description.setText(WordUtils.capitalize(item.getWeather().get(0).getDescription()));
        precip.setText(String.format("%.0f%%", (item.getPop() * 100))); //probability is in decimal format, * 100 to get percent
        clouds.setText(String.format("%s%%", item.getClouds()));
        temp.setText(FormattingUtils.formatTemperature(item.getTemp()));
        feelsLike.setText(FormattingUtils.formatTemperature(item.getFeels_like()));
        speed.setText(String.format("%sm/s", item.wind_speed));


    }


}
