package com.android.runweather.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.runweather.R;
import com.google.android.material.button.MaterialButton;

public class HelpActivity extends MenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        setupExpandableTextViews(R.id.aboutText, R.id.aboutBtn);
        setupExpandableTextViews(R.id.howText, R.id.howBtn);
        setupExpandableTextViews(R.id.timeText, R.id.timeBtn);
        setupExpandableTextViews(R.id.weatherText, R.id.weatherBtn);
        setupExpandableTextViews(R.id.contactText, R.id.contactBtn);
    }

    private void setupExpandableTextViews(int textID, int buttonID) {
        TextView text = findViewById(textID);
        MaterialButton button = findViewById(buttonID);

        button.setOnClickListener(v -> {
            if (text.getVisibility() == View.VISIBLE) {
                text.setVisibility(View.GONE);
                button.setIconResource(R.drawable.expand);
            } else {
                text.setVisibility(View.VISIBLE);
                button.setIconResource(R.drawable.collapse);
            }
        });
    }

}
