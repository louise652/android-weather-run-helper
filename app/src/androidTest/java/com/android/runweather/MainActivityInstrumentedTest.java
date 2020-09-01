package com.android.runweather;

import android.view.View;
import android.widget.Button;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import com.android.runweather.Utils.UiAutomatorUtils;
import com.android.runweather.activities.MainActivity;
import com.android.runweather.models.WeatherVO;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.android.runweather.Utils.UiAutomatorUtils.assertViewWithTextIsVisible;
import static com.android.runweather.Utils.UiAutomatorUtils.denyCurrentPermission;
import static com.android.runweather.Utils.UiAutomatorUtils.denyCurrentPermissionPermanently;
import static com.android.runweather.Utils.UiAutomatorUtils.grantPermission;
import static com.android.runweather.Utils.UiAutomatorUtils.openPermissions;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainActivityInstrumentedTest {

    public static final WeatherVO[] WEATHER = new WeatherVO[]{
            new WeatherVO(LocalDateTime.now(),
                    "Description",
                    15.0,
                    17.2,
                    33,
                    10.3,
                    500)};


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity activity;
    private UiDevice device;

    @Before
    public void setup() {
        activity = rule.getActivity();
        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    //https://blog.egorand.me/testing-runtime-permissions-lessons-learned/
    @Test
    public void a_shouldDisplayPermissionRequestDialogAtStartup() throws Exception {
        assertViewWithTextIsVisible(device, UiAutomatorUtils.TEXT_ALLOW);
        assertViewWithTextIsVisible(device, UiAutomatorUtils.TEXT_DENY);

        // cleanup for the next test
        denyCurrentPermission(device);
    }


    @Test
    public void b_shouldDisplayShortRationaleIfPermissionWasDenied() throws Exception {
        denyCurrentPermission(device);

        onView(withText(R.string.permission_denied_rationale_short)).check(matches(isDisplayed()));
        onView(withText(R.string.grant_permission)).check(matches(isDisplayed()));
    }

    @Test
    public void c_shouldDisplayLongRationaleIfPermissionWasDeniedPermanently() throws Exception {
        denyCurrentPermissionPermanently(device);

        onView(withText(R.string.permission_denied_rationale_long)).check(matches(isDisplayed()));
        onView(withText(R.string.grant_permission)).check(matches(isDisplayed()));

        // will grant the permission for the next test
        onView(withText(R.string.grant_permission)).perform(click());
        openPermissions(device);
        grantPermission(device, "Contacts");
    }

    @Test
    public void d_shouldLoadWeatherIfPermissionWasGranted() throws Exception {
        for (WeatherVO weatherItem : WEATHER) {
            onView(withText(weatherItem.getId())).check(matches(isDisplayed()));
            //todo
        }
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