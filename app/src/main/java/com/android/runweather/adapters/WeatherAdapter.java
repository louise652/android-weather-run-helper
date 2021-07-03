package com.android.runweather.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.runweather.R;
import com.android.runweather.models.Hourly;
import com.android.runweather.tasks.ImageIconTask;
import com.android.runweather.utils.FormattingUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.android.runweather.utils.Constants.ONE_HUNDRED;
import static com.android.runweather.utils.Constants.ZERO;
import static com.android.runweather.utils.FormattingUtils.getDate;
import static com.android.runweather.utils.FormattingUtils.sdf;
import static org.apache.commons.lang3.text.WordUtils.capitalize;

/**
 * Adapter to display hourly weather info
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private final List<Hourly> items;
    private final Context context;

    public WeatherAdapter(Context context, List<Hourly> items) {
        this.context = context;
        this.items = items;
    }

    /*
     * Get the view
     */

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.weather_item, parent, false);

        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        Hourly item = items.get(position);

        //kick off an async task to get weather image
        ImageIconTask iconTask = new ImageIconTask();
        iconTask.execute(item.getWeather().get(ZERO).getIcon());


        //set display fields with the model data
        String time = sdf.format(getDate(item.getDt()));
        String description = capitalize(item.getWeather().get(ZERO).getDescription());
        holder.description.setText(String.format("%s- %s", time, description));
        holder.precip.setText(String.format("%.0f%%", (item.getPop() * ONE_HUNDRED))); //probability is in decimal format, * 100 to get percent
        holder.clouds.setText(String.format("%s%%", item.getClouds()));
        holder.temp.setText(FormattingUtils.formatTemperature(item.getTemp()));
        holder.feelsLike.setText(FormattingUtils.formatTemperature(item.getFeels_like()));
        holder.speed.setText(String.format("%sm/s", item.wind_speed));

        //get icon for weather condition
        try {
            Drawable drawable = iconTask.get();
            //set Img
            holder.img.setBackground(drawable);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        determineClothing(holder, item, description);
    }

    private void determineClothing(@NonNull ViewHolder holder, Hourly item, String description) {
        if (description.contains("Snow") || item.getFeels_like() < 5) {
            setClothesViews(holder, R.drawable.winter, "It is very cold", item.isDaylight);
        } else if (description.contains("Rain")) {
            setClothesViews(holder, R.drawable.raining, "It is very wet",  item.isDaylight);
        } else if (item.getFeels_like() > 10) {
            setClothesViews(holder, R.drawable.summer, "It is very hot",  item.isDaylight);
        } else {
            setClothesViews(holder, R.drawable.autum, "It is very temperate",  item.isDaylight);
        }
    }

    public void setClothesViews(@NonNull WeatherAdapter.ViewHolder holder, int icon, String text, boolean isDaytime) {
        holder.clothesImg.setImageResource(icon);
        holder.clothesDescription.setText(text);

        if (!isDaytime){
            holder.hiVisImg.setVisibility(View.VISIBLE);
        }else{
            holder.hiVisImg.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView description;
        private final TextView precip;
        private final TextView clouds;
        private final TextView temp;
        private final TextView feelsLike;
        private final TextView speed;
        private final TextView clothesDescription;
        private final ImageView img;
        private final ImageView clothesImg, hiVisImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //instantiate UI components
            description = itemView.findViewById(R.id.description);
            precip = itemView.findViewById(R.id.precip);
            clouds = itemView.findViewById(R.id.clouds);
            temp = itemView.findViewById(R.id.temp);
            feelsLike = itemView.findViewById(R.id.feelsLike);
            speed = itemView.findViewById(R.id.speed);
            clothesDescription = itemView.findViewById(R.id.clothesTV);

            img = itemView.findViewById(R.id.condIconHourly);
            clothesImg = itemView.findViewById(R.id.clothesIV);
            hiVisImg = itemView.findViewById(R.id.hiVisIV);
        }


    }
}
