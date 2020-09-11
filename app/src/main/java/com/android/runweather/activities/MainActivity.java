package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.BuildConfig;
import com.android.runweather.R;
import com.android.runweather.utils.LocationUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

/**
 * Home activity which handles either:
 * Eliciting location service permission from the user and getting their city from it or;
 * Hooking into the Google Places API so the user can type and select their location manually
 */

public class MainActivity extends AppCompatActivity {

    String locationServiceString;
    LatLng coords;
    View view;
    AutocompleteSupportFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autocompleteFragment = initManualPlaceSelection();

        view = findViewById(R.id.manual_container);

        coords = LocationUtil.getInstance(this).checkLocationPermission();
        locationServiceString = LocationUtil.getInstance(this).getCityFromCoords(coords.latitude, coords.longitude);

        System.out.println(coords);

        if (coords != null) {
            autocompleteFragment.setText(locationServiceString);

        }

        initManualLocation();

    }

    /**
     * Overidden methods to handle manual location entry
     */
    private void initManualLocation() {

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setHint(getString(R.string.location_hint));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locationServiceString = place.getName();

                coords = place.getLatLng();

            }

            @Override
            public void onError(@NonNull Status status) {
                locationServiceString = R.string.place_error + status.getStatusMessage();
            }
        });
    }


    /**
     * Setup the AutoCompleteSupport fragment. This allows the user to search for a location if they do not want to use the location services
     *
     * @return fragment view
     */
    private AutocompleteSupportFragment initManualPlaceSelection() {
        // Initialize Places.
        Places.initialize(getApplicationContext(), BuildConfig.PLACES_KEY);

        // Initialize the AutocompleteSupportFragment.
        return (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
    }

    /**
     * Call out to weather API with the location passed as a param on button click
     *
     * @param view Button to call the api with location
     */
    public void callWeather(View view) {
        Toast.makeText(this, "Getting weather results for " + locationServiceString, Toast.LENGTH_SHORT).show();

        //Launch result activity and pass in City
        Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
        resultIntent.putExtra("city", locationServiceString);
        resultIntent.putExtra("lat", coords.latitude);
        resultIntent.putExtra("lng", coords.longitude);
        MainActivity.this.startActivity(resultIntent);

    }
}