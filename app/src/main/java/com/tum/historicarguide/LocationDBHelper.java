package com.tum.historicarguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mati on 01.04.2017.
 */

public class LocationDBHelper extends SQLiteOpenHelper {

    private static final String TAG = LocationDBHelper.class.getSimpleName();

    public static final String DB_NAME = "location.db";
    public static final int DB_VERSION = 1;

    // Change name to "munich"
    public static final String TABLE_LOCATION = "location";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ORIENTATION = "orientation";
    public static final String COLUMN_MIDDLEARCHWIDTH = "middlearchwidth";
    public static final String COLUMN_MIDDLEARCHHEIGHT = "middlearchheight";
    public static final String COLUMN_ZENITHX = "zenithx";
    public static final String COLUMN_ZENITHY = "zenithy";
    public static final String COLUMN_GATECLOSED = "gateclosed";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_LOCATION +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_LATITUDE + " REAL NOT NULL, " +
                    COLUMN_LONGITUDE + " REAL NOT NULL, " +
                    COLUMN_ORIENTATION + " REAL NOT NULL, " +
                    COLUMN_MIDDLEARCHWIDTH + " REAL NOT NULL, " +
                    COLUMN_MIDDLEARCHHEIGHT + " REAL NOT NULL, " +
                    COLUMN_ZENITHX + " REAL NOT NULL, " +
                    COLUMN_ZENITHY + " REAL NOT NULL, " +
                    COLUMN_GATECLOSED + " INTEGER NOT NULL);";

    public LocationDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            Log.e(TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}