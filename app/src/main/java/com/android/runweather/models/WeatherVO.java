package com.android.runweather.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeatherVO {
    LocalDateTime dt_text;
    String description;
    Double temp_min;
    Double temp_max;
    Double humidity;
    Double speed;
    int id;  //meterology code: rain between 300 and 600, clear >=800


}

