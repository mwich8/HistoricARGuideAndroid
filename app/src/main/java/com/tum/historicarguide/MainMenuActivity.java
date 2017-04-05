package com.tum.historicarguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mati on 31.03.2017.
 */

public class MainMenuActivity extends Activity {

    public static final String TAG = MainMenuActivity.class.getSimpleName();

    private LocationDataSource dataSource;

    private static final String TIMES_APP_WAS_STARTED_KEY = "TIMES_APP_WAS_STARTED_KEY";

    ImageButton mainActivityButton = null;
    ImageButton mapActivityButton = null;
    ImageButton locationsActivityButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_tiles);
        // Fix Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUpDatabase();

        // Register Button to change to the main activity
        mainActivityButton = (ImageButton) findViewById(R.id.augmentViewImageButton);
        mainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Clicked on AUGMENT VIEW Button");
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // Register Button to change to the locations activity
        mapActivityButton = (ImageButton) findViewById(R.id.mapImageButton);
        mapActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Clicked on LOCATE HISTORIC PLACES Button");
                Intent i = new Intent(view.getContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        // Register Button to change to the locations activity
        locationsActivityButton = (ImageButton) findViewById(R.id.listAllLocationsImageButton);
        locationsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Clicked on LIST ALL LOCATIONS Button");
                Intent i = new Intent(view.getContext(), LocationsActivity.class);
                startActivity(i);
            }
        });
    }

    private void setUpDatabase() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // read number from shared Preferences
        int defaultValue = 0;
        int timesAppWasStarted = sharedPreferences.getInt(TIMES_APP_WAS_STARTED_KEY, defaultValue);

        // Initialization of database if it is empty
        if (timesAppWasStarted == 0) {
            dataSource = new LocationDataSource(this);
            Log.d(TAG, "Die Datenquelle wird geöffnet.");
            dataSource.open();

            Log.d(TAG, "Die Datenquelle wird gefüllt.");
            fillDatabase();

            Log.d(TAG, "Die Datenquelle wird geschlossen.");
            dataSource.close();
        }
        timesAppWasStarted++;

        // Write new number to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TIMES_APP_WAS_STARTED_KEY, timesAppWasStarted);
        editor.apply();
    }

    // Fills the database
    private void fillDatabase() {
        Location locationSiegestor = dataSource.createLocation("Siegestor", 48.151833f, 11.581906f, 8.0f, 125.0f, 54.0f, 0.5f, 265.0f / 546.0f, false);
        Log.d(TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(TAG, locationSiegestor.toString());
        Location locationFeldherrnhalle = dataSource.createLocation("Feldherrnhalle", 48.142537f, 11.577571f, 185.0f, 106.4f, 46.0f, 0.5f, 84.0f / 363.0f, true);
        Log.d(TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(TAG, locationFeldherrnhalle.toString());
    }
}
