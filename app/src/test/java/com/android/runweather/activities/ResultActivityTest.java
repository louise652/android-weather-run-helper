package com.android.runweather.activities;

import org.junit.Test;

public class ResultActivityTest {

    @Test
    void test_mockObject() {
        //Given an example payload
        //When page loads
        //Then payload gets decanted into an object
    }

    @Test
    void test_bestTimeToRun() {
        //Given a successful payload
        //When page loads
        //A time recommendation displays based on rain and temp
    }

    @Test
    void test_outfitRecommendation() {
        //Given a successful payload
        //When page loads
        //An outfit recommendation displays based on rain and temp
    }

    @Test
    void test_error() {
        //Given an unsuccessful payload
        //When page loads
        //An error toast displays
    }

    @Test
    void test_missing_data() {
        //Given a successful payload with missing attributes
        //When page loads
        //Recommendations appear with caveats
    }

}