package com.tum.historicarguide;

import android.app.ActionBar;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Mati on 01.04.2017.
 */

public class LocationsActivity extends Activity{

    private static final int DETAIL_REQUEST = 1;

    private static final String TAG = LocationsActivity.class.getSimpleName();

    private ListView locationsListView;
    private Location[] locationsArray;
    private LocationsAdapter locationsAdapter;
    private LocationDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_list);
        // Fix Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locationsListView = (ListView) findViewById(R.id.locationsListView);
        // Enable the back button on the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dataSource = new LocationDataSource(this);
        Log.d(TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        Log.d(TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        databaseEntriesToListView();

        Log.d(TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();

        locationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Take the location and run to the detailedView
                Intent i = new Intent(view.getContext(), DetailActivity.class);
                i.putExtra("Location", locationsArray[position]);
                startActivityForResult(i, DETAIL_REQUEST);
            }
        });
    }

    private void databaseEntriesToListView () {
        List<Location> locationList = dataSource.getAllLocations();
        locationsArray = locationList.toArray(new Location[locationList.size()]);

        locationsAdapter = new LocationsAdapter(getApplicationContext(), R.layout.row, locationsArray);

        if (locationsListView != null) {
            locationsListView.setAdapter(locationsAdapter);
        }
    }
}
