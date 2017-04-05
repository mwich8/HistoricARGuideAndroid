package com.tum.historicarguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mati on 01.04.2017.
 */

class LocationDBHelper extends SQLiteOpenHelper {

    private static final String TAG = LocationDBHelper.class.getSimpleName();

    private static final String DB_NAME = "location.db";
    private static final int DB_VERSION = 1;

    // Change name to "munich"
    static final String TABLE_LOCATION = "location";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_LATITUDE = "latitude";
    static final String COLUMN_LONGITUDE = "longitude";
    static final String COLUMN_ORIENTATION = "orientation";
    static final String COLUMN_MIDDLEARCHWIDTH = "middlearchwidth";
    static final String COLUMN_MIDDLEARCHHEIGHT = "middlearchheight";
    static final String COLUMN_ZENITHX = "zenithx";
    static final String COLUMN_ZENITHY = "zenithy";
    static final String COLUMN_GATECLOSED = "gateclosed";

    private static final String SQL_CREATE =
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

    LocationDBHelper(Context context) {
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