package com.tum.historicarguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

/**
 * Created by Mati on 31.03.2017.
 */

public class MainMenuActivity extends Activity {

    public static final String TAG = MainMenuActivity.class.getSimpleName();

    private LocationDataSource dataSource;

    Button mainActivityButton = null;
    Button locationsActivityButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        dataSource = new LocationDataSource(this);
        Log.d(TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        // Location location = new Location(1,"Siegestor", 20.0f, 20.0f, 45.0f, 120.0f, 40.0f, 150.0f, 150.0f, false);
        Location location = dataSource.createLocation("Feldherrnhalle", 40.0f, 40.0f, 135.0f, 140.0f, 60.0f, 120.0f, 130.0f, true);
        Log.d(TAG, "Es wurde der folgende Eintrag in die Datenbank geschrieben:");
        Log.d(TAG, location.toString());

        Log.d(TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();

        Log.d(TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();

        // Register Button to change to the main activity
        mainActivityButton = (Button) findViewById(R.id.augmentViewButton);
        mainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Clicked on AUGMENT VIEW Button");
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        // Register Button to change to the locations activity
        locationsActivityButton = (Button) findViewById(R.id.listAllLocationsButton);
        locationsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Clicked on LIST ALL LOCATIONS Button");
                Intent i = new Intent(view.getContext(), LocationsActivity.class);
                startActivity(i);
            }
        });
    }

    private void showAllListEntries () {
        List<Location> locationList = dataSource.getAllLocations();

        /*
        ArrayAdapter<Location> locationArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                shoppingMemoList);
        */

        // Currently no listview available ==> Do this in other activity
        // ListView locationsListView = (ListView) findViewById(R.id.listview_locations);
        // locationsListView.setAdapter(locationArrayAdapter);
    }
}
