package com.android.runweather;

import android.view.View;
import android.widget.Button;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import com.android.runweather.Utils.UiAutomatorUtils;
import com.android.runweather.activities.MainActivity;
import com.android.runweather.models.WeatherVO;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.android.runweather.Utils.UiAutomatorUtils.assertViewWithTextIsVisible;
import static com.android.runweather.Utils.UiAutomatorUtils.denyCurrentPermission;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityInstrumentedTest {

    public static final WeatherVO[] WEATHER = new WeatherVO[]{
            new WeatherVO("1598562000",
                    "Description",
                    15.0,
                    17.2,
                    33,
                    10.3,
                    500)};


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false, true);

    private UiDevice device;

    @Before
    public void setup() {

        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void a_mainComponentsRender() {
        onView(withId(R.id.weatherBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.weatherBtn)).check(matches(CoreMatchers.<View>instanceOf(Button.class)));
    }


    //https://blog.egorand.me/testing-runtime-permissions-lessons-learned/
    @Test
    public void b_shouldDisplayPermissionRequestDialogOnClick() throws Exception {
        //given the app is loaded, when the weather button is clicked
        onView(withId(R.id.weatherBtn)).perform(click());

        //then the location prompt will appear
        assertViewWithTextIsVisible(device, UiAutomatorUtils.TEXT_ALLOW);
        assertViewWithTextIsVisible(device, UiAutomatorUtils.TEXT_DENY);

        // cleanup for the next test
        denyCurrentPermission(device);
    }


    @Test
    public void c_shouldDisplayRationaleIfPermissionWasDenied() throws Exception {
        //when button has been clicked
        onView(withId(R.id.weatherBtn)).perform(click());

        //given location permission has been denied
        denyCurrentPermission(device);
        onView(withId(R.id.weatherBtn)).perform(click());

        //then a rationale will display
        onView(withText(R.string.text_location_permission)).check(matches(isDisplayed()));
        onView(withText(R.string.grant_permission)).check(matches(isDisplayed()));
    }


    @Ignore("Not yet implemented")
    @Test
    public void d_shouldLoadWeatherIfPermissionWasGranted() throws Exception {
        for (WeatherVO weatherItem : WEATHER) {
            onView(withText(weatherItem.getId())).check(matches(isDisplayed()));
            //todo
        }
    }


    @Test
    public void test_locationApproved() {
        //Given button is clicked
        //When app asks for location permission and user agrees
        //Then result activity launches
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