package com.android.runweather.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.runweather.fragments.TimeSettingsFragment;
import com.android.runweather.fragments.WeatherSettingsFragment;

public class TabbedSettingsAdapter extends FragmentPagerAdapter {
    final int totalTabs;
        public TabbedSettingsAdapter(FragmentManager fm, int totalTabs) {
            super(fm);
            this.totalTabs = totalTabs;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TimeSettingsFragment();
                case 1:
                    return new WeatherSettingsFragment();

                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

