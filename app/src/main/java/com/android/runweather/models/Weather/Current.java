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
public class Current {
    public int dt; //current time unix UTC
    public int sunrise; //sunrise unix utc
    public int sunset; //sunset unix utc
    public double temp;
    public double feels_like; //human perception of weather
    public int humidity; //humidity %
    public int clouds; //% cloudiness
    public double wind_speed;
    public List<Weather> weather;
}