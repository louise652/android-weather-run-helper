package com.android.runweather;

import androidx.test.rule.ActivityTestRule;

import com.android.runweather.activities.ResultActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ResultActivityInstrumentedTest {
    @Rule
    public ActivityTestRule<ResultActivity> rule = new ActivityTestRule<>(ResultActivity.class);
    private ResultActivity activity;

    @Before
    void setup() {
        activity = rule.getActivity();
    }

    @Test
    public void test_resultComponentsRender() {
        //Given user has navigated to activity
        //When page loads
        //Then components render
    }
}
