package com.android.runweather.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class WeatherVO {
    LocalDateTime dt_text;
    String description;
    double temp_min;
    double temp_max;
    int humidity;
    double speed;
    int id;  //meterology code: rain between 300 and 600, clear >=800


}

