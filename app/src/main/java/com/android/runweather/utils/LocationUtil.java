package com.android.runweather.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.runweather.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtil {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static LocationUtil locationUtil;

    private Activity activity;


    private LocationUtil(Activity activity) {
        this.activity = activity;
    }

    public static synchronized LocationUtil getInstance(Activity activity) {
        if (locationUtil == null) {
            locationUtil = new LocationUtil(activity);
        }
        return locationUtil;
    }

    public String checkLocationPermission() {
        String locationResult = "";
        //check to see if we have permission for location
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            showLocationPermissionPrompt();
        }

        //if we have permission, grab coords
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = (locationManager.getBestProvider(criteria, false) != null ? locationManager.getBestProvider(criteria, false) : "");

        if (provider != "") {
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                locationResult = getCityFromLocation(location);
            }

        }
        System.out.println(locationResult);
        return locationResult;
    }

    private String getCityFromLocation(Location location) {
        String locationResult;

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            locationResult = addresses.get(0).getSubAdminArea(); //returns the city/town location
        } catch (IOException e) {
            locationResult = "";
            e.printStackTrace();
        }
        return locationResult;
    }

    private void showLocationPermissionPrompt() {
        // No permission. Show why we need it
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            elicitLocationPermission();
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void elicitLocationPermission() {
        //build an async alert with an explanation and extra prompt
        new AlertDialog.Builder(activity)
                .setTitle(R.string.title_location_permission)
                .setMessage(R.string.text_location_permission)
                .setPositiveButton(R.string.grant_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Closes the dialog if denied
                dialogInterface.dismiss();
            }
        })
                .create()
                .show();
    }
}