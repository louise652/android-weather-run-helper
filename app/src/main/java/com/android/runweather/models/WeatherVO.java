package com.android.runweather.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

/**
 * Openweather api object: see https://openweathermap.org/api/one-call-api for detailed description
 * of fields
 */
public class WeatherVO {
    public double lat; //latitude
    public double lon; //longitude
    public String timezone;
    public int timezone_offset; //shift in secs from UTC
    public Current current;
    public List<Hourly> hourly;

}
