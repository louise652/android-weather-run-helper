package com.android.runweather.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.runweather.Fragments.TimeSettingsFragment;
import com.android.runweather.Fragments.WeatherSettingsFragment;

public class TabbedSettingsAdapter extends FragmentPagerAdapter {
        private final Context myContext;
        int totalTabs;
        public TabbedSettingsAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }
        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TimeSettingsFragment timeSettingsFragment = new TimeSettingsFragment();
                    return timeSettingsFragment;
                case 1:
                    WeatherSettingsFragment weatherSettingsFragment = new WeatherSettingsFragment();
                    return weatherSettingsFragment;

                default:
                    return null;
            }
        }
        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

