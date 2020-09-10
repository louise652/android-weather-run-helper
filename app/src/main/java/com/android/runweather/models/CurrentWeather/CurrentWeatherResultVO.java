package com.android.runweather.models.CurrentWeather;

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
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CurrentWeatherResultVO {

    public List<Weather> weather;
    public Main main;
    public int visibility;
    public Wind wind;

    public Clouds clouds;
    public Sys sys;
    public int cod;
}



