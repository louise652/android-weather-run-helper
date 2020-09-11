package com.android.runweather.models.FutureWeather;

import com.android.runweather.models.CurrentWeather.Clouds;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class List {
    public Clouds clouds;
    public int visibility;
    public double pop; //rain probability
    public String dt_txt;
    public java.util.List<Weather> weather;
    public Main main;
    public Wind wind;
}
