package com.android.runweather.models.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    public int id; //weather condition id
    public String description;//more detailed description
    public String icon; //icon id
}
