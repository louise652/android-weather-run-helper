package com.android.runweather.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.runweather.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Utility to elicit the location service on the users device to automate location entry
 */
public class LocationUtil {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static LocationUtil locationUtil;

    private final Activity activity;


    private LocationUtil(Activity activity) {
        this.activity = activity;
    }

    public static synchronized LocationUtil getInstance(Activity activity) {
        if (locationUtil == null) {
            locationUtil = new LocationUtil(activity);
        }
        return locationUtil;
    }

    /**
     * Checks if location permission is already granted. Show prompt if not
     */
    public void checkLocationPermission() {
        //check to see if we already have permission for location
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            showLocationPermissionPrompt();
        }
    }

    /**
     * Uses the location service to get last known location
     *
     * @return user coords
     */
    public LatLng getUserLocationResult() {

        LatLng locationResult = null;
        //if we have permission, grab coords
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = (locationManager.getBestProvider(criteria, false) != null ? locationManager.getBestProvider(criteria, false) : "");

        if (provider != null && !provider.isEmpty()) {
            @SuppressLint("MissingPermission") //suppressing warning as we have previously checked for permissions
                    Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                locationResult = new LatLng(lat, lng);

            }else{
                //default if location broken (temporary)
                locationResult = new LatLng(53.4831837, -2.2300844);

            }

        }
        return locationResult;
    }

    /**
     * Translates coordinates into a town/city
     *
     * @param lat Location latitude
     * @param lng Location longitude
     * @return user town/city
     */
    public String getLocFromCoords(double lat, double lng) {

        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getSubAdminArea(); //returns the city/town location
        } catch (IOException e) {

            e.printStackTrace();
            return "";
        }
    }

    /**
     * Prompt to allow location
     */
    private void showLocationPermissionPrompt() {
        // No permission. Show why we need it
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) && activity.isFinishing()) {
            elicitLocationPermission();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


    /**
     * Detailed explanation on why we are asking for location permission
     */
    private void elicitLocationPermission() {
        //build an async alert with an explanation and extra prompt
        new AlertDialog.Builder(activity)
                .setTitle(R.string.title_location_permission)
                .setMessage(R.string.text_location_permission)
                .setPositiveButton(R.string.grant_permission, (dialogInterface, i) -> {
                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }).setNegativeButton(R.string.deny, (dialogInterface, i) -> {
            //Closes the dialog if denied
            dialogInterface.dismiss();
        })
                .create()
                .show();
    }
}