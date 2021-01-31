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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.runweather.R;
import com.android.runweather.adapters.RecyclerViewAdapter;
import com.android.runweather.interfaces.StartDragListener;
import com.android.runweather.utils.ItemMoveCallback;

import java.util.ArrayList;
import java.util.Calendar;

import static com.android.runweather.utils.Constants.CUSTOM_END_TIME;
import static com.android.runweather.utils.Constants.CUSTOM_START_TIME;
import static com.android.runweather.utils.Constants.DAYLIGHT_ERROR_TXT;
import static com.android.runweather.utils.Constants.EIGHT;
import static com.android.runweather.utils.Constants.END_TIME_INDEX;
import static com.android.runweather.utils.Constants.FIRST;
import static com.android.runweather.utils.Constants.FOUR;
import static com.android.runweather.utils.Constants.FOURTH;
import static com.android.runweather.utils.Constants.ONE;
import static com.android.runweather.utils.Constants.RAIN;
import static com.android.runweather.utils.Constants.SECOND;
import static com.android.runweather.utils.Constants.SETTINGS_SAVED;
import static com.android.runweather.utils.Constants.START_TIME_INDEX;
import static com.android.runweather.utils.Constants.SUNLIGHT;
import static com.android.runweather.utils.Constants.SUNRISE;
import static com.android.runweather.utils.Constants.SUNSET;
import static com.android.runweather.utils.Constants.TEMP;
import static com.android.runweather.utils.Constants.THIRD;
import static com.android.runweather.utils.Constants.TIME_PREFERENCES;
import static com.android.runweather.utils.Constants.TIME_PREF_SELECTED;
import static com.android.runweather.utils.Constants.TWELVE;
import static com.android.runweather.utils.Constants.TWENTY_FOUR;
import static com.android.runweather.utils.Constants.TWENTY_THREE;
import static com.android.runweather.utils.Constants.TWO;
import static com.android.runweather.utils.Constants.WEATHER_PREFERENCES;
import static com.android.runweather.utils.Constants.WIND;
import static com.android.runweather.utils.Constants.ZERO;

/**
 * Activity to handle selecting either a predefined time window or custom time range to narrow down results.
 */

public class SettingsActivity extends AppCompatActivity implements StartDragListener {

    public SharedPreferences timePrefs, weatherPrefs;
    Spinner timeRange;
    NumberPicker hourFrom, hourTo;
    LinearLayout customSelection;
    String first, second, third, fourth;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    ItemTouchHelper touchHelper;
    private int startTimeIndex, endTimeIndex, itemSelected, hoursUntilTomorrow, currentHour, customFromHr, customToHr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        hoursUntilTomorrow = TWENTY_FOUR - currentHour;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        instantiateTimeComponents();
        instantiateWeatherPrefPicker();

        getUserTimePrefs();
        getWeatherPrefs();

        setNumberPickerVals(hourFrom, customFromHr);
        setNumberPickerVals(hourTo, customToHr);

        setupCustomTimePickerListeners();
        setupTimeRangePickerListener();


    }

    private void instantiateWeatherPrefPicker() {
        weatherPrefs = getSharedPreferences(WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.weatherPrefsRV);

        populateRecyclerView();
    }

    /*
     * Add the adapter to allow the user to drag/drop their preferred weather conditions
     */
    private void populateRecyclerView() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add(SUNLIGHT);
        stringArrayList.add(RAIN);
        stringArrayList.add(WIND);
        stringArrayList.add(TEMP);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerViewAdapter(stringArrayList, this);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
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
                int sunrise = intent.getIntExtra(SUNRISE, ZERO);
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
        customFromHr = timePrefs.getInt(CUSTOM_START_TIME, ZERO);
        customToHr = timePrefs.getInt(CUSTOM_END_TIME, TWELVE);

        startTimeIndex = timePrefs.getInt(START_TIME_INDEX, ZERO);
        endTimeIndex = timePrefs.getInt(END_TIME_INDEX, TWELVE);
    }

    /*
     * Get the order of preferred weather conditions from shared prefs
     */
    private void getWeatherPrefs() {

        first = weatherPrefs.getString(FIRST, SUNLIGHT);
        second = weatherPrefs.getString(SECOND, RAIN);
        third = weatherPrefs.getString(THIRD, WIND);
        fourth = weatherPrefs.getString(FOURTH, TEMP);
    }

    /*
     * Set indexes based on user time window selection
     */
    private void setTimeWindowIndexes(int position, int sunrise, int sunset) {
        switch (position) {
            case 0:
                //2 hours
                startTimeIndex = ZERO;
                endTimeIndex = TWO;
                break;
            case 1:
                //4 hours
                startTimeIndex = ZERO;
                endTimeIndex = FOUR;
                break;
            case 2:
                //8 hours
                startTimeIndex = ZERO;
                endTimeIndex = EIGHT;
                break;
            case 3://daytime: logic based on sunrise/sunset
                setDaytimeIndexes(sunrise, sunset);
                break;
            case 4:
                //tomorrow daylight: sunrise and sunset should be similar enough between today and tomorrow to reuse here
                startTimeIndex = hoursUntilTomorrow + sunrise;
                endTimeIndex = hoursUntilTomorrow + sunset + ONE;
                break;
            case 5:
                //custom, show pickers so user can select their own tine range
                customSelection.setVisibility(View.VISIBLE);
                break;

            default:
                startTimeIndex = ZERO;
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
            Toast.makeText(getApplicationContext(), DAYLIGHT_ERROR_TXT, Toast.LENGTH_SHORT).show();
            //default to next twelve hours
            startTimeIndex = ZERO;
            endTimeIndex = TWELVE;
        } else {
            startTimeIndex = (currentHour < sunrise ? sunrise : ZERO);
            endTimeIndex = sunset - currentHour + ONE;
        }
    }

    /*
     * Uses number pickers to allow the user to select hours from a 24h time range (00:00-23:00)
     * Defaults from the current hour to currentHour +4
     */
    private void setNumberPickerVals(NumberPicker np, int defaultHour) {

        np.setMaxValue(TWENTY_THREE);
        np.setMinValue(ZERO);

        np.setValue(defaultHour);
        np.setFormatter(i -> String.format("%02d:00", i));

        /*
         * Fixes a bug where the default does not display on load due to the formatter. See
         * https://stackoverflow.com/questions/17708325/android-numberpicker-with-formatter-doesnt-format-on-first-rendering/44949069#44949069
         */
        View firstItem = np.getChildAt(ZERO);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    /*
     * Button saves user preferences and loads up suggested timeslots accordingly
     */
    public void savePrefs(View view) {

        SharedPreferences.Editor timePrefEditor = timePrefs.edit();

        timePrefEditor.putInt(START_TIME_INDEX, startTimeIndex);
        timePrefEditor.putInt(END_TIME_INDEX, endTimeIndex);
        timePrefEditor.putInt(TIME_PREF_SELECTED, itemSelected);

        timePrefEditor.putInt(CUSTOM_START_TIME, customFromHr);
        timePrefEditor.putInt(CUSTOM_END_TIME, customToHr);

        timePrefEditor.apply();

        SharedPreferences.Editor weatherPrefEditor = weatherPrefs.edit();
        weatherPrefEditor.putString(FIRST, first);
        weatherPrefEditor.putString(SECOND, second);
        weatherPrefEditor.putString(THIRD, third);
        weatherPrefEditor.putString(FOURTH, fourth);

        weatherPrefEditor.apply();

        Toast.makeText(getApplicationContext(), SETTINGS_SAVED, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
