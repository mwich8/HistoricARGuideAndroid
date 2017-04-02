package com.tum.historicarguide;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Mati on 01.04.2017.
 */

public class LocationsActivity extends Activity {

    public static final String TAG = LocationsActivity.class.getSimpleName();

    private ListView locationsListView;
    private ArrayAdapter locationsArrayAdapter;
    private LocationsAdapter locationsAdapter;
    private LocationDataSource dataSource;

    String[] testArray = new String[]{
            "Fussball",
            "Tennis",
            "Basketball",
            "Baseball",
            "Handball",
            "Curling",
            "Reiten",
            "Schwimmen"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);

        locationsListView = (ListView) findViewById(R.id.locationsListView);
        // Enable the back button on the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dataSource = new LocationDataSource(this);
        Log.d(TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.d(TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        showAllListEntries();

        Log.d(TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }

    private void showAllListEntries () {
        List<Location> locationList = dataSource.getAllLocations();
        Location[] locationsArray = locationList.toArray(new Location[locationList.size()]);

        locationsAdapter = new LocationsAdapter(getApplicationContext(), R.layout.row, locationsArray);

        if (locationsListView != null) {
            locationsListView.setAdapter(locationsAdapter);
        }
    }
}
