package com.android.runweather.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.runweather.R;
import com.android.runweather.adapters.RecyclerViewAdapter;
import com.android.runweather.interfaces.StartDragListener;
import com.android.runweather.utils.ItemMoveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.android.runweather.utils.Constants.CUSTOM_ORDER;
import static com.android.runweather.utils.Constants.ORDER_PREFERENCES;
import static com.android.runweather.utils.Constants.PREF_A;
import static com.android.runweather.utils.Constants.PREF_B;
import static com.android.runweather.utils.Constants.PREF_C;
import static com.android.runweather.utils.Constants.PREF_D;
import static com.android.runweather.utils.Constants.RAIN;
import static com.android.runweather.utils.Constants.SETTINGS_SAVED;
import static com.android.runweather.utils.Constants.SUNLIGHT;
import static com.android.runweather.utils.Constants.TEMP;
import static com.android.runweather.utils.Constants.WEATHER_PREFERENCES;
import static com.android.runweather.utils.Constants.WIND;

public class WeatherSettingsFragment extends Fragment implements StartDragListener, View.OnClickListener  {

    public SharedPreferences weatherPrefs, orderPrefs;
    RadioButton customOrder, timeOrder;
    RadioGroup customOrderRG;

    LinearLayout customSelection, weatherPrefsLL;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;
    ItemTouchHelper touchHelper;
    Button saveWeatherSettings;

    public WeatherSettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_settings, container, false);


        customOrderRG = view.findViewById(R.id.customOrderRG);

        customOrder = view.findViewById(R.id.radioCustom);
        customOrder.setOnClickListener(this::onCustomPrefClick);

        timeOrder = view.findViewById(R.id.radioTime);
        timeOrder.setOnClickListener(this::onTimeAscClick);

        weatherPrefsLL = view.findViewById(R.id.weatherPrefsLL);
        orderPrefs = getActivity().getSharedPreferences(ORDER_PREFERENCES, Context.MODE_PRIVATE);

        if (!orderPrefs.getBoolean(CUSTOM_ORDER, false)) {
            customOrderRG.check((R.id.radioTime));
            weatherPrefsLL.setVisibility(View.INVISIBLE);
        }else{
            customOrderRG.check((R.id.radioCustom));
            weatherPrefsLL.setVisibility(View.VISIBLE);
        }

        weatherPrefs = getActivity().getSharedPreferences(WEATHER_PREFERENCES, Context.MODE_PRIVATE);
        recyclerView = view.findViewById(R.id.weatherPrefsRV);

        saveWeatherSettings = view.findViewById(R.id.saveWeatherSettings);
        saveWeatherSettings.setOnClickListener(this);




        populateRecyclerView();

    return view;
    }

    /*
     * Add the adapter to allow the user to drag/drop their preferred weather conditions
     */
    private void populateRecyclerView() {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> stringArrayList = setupOrderedWeatherPrefs();
        mAdapter = new RecyclerViewAdapter(stringArrayList, this);

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }

    /*
     * Sets default ordering if user prefs are not set
     */
    private ArrayList<String> setupOrderedWeatherPrefs() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        //setting defaults if empty
        if (weatherPrefs.getString(PREF_A, "").isEmpty()) {
            stringArrayList.add(SUNLIGHT);
            stringArrayList.add(RAIN);
            stringArrayList.add(TEMP);
            stringArrayList.add(WIND);
        } else {

            //otherwise loop through prefs in order and add value to list
            Map<String, ?> prefsKeys = weatherPrefs.getAll();
            //loop through each pref and add the comparator to order appropriately
            ArrayList<String> prefsString = new ArrayList<>(Arrays.asList(PREF_A, PREF_B, PREF_C, PREF_D));

            for (String pref : prefsString) {
                for (Map.Entry<String, ?> entry : prefsKeys.entrySet()) {
                    if (entry.getKey().equals(pref)) { //loop through secondary prefs and order
                        stringArrayList.add(entry.getValue().toString());
                    }
                }
            }
        }
        return stringArrayList;
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }


    public void onCustomPrefClick(View view) {
        weatherPrefsLL.setVisibility(View.VISIBLE);
    }

    public void onTimeAscClick(View view) {
        weatherPrefsLL.setVisibility(View.INVISIBLE);

    }


    /*
     * Button saves user preferences and loads up suggested timeslots accordingly
     */

    @Override
    public void onClick(View v) {

        SharedPreferences.Editor weatherPrefEditor = weatherPrefs.edit();
        weatherPrefEditor.putString(PREF_A, mAdapter.data.get(0));
        weatherPrefEditor.putString(PREF_B, mAdapter.data.get(1));
        weatherPrefEditor.putString(PREF_C, mAdapter.data.get(2));
        weatherPrefEditor.putString(PREF_D, mAdapter.data.get(3));

        weatherPrefEditor.apply();

        SharedPreferences.Editor orderPrefEditor = orderPrefs.edit();
        orderPrefEditor.putBoolean(CUSTOM_ORDER, customOrder.isChecked());
        orderPrefEditor.apply();

        Toast.makeText(getContext(), SETTINGS_SAVED, Toast.LENGTH_SHORT).show();

    }
}