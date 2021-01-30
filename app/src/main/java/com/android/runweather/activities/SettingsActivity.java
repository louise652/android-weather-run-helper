package com.android.runweather.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;

import java.util.Calendar;

import static com.android.runweather.activities.MainActivity.END_TIME_INDEX;
import static com.android.runweather.activities.MainActivity.START_TIME_INDEX;
import static com.android.runweather.activities.MainActivity.SUNRISE;
import static com.android.runweather.activities.MainActivity.SUNSET;
import static com.android.runweather.activities.MainActivity.TIME_PREFERENCES;
import static com.android.runweather.activities.MainActivity.TWELVE;

/**
 * Activity to handle selecting either a predefined time window or custom time range to narrow down results.
 */

public class SettingsActivity extends AppCompatActivity {
    public static final int TWENTY_FOUR = 24;
    private static final String TIME_PREF_SELECTED = "timePrefSelected";
    private static final String CUSTOM_START_TIME = "customStart";
    private static final String CUSTOM_END_TIME = "customEnd";
    public SharedPreferences timePrefs;
    Spinner timeRange;
    NumberPicker hourFrom, hourTo;
    LinearLayout customSelection;
    private int startTimeIndex, endTimeIndex, itemSelected, hoursUntilTomorrow, currentHour, customFromHr, customToHr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        hoursUntilTomorrow = TWENTY_FOUR - currentHour;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        instantiateTimeComponents();
        getUserTimePrefs();

        setNumberPickerVals(hourFrom, customFromHr);
        setNumberPickerVals(hourTo, customToHr);

        setupCustomTimePickerListeners();
        setupTimeRangePickerListener();
    }

    /*
     * spinner listener to define preselected from and to time range
     */
    private void setupTimeRangePickerListener() {

        timeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customSelection.setVisibility(View.INVISIBLE);
                itemSelected = position;
                Intent intent = getIntent();
                int sunrise = intent.getIntExtra(SUNRISE, 0);
                int sunset = intent.getIntExtra(SUNSET, TWENTY_FOUR);

                setTimeWindowIndexes(position, sunrise, sunset);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    /*
     *Listener for custom to and from number pickers
     */
    private void setupCustomTimePickerListeners() {
        hourFrom.setOnValueChangedListener((picker, oldVal, newVal) -> {
            customFromHr = picker.getValue();
            if (currentHour > picker.getValue()) { //tomorrow, start time is hours until midnight + value
                startTimeIndex = hoursUntilTomorrow + newVal;
            } else { //today, start tine is val - current hour
                startTimeIndex = newVal - currentHour;
            }
        });
        hourTo.setOnValueChangedListener((picker, oldVal, newVal) -> {
            customToHr = picker.getValue();
            if (customToHr > customFromHr) { //add difference between to and from time to start index
                endTimeIndex = startTimeIndex + (customToHr - customFromHr);
            } else { // add num hours between today and tomorrow to start index
                endTimeIndex = startTimeIndex + (customToHr + (TWENTY_FOUR - customFromHr));
            }
        });
    }

    private void instantiateTimeComponents() {
        timeRange = findViewById(R.id.timeSelection);
        customSelection = findViewById(R.id.customSelection);
        hourFrom = findViewById(R.id.hourFrom);
        hourTo = findViewById(R.id.hourTo);
        timePrefs = getSharedPreferences(TIME_PREFERENCES, Context.MODE_PRIVATE);

        //Show the time range the user has previously selected or default to option 1 (next 4 hours)
        int spinnerPosition = timePrefs.getInt(TIME_PREF_SELECTED, 1);
        timeRange.setSelection(spinnerPosition);
    }

    /*
     * Grab time in hours and the indexes from shared preferences
     */
    private void getUserTimePrefs() {
        customFromHr = timePrefs.getInt(CUSTOM_START_TIME, 0);
        customToHr = timePrefs.getInt(CUSTOM_END_TIME, TWELVE);

        startTimeIndex = timePrefs.getInt(START_TIME_INDEX, 0);
        endTimeIndex = timePrefs.getInt(END_TIME_INDEX, TWELVE);
    }

    /*
     * Set indexes based on user time window selection
     */
    private void setTimeWindowIndexes(int position, int sunrise, int sunset) {
        switch (position) {
            case 0:
                //2 hours
                startTimeIndex = 0;
                endTimeIndex = 2;
                break;
            case 1:
                //4 hours
                startTimeIndex = 0;
                endTimeIndex = 4;
                break;
            case 2:
                //8 hours
                startTimeIndex = 0;
                endTimeIndex = 8;
                break;
            case 3://daytime: logic based on sunrise/sunset
                setDaytimeIndexes(sunrise, sunset);
                break;
            case 4:
                //tomorrow daylight: sunrise and sunset should be similar enough between today and tomorrow to reuse here
                startTimeIndex = hoursUntilTomorrow + sunrise;
                endTimeIndex = hoursUntilTomorrow + sunset + 1;
                break;
            case 5:
                //custom, show pickers so user can select their own tine range
                customSelection.setVisibility(View.VISIBLE);
                break;

            default:
                startTimeIndex = 0;
                endTimeIndex = TWELVE;
                break;
        }
    }

    /*
     * If option is selected it will show hourly weather for daylight today.
     * A toast will display if there are no hours of daylight left.
     */
    private void setDaytimeIndexes(int sunrise, int sunset) {

        if (currentHour >= sunset) {
            Toast.makeText(getApplicationContext(), "No more daylight today- try another selection", Toast.LENGTH_SHORT).show();
            //default to next twelve hours
            startTimeIndex = 0;
            endTimeIndex = TWELVE;
        } else {
            startTimeIndex = (currentHour < sunrise ? sunrise : 0);
            endTimeIndex = sunset - currentHour + 1;
        }
    }

    /*
     * Uses number pickers to allow the user to select hours from a 24h time range (00:00-23:00)
     * Defaults from the current hour to currentHour +4
     */
    private void setNumberPickerVals(NumberPicker np, int defaultHour) {

        np.setMaxValue(23);
        np.setMinValue(0);

        np.setValue(defaultHour);
        np.setFormatter(i -> String.format("%02d:00", i));

        /*
         * Fixes a bug where the default does not display on load due to the formatter. See
         * https://stackoverflow.com/questions/17708325/android-numberpicker-with-formatter-doesnt-format-on-first-rendering/44949069#44949069
         */
        View firstItem = np.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    /*
     * Button saves user preferences and loads up suggested timeslots accordingly
     */
    public void savePrefs(View view) {

        SharedPreferences.Editor editor = timePrefs.edit();

        editor.putInt(START_TIME_INDEX, startTimeIndex);
        editor.putInt(END_TIME_INDEX, endTimeIndex);
        editor.putInt(TIME_PREF_SELECTED, itemSelected);

        editor.putInt(CUSTOM_START_TIME, customFromHr);
        editor.putInt(CUSTOM_END_TIME, customToHr);

        editor.apply();
        Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
