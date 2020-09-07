package com.android.runweather.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.R;
import com.android.runweather.utils.LocationUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class MainActivity extends AppCompatActivity {
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void getWeather(View view) {
        String locationServiceString = LocationUtil.getInstance(this).checkLocationPermission();

        if (locationServiceString.isEmpty()) {
            getManualLocation();
        }
    }

    private void getManualLocation() {
        txtView = findViewById(R.id.txtView);

        // Initialize the AutocompleteSupportFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES) //don't need anymore granularity than "City"
                .build();
        autocompleteFragment.setFilter(filter);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                txtView.setText(place.getName());
            }


            @Override
            public void onError(Status status) {
                txtView.setText(status.toString());
            }
        });
    }


}