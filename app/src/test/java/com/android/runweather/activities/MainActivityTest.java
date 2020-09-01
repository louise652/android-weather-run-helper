package com.android.runweather.activities;

import org.junit.Test;

public class MainActivityTest {

    @Test
    void test_mainComponentsRender(){
        //Given app is opened
        //When page loads
        //Then button renders
    }


    @Test
    void test_locationPrompt(){
        //Given app is loaded
        //When button clicked and location permission is not approved
        //Then location prompt appears
    }


    @Test
    void test_locationApproved(){
        //Given button is clicked
        //When app asks for location permission and user agrees
        //Then result activity launches
    }


    @Test
    void test_locationDenied(){
        //Given button is clicked
        //When app asks for location permission and user doesn't agree
        //Then page loads with manual option to type location
    }

    @Test
    void test_manualLocationValid(){
        //Given button is clicked and user doesn't agree to location permission
        //When the user enters in a valid manual location
        //Then result activity launches
    }

    @Test
    void test_manualLocationInvalid(){
        //Given button is clicked and user doesn't agree to location permission
        //When the user enters in a invalid manual location
        //An error toast appears
    }

    @Test
    void test_help(){
        //Given app is loaded
        //When button help icon is clicked
        //Then help text appears
    }

}