package com.android.runweather.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    Spinner timeRange;
    NumberPicker hourFrom, hourTo;
    LinearLayout customSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        timeRange = findViewById(R.id.timeSelection);

        customSelection = findViewById(R.id.customSelection);
        hourFrom = findViewById(R.id.hourFrom);
        hourTo = findViewById(R.id.hourTo);


        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        calendar.add(Calendar.HOUR_OF_DAY, 4);

        int previousHour = calendar.getTime().getHours();


        setNumberPickerVals(hourFrom, currentHour);
        setNumberPickerVals(hourTo, previousHour);



        /*
         * spinner listener to define from and to time range
         */
        timeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customSelection.setVisibility(View.INVISIBLE);
                switch (position) {

                    case 0:
                        //2 hours
                        break;
                    case 1:
                        //4 hours
                        break;
                    case 2:
                        //8 hours
                        break;
                    case 3:
                        //daytime
                        break;
                    case 4:
                        //tomorrow
                        break;
                    case 5:
                        //custom, show pickers so user can select their own tine range
                        customSelection.setVisibility(View.VISIBLE);
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

    }
}
