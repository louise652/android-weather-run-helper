package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class HelpActivity extends AppCompatActivity {

    ExpandableTextView aboutTxt, settingsWeatherText, howToText, settingsTimeText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);
        aboutTxt = findViewById(R.id.about_text_view);
        aboutTxt.setText(getString(R.string.about));

        howToText = findViewById(R.id.how_to_text_view);
        howToText.setText(getString(R.string.howToUse));

        settingsTimeText = findViewById(R.id.settings_time_text_view);
        settingsTimeText.setText(getString(R.string.settingsTimeHelp));

        settingsWeatherText = findViewById(R.id.settings_weather_text_view);
        settingsWeatherText.setText(getString(R.string.settingsWeatherHelp));
    }

    // Add a home button to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));


        return true;
    }
}
