package com.android.runweather.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.runweather.BuildConfig;
import com.android.runweather.R;
import com.google.android.material.button.MaterialButton;

public class HelpActivity extends AppCompatActivity {

    public static final String JOG_ON_FEEDBACK = "Jog On feedback";
    public static final String CHOOSE_AN_EMAIL_CLIENT = "Choose an Email client";
    public static final String PLAIN_TEXT = "plain/text";
    private static final String EMAIL_ADDRESS = BuildConfig.EMAIL;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;

            case R.id.menu_email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADDRESS});
                email.putExtra(Intent.EXTRA_SUBJECT, JOG_ON_FEEDBACK);
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType(PLAIN_TEXT);
                startActivity(Intent.createChooser(email, CHOOSE_AN_EMAIL_CLIENT));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
