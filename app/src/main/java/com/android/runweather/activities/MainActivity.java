package com.android.runweather.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.utils.LocationUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView txtCity;
    String locationServiceString;
    View view;
    AutocompleteSupportFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autocompleteFragment = initManualPlaceSelection();

        view = findViewById(R.id.manual_container);
        locationServiceString = LocationUtil.getInstance(this).checkLocationPermission();
        System.out.println(locationServiceString);

        if (!locationServiceString.isEmpty()) {
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

        autocompleteFragment.setHint("Enter your location");

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                locationServiceString = place.getName();
                System.out.println("Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred: " + status);
            }
        });
    }


    /**
     * Setup the AutoCompleteSupport fragment. This allows the user to serach for a location if they do not want to use the lcoation services
     *
     * @return
     */
    private AutocompleteSupportFragment initManualPlaceSelection() {
        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyD9Qbv5Ve6hw76OEHA9GyGyUMQP0lD3uVE");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        return (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
    }

    /**
     * Call out to weather API with the location passed as a param on button click
     *
     * @param view
     */
    public void callWeather(View view) {
        Toast.makeText(this, "Calling API with location: " + locationServiceString, Toast.LENGTH_LONG).show();
    }
}