package com.android.runweather.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

/*
  Representation of a WeatherVO returned from the weather API
 */
public class WeatherVO {
    public LocationVO location;
    public CurrentCondition currentCondition;
    public Temperature temperature;
    public Wind wind;
    public Rain rain;
    public Snow snow ;
    public Clouds clouds;

    public byte[] iconData;

    @Getter
    @Setter
    public  class CurrentCondition {
        private int weatherId; //meterology code: rain between 300 and 600, clear >=800
        private String condition;
        private String descr;
        private String icon;


        private float pressure;
        private float humidity;


    }
    @Getter
    @Setter
    public  class Temperature {
        private float temp;
        private float minTemp;
        private float maxTemp;

    }
    @Getter
    @Setter
    public  class Wind {
        private float speed;
        private float deg;

    }
    @Getter
    @Setter
    public  class Rain {
        private String time;
        private float ammount;



    }
    @Getter
    @Setter
    public  class Snow {
        private String time;
        private float ammount;



    }
    @Getter
    @Setter
    public  class Clouds {
        private int perc;





    }

}

