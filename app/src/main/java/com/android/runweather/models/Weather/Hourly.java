package com.android.runweather.models.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hourly {

    public int dt; //unix utc forecasted time
    public double temp;
    public double feels_like; //human perception pf temp
    public int humidity;//humidity percent
    public int clouds; //% cloudiness
    public double wind_speed;
    public List<Weather> weather;
    public double pop; //probability of precipitation
}
