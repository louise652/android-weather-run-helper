package com.android.runweather.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class WeatherVO {
    String dt_text;
    String description;
    double temp_min;
    double temp_max;
    int humidity;
    double speed;
    int id;  //meterology code: rain between 300 and 600, clear >=800


}

