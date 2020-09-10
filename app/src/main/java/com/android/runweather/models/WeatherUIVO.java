package com.android.runweather.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherUIVO {
    public String time;
    public String overall;
    public String detail;
    public String icon;
    public int id;
    public double rain;
    public int windDeg;
    public double windSpeed;
    public int clouds;
    public double temp;
    public int humidity;
    public double feeltemp;
}
