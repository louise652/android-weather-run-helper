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

import static com.android.runweather.activities.MainActivity.END_TIME;
import static com.android.runweather.activities.MainActivity.START_TIME;
import static com.android.runweather.activities.MainActivity.SUNRISE;
import static com.android.runweather.activities.MainActivity.SUNSET;
import static com.android.runweather.activities.MainActivity.TIME_PREFERENCES;
import static com.android.runweather.activities.MainActivity.TWELVE;

public class SettingsActivity extends AppCompatActivity {
    public static final int TWENTY_FOUR = 24;
    private static final String TIME_PREF_SELECTED = "timePrefSelected";
    public SharedPreferences timePrefs;
    Spinner timeRange;
    NumberPicker hourFrom, hourTo;
    LinearLayout customSelection;
    private int startTime, endTime, itemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        timeRange = findViewById(R.id.timeSelection);


        customSelection = findViewById(R.id.customSelection);
        hourFrom = findViewById(R.id.hourFrom);
        hourTo = findViewById(R.id.hourTo);

        timePrefs = getSharedPreferences(TIME_PREFERENCES, Context.MODE_PRIVATE);
        //Show the time range the user has previously selected or default to option 1 (next 4 hours)
        int spinnerPosition = timePrefs.getInt(TIME_PREF_SELECTED, 1);
        timeRange.setSelection(spinnerPosition);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int futureHour = calendar.get(Calendar.HOUR_OF_DAY + 4);


        setNumberPickerVals(hourFrom, currentHour);
        setNumberPickerVals(hourTo, futureHour);



        /*
         * spinner listener to define from and to time range
         */
        timeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customSelection.setVisibility(View.INVISIBLE);
                itemSelected = position;
                Intent intent = getIntent();
                int sunrise = intent.getIntExtra(SUNRISE, 0);
                int sunset = intent.getIntExtra(SUNSET, TWENTY_FOUR);

                int hoursUntilTomorrow = TWENTY_FOUR - currentHour;
                switch (position) {

                    case 0:
                        //2 hours
                        startTime = 0;
                        endTime = 2;
                        break;
                    case 1:
                        //4 hours
                        startTime = 0;
                        endTime = 4;
                        break;
                    case 2:
                        //8 hours
                        startTime = 0;
                        endTime = 8;
                        break;
                    case 3:
                        //daytime: logic based on sunrise/sunset

                        if (currentHour >= sunset) {
                            Toast.makeText(getApplicationContext(), "No more daylight today- try another selection", Toast.LENGTH_SHORT).show();
                            //default to next twelve hours
                            startTime = 0;
                            endTime = TWELVE;
                        } else {
                            startTime = (currentHour < sunrise ? sunrise : 0);
                            endTime = sunset;
                        }

                        break;
                    case 4:
                        //tomorrow daylight: sunrise and sunset should be similar enough between today and tomorrow to reuse here
                        startTime = hoursUntilTomorrow + sunrise;
                        endTime = hoursUntilTomorrow + sunset +1;
                        break;
                    case 5:
                        //custom, show pickers so user can select their own tine range
                        customSelection.setVisibility(View.VISIBLE);
                        break;

                    default:
                        startTime = 0;
                        endTime = TWELVE;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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

        editor.putInt(START_TIME, startTime);
        editor.putInt(END_TIME, endTime);
        editor.putInt(TIME_PREF_SELECTED, itemSelected);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
