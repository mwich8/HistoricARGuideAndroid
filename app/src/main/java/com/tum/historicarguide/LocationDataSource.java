package com.tum.historicarguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mati on 01.04.2017.
 */

public class LocationDataSource {

    private static final String TAG = LocationDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private LocationDBHelper dbHelper;

    private String[] columns = {
            LocationDBHelper.COLUMN_ID,
            LocationDBHelper.COLUMN_NAME,
            LocationDBHelper.COLUMN_LATITUDE,
            LocationDBHelper.COLUMN_LONGITUDE,
            LocationDBHelper.COLUMN_ORIENTATION,
            LocationDBHelper.COLUMN_MIDDLEARCHWIDTH,
            LocationDBHelper.COLUMN_MIDDLEARCHHEIGHT,
            LocationDBHelper.COLUMN_ZENITHX,
            LocationDBHelper.COLUMN_ZENITHY,
            LocationDBHelper.COLUMN_GATECLOSED,
    };

    public LocationDataSource(Context context) {
        Log.d(TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new LocationDBHelper(context);
    }

    public void open() {
        Log.d(TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Location createLocation(String name, float latitude, float longitude, float orientation, float middleArchWidth, float middleArchHeight, float zenithX, float zenithY, boolean gateClosed) {
        ContentValues values = new ContentValues();
        values.put(LocationDBHelper.COLUMN_NAME, name);
        values.put(LocationDBHelper.COLUMN_LATITUDE, latitude);
        values.put(LocationDBHelper.COLUMN_LONGITUDE, latitude);
        values.put(LocationDBHelper.COLUMN_ORIENTATION, latitude);
        values.put(LocationDBHelper.COLUMN_MIDDLEARCHWIDTH, latitude);
        values.put(LocationDBHelper.COLUMN_MIDDLEARCHHEIGHT, latitude);
        values.put(LocationDBHelper.COLUMN_ZENITHX, latitude);
        values.put(LocationDBHelper.COLUMN_ZENITHY, latitude);
        if(gateClosed) {
            values.put(LocationDBHelper.COLUMN_GATECLOSED, 1);
        } else {
            values.put(LocationDBHelper.COLUMN_GATECLOSED, 0);
        }

        // Insert dataset in table
        long insertId = database.insert(LocationDBHelper.TABLE_LOCATION, null, values);

        Cursor cursor = database.query(LocationDBHelper.TABLE_LOCATION,
                columns, LocationDBHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Location location = cursorToLocation(cursor);
        cursor.close();

        return location;
    }

    private Location cursorToLocation(Cursor cursor) {
        // Get the Index of each column w.r.t. the defined column name
        int idIndex = cursor.getColumnIndex(LocationDBHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(LocationDBHelper.COLUMN_NAME);
        int idLatitude = cursor.getColumnIndex(LocationDBHelper.COLUMN_LATITUDE);
        int idLongitude = cursor.getColumnIndex(LocationDBHelper.COLUMN_LONGITUDE);
        int idOrientation = cursor.getColumnIndex(LocationDBHelper.COLUMN_ORIENTATION);
        int idMiddleArchWidth = cursor.getColumnIndex(LocationDBHelper.COLUMN_MIDDLEARCHWIDTH);
        int idMiddleArchHeight = cursor.getColumnIndex(LocationDBHelper.COLUMN_MIDDLEARCHHEIGHT);
        int idZenithX = cursor.getColumnIndex(LocationDBHelper.COLUMN_ZENITHX);
        int idZenithY = cursor.getColumnIndex(LocationDBHelper.COLUMN_ZENITHY);
        int idGateClosed = cursor.getColumnIndex(LocationDBHelper.COLUMN_GATECLOSED);

        // Get the actual data on the current cursor position
        long id = cursor.getLong(idIndex);
        String name = cursor.getString(idName);
        float latitude = cursor.getFloat(idLatitude);
        float longitude = cursor.getFloat(idLongitude);
        float orientation = cursor.getFloat(idOrientation);
        float middleArchWidth = cursor.getFloat(idMiddleArchWidth);
        float middleArchHeight = cursor.getFloat(idMiddleArchHeight);
        float zenithX = cursor.getFloat(idZenithX);
        float zenithY = cursor.getFloat(idZenithY);
        boolean gateClosed;
        if (cursor.getInt(idGateClosed) == 1) {
            gateClosed = true;
        } else {
            gateClosed = false;
        }

        Location location = new Location(id, name, latitude, longitude, orientation, middleArchWidth, middleArchHeight, zenithX, zenithY, gateClosed);
        return location;
    }

    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<>();

        Cursor cursor = database.query(LocationDBHelper.TABLE_LOCATION,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Location location;

        while(!cursor.isAfterLast()) {
            location = cursorToLocation(cursor);
            locationList.add(location);
            Log.d(TAG, location.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return locationList;
    }
}
