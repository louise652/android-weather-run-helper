package com.android.runweather.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import com.android.runweather.BuildConfig;
import com.android.runweather.R;

import static com.android.runweather.utils.Constants.SUNRISE;
import static com.android.runweather.utils.Constants.SUNSET;

public class MenuActivity extends AppCompatActivity {

    public static final String JOG_ON_FEEDBACK = "Jog On feedback";
    public static final String CHOOSE_AN_EMAIL_CLIENT = "Choose an Email client";
    public static final String MAILTO = "mailto:";
    private static final String EMAIL_ADDRESS = BuildConfig.EMAIL;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuBuilder m = (MenuBuilder) menu;
        //noinspection RestrictedApi
        m.setOptionalIconsVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;

            case R.id.menu_email:

                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse(MAILTO));
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
                email.putExtra(Intent.EXTRA_SUBJECT, JOG_ON_FEEDBACK);
                startActivity(Intent.createChooser(email, CHOOSE_AN_EMAIL_CLIENT));
                return true;

            case R.id.menu_settings:
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                int sunrise = sharedPref.getInt(SUNRISE, 0);
                int sunset = sharedPref.getInt(SUNSET, 0);
                Intent settingsIntent = new Intent(getApplicationContext(), TabbedSettingsActivity.class);
                settingsIntent.putExtra(SUNRISE, sunrise);
                settingsIntent.putExtra(SUNSET, sunset);
                startActivity(settingsIntent);
                return true;

            case R.id.menu_help:
                Intent helpIntent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(helpIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

