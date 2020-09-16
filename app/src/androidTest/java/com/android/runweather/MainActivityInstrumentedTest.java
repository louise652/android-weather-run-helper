package com.android.runweather;

import android.widget.Button;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.android.runweather.Utils.UiAutomatorUtils;
import com.android.runweather.activities.MainActivity;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.android.runweather.Utils.UiAutomatorUtils.allowCurrentPermission;
import static com.android.runweather.Utils.UiAutomatorUtils.assertViewWithTextIsVisible;
import static com.android.runweather.Utils.UiAutomatorUtils.denyCurrentPermission;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityInstrumentedTest {


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false, true);

    private UiDevice device;

    @Before
    public void setup() {

        this.device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    //https://blog.egorand.me/testing-runtime-permissions-lessons-learned/
    @Test
    public void a_shouldDisplayPermissionRequestDialogOnLoad() throws Exception {
        denyCurrentPermission(device);

        //then the location prompt will appear
        assertViewWithTextIsVisible(device, UiAutomatorUtils.TEXT_ALLOW);
        assertViewWithTextIsVisible(device, UiAutomatorUtils.TEXT_DENY);

        // cleanup for the next test
        denyCurrentPermission(device);
    }

    @Test
    public void b_mainComponentsRenderWhenPermissionAllowed() throws UiObjectNotFoundException {
        //given app has loaded

        // when permissions have been allowed
        allowCurrentPermission(device);

        //then components load
        onView(withId(R.id.manual_container)).check(matches(isDisplayed()));
        onView(withId(R.id.autocomplete_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_weather)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_weather)).check(matches(CoreMatchers.instanceOf(Button.class)));
    }

    @Test
    public void c_mainComponentsRenderWhenPermissionDenied() throws UiObjectNotFoundException {
        //given app has loaded

        // when permissions have been denied
        denyCurrentPermission(device);

        //then components load
        onView(withId(R.id.manual_container)).check(matches(isDisplayed()));
        onView(withId(R.id.autocomplete_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_weather)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_weather)).check(matches(CoreMatchers.instanceOf(Button.class)));
    }

}