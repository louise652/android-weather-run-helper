package com.android.runweather;

import android.view.View;
import android.widget.Button;

import androidx.test.rule.ActivityTestRule;

import com.android.runweather.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity activity;

    @Before
    public void setup() {
        activity = rule.getActivity();
    }

    @Test
    public void test_mainComponentsRender() {
        View viewById = activity.findViewById(R.id.weatherBtn);
        assertThat(viewById, notNullValue());
        assertThat(viewById, instanceOf(Button.class));
    }


    @Test
    public void test_locationPrompt() {
        //Given app is loaded
        //When button clicked and location permission is not approved
        //Then location prompt appears
    }


    @Test
    public void test_locationApproved() {
        //Given button is clicked
        //When app asks for location permission and user agrees
        //Then result activity launches
    }


    @Test
    public void test_locationDenied() {
        //Given button is clicked
        //When app asks for location permission and user doesn't agree
        //Then page loads with manual option to type location
    }

    @Test
    public void test_manualLocationValid() {
        //Given button is clicked and user doesn't agree to location permission
        //When the user enters in a valid manual location
        //Then result activity launches
    }

    @Test
    public void test_manualLocationInvalid() {
        //Given button is clicked and user doesn't agree to location permission
        //When the user enters in a invalid manual location
        //An error toast appears
    }

    @Test
    public void test_help() {
        //Given app is loaded
        //When button help icon is clicked
        //Then help text appears
    }
}