package com.tum.historicarguide;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Mati on 03.04.2017.
 */

public class DetailActivity extends Activity {

    private ListView locationsListView;
    private TextView locationsHeaderView;
    private ArrayAdapter arrayAdapter;
    private DetailAdapter detailAdapter;
    private DetailModel[] details;

    private String[] locationsDetails = new String[]{
            "Latitude ",
            "Longitude ",
            "Orientation ",
            "Middle Arch Width ",
            "Middle Arch Height ",
            "Zenith X ",
            "Zenith Y ",
            "Gate Closed "
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Fix Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locationsListView = (ListView) findViewById(R.id.locationsDetailsListView);
        locationsHeaderView = (TextView) findViewById(R.id.detailHeaderView);
        // Enable the back button on the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Location retrievedLocation = (Location) getIntent().getSerializableExtra("Location");

        if (locationsHeaderView != null) {
            locationsHeaderView.setText(retrievedLocation.getName());
        }

        String[] values = detailsToStringArray(retrievedLocation);

        details = generateDetails(locationsDetails, values);

        detailAdapter = new DetailAdapter(getApplicationContext(), R.layout.simple_row, details);

        // addLocationDetails(retrievedLocation);

        // arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locationsDetails);

        if (locationsListView != null) {
            locationsListView.setAdapter(detailAdapter);
        }
    }

    private void addLocationDetails(Location location) {

        String[] customDetails = detailsToStringArray(location);

        for (int i = 0; i < locationsDetails.length; i++) {
            locationsDetails[i] += customDetails[i];
            // locationsDetails[i] =  locationsDetails[i] + " s";
        }
    }

    private String[] detailsToStringArray(Location location) {
        String[] stringArray = new String[8];
        stringArray[0] = Float.toString(location.getLatitude());
        stringArray[1] = Float.toString(location.getLongitude());
        stringArray[2] = Float.toString(location.getOrientation());
        stringArray[3] = Float.toString(location.getMiddleArchWidth());
        stringArray[4] = Float.toString(location.getMiddleArchHeight());
        stringArray[5] = Float.toString(location.getZenithX());
        stringArray[6] = Float.toString(location.getZenithY());
        if (location.isGateClosed()) {
            stringArray[7] = "Yes";
        } else {
            stringArray[7] = "No";
        }
        return stringArray;
    }

    private DetailModel[] generateDetails(String[] keys, String[] values) {
        if (keys.length != values.length) {
            DetailModel detail = new DetailModel("KEY", "VALUE");
            DetailModel[] details = new DetailModel[1];
            details[0] = detail;
            return details;
        }
        DetailModel[] details = new DetailModel[keys.length];
        for(int i = 0; i < keys.length; i++) {
            details[i] = new DetailModel(keys[i], values[i]);
        }
        return details;
    }
}
