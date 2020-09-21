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

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private List<Hourly> items;
    Context context;

    public WeatherAdapter(Context context, List<Hourly> items) {
        this.context = context;
        this.items = items;
    }

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

        ImageIconTask icontask = new ImageIconTask();
        icontask.execute(item.getWeather().get(0).getIcon());

        holder.dt_txt.setText(FormattingUtils.formatDateTime(Integer.toString(item.getDt())));
        holder.description.setText(WordUtils.capitalize(item.getWeather().get(0).getDescription()));
        holder.precip.setText(String.format("%.0f%%", (item.getPop() * 100))); //probability is in decimal format, * 100 to get percent
        holder.clouds.setText(String.format("%s%%", item.getClouds()));
        holder.temp.setText(FormattingUtils.formatTemperature(item.getTemp()));
        holder.feelsLike.setText(FormattingUtils.formatTemperature(item.getFeels_like()));
        holder.speed.setText(String.format("%sm/s", item.wind_speed));

        //get icon for weather condition
        try {
            Drawable drawable = icontask.get();
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


        TextView dt_txt;
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
            dt_txt = itemView.findViewById(R.id.dt_txt);
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
