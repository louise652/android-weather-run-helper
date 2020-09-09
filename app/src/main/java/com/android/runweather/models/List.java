package com.android.runweather.models;

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
    public String dt_txt;
    public java.util.List<Weather> weather;
    public Rain rain;
    public Main main;
    public Wind wind;
}
