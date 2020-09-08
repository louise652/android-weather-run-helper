package com.android.runweather.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
  Representation of a LocationVO returned from the weather API
 */

@Getter
@Setter

public class LocationVO  implements Serializable {

    private float longitude;
    private float latitude;
    private long sunset;
    private long sunrise;
    private String country;
    private String city;

}