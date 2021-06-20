package com.android.runweather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hourly {

    public int dt; //unix utc forecasted time
    public double temp;
    public double feels_like; //human perception pf temp
    public int humidity;//humidity percent
    public int clouds; //% cloudiness
    public double wind_speed;
    public Double pop; //probability of precipitation
    public List<Weather> weather;
    public int tempRank; //ranks how favourable the temperature is (lowest = best)
    public boolean isDaylight; //between sunrise and sunset

}
