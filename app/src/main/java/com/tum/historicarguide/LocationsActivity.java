package com.tum.historicarguide;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Mati on 01.04.2017.
 */

public class LocationsActivity extends Activity {

    private ListView locationsListView;
    private ArrayAdapter locationsArrayAdapter;

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
        // Enable the back button on the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        locationsListView = (ListView) findViewById(R.id.locationsListView);

        locationsArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, testArray);

        if (locationsListView != null) {
            locationsListView.setAdapter(locationsArrayAdapter);
        }
    }
}
