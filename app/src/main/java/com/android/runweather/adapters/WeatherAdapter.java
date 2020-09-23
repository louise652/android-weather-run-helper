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

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Adapter to display hourly weather info
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<Hourly> items;
    Context context;

    public WeatherAdapter(Context context, List<Hourly> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * Get the view
     * @param parent result view
     * @param viewType
     * @return item view
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
        iconTask.execute(item.getWeather().get(0).getIcon());


        //set display fields with the model data
        String time = FormattingUtils.formatDateTime(Integer.toString(item.getDt()));
        String description = WordUtils.capitalize(item.getWeather().get(0).getDescription());
        holder.description.setText(String.format("%s- %s", time, description));
        holder.precip.setText(String.format("%.0f%%", (item.getPop() * 100))); //probability is in decimal format, * 100 to get percent
        holder.clouds.setText(String.format("%s%%", item.getClouds()));
        holder.temp.setText(FormattingUtils.formatTemperature(item.getTemp()));
        holder.feelsLike.setText(FormattingUtils.formatTemperature(item.getFeels_like()));
        holder.speed.setText(String.format("%sm/s", item.wind_speed));

        //get icon for weather condition
        try {
            Drawable drawable = iconTask.get();
            //set Img
            holder.img.setBackground(drawable);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView precip;
        TextView clouds;
        TextView temp;
        TextView feelsLike;
        TextView speed;
        View rootView;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            //instantiate UI components
            description = itemView.findViewById(R.id.description);
            precip = itemView.findViewById(R.id.precip);
            clouds = itemView.findViewById(R.id.clouds);
            temp = itemView.findViewById(R.id.temp);
            feelsLike = itemView.findViewById(R.id.feelsLike);
            speed = itemView.findViewById(R.id.speed);

            img = itemView.findViewById(R.id.condIconHourly);
        }


    }
}
