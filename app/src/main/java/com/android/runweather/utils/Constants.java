package com.android.runweather.utils;

public class Constants {

    //shared preferences
    public static final String TIME_PREFERENCES = "timePreferences";
    public static final String WEATHER_PREFERENCES = "weatherPreferences";

    //fields to ge the preferred times
    public static final String START_TIME_INDEX = "startTime";
    public static final String END_TIME_INDEX = "endTime";
    public static final String SUNRISE = "sunrise";
    public static final String SUNSET = "sunset";
    public static final String TIME_PREF_SELECTED = "timePrefSelected";
    public static final String CUSTOM_START_TIME = "customStart";
    public static final String CUSTOM_END_TIME = "customEnd";

    //toasts
    public static final String WEATHER_RESULT_TXT = "Getting weather results for ";
    public static final String LOCATION_ERROR_TXT = "Could not get your location. Ensure location permission is granted or try later";
    public static final String DAYLIGHT_ERROR_TXT = "No more daylight today- try another selection";
    public static final String SETTINGS_SAVED = "Settings saved";

    //weather condition options
    public static final String SUNLIGHT = "Sunlight";
    public static final String WIND = "Less Wind";
    public static final String TEMP = "Good Temp";
    public static final String RAIN = "Less Rain";

    //Order of user weather prefs, these need to be alphabetical for ordering
    public static final String A = "a";
    public static final String B = "b";
    public static final String C = "c";
    public static final String D = "d";

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int FOUR = 4;
    public static final int EIGHT = 8;
    public static final int TWELVE = 12;
    public static final int TWENTY_THREE = 23;
    public static final int TWENTY_FOUR = 24;
    public static final int ONE_HUNDRED = 100;


}
