package com.jhlkdz.temperatremeasure.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    // Name of the database file
    private static final String DATABASE_NAME = "temperature.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_CHECK_INFO_TABLE =  "CREATE TABLE " + CheckInfoContract.CheckInfoEntry.TABLE_NAME + " ("
                + CheckInfoContract.CheckInfoEntry.cID + " INTEGER NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.ID + " INTEGER NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.TIME + " TIMESTAMP NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.OUT_TEMPERATURE + " REAL NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.OUT_HUMIDITY + " REAL NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.IN_TEMPERATURE + " REAL NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.IN_HUMIDITY + " REAL NOT NULL, "
                + CheckInfoContract.CheckInfoEntry.TEMPERATURE+ " TEXT NOT NULL );";

        String SQL_CREATE_BASE_INFO_TABLE =  "CREATE TABLE " + BaseInfoContract.BaseInfoEntry.TABLE_NAME + " ("
                + BaseInfoContract.BaseInfoEntry.ID + " INTEGER NOT NULL, "
                + BaseInfoContract.BaseInfoEntry.BARN_NUM + " INTEGER NOT NULL, "
                + BaseInfoContract.BaseInfoEntry.OUT_EXTENTION + " INTEGER NOT NULL, "
                + BaseInfoContract.BaseInfoEntry.OUT_ADDRESS + " INTEGER NOT NULL, "
                + BaseInfoContract.BaseInfoEntry.OUT_POINT + " INTEGER NOT NULL, "
                + BaseInfoContract.BaseInfoEntry.MAX_BYTES + " INTEGER NOT NULL );";

        String SQL_CREATE_BARN_INFO_TABLE =  "CREATE TABLE " + BarnInfoContract.BarnInfoEntry.TABLE_NAME + " ("
                +BarnInfoContract.BarnInfoEntry.bID + " INTEGER primary key AUTOINCREMENT, "
                + BarnInfoContract.BarnInfoEntry.ID + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.SYSTEM_TYPE + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.EXTENSION + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.ROW + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.COLUMN + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.LEVEL + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.START_COLUMN + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.B8 + " BLOB NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.B9 + " BLOB NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.IN_ADDRESS + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.IN_POINT + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.B12 + " BLOB NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.MAIN_ADDRESS + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.COLLECTOR_NUM + " INTEGER NOT NULL, "
                + BarnInfoContract.BarnInfoEntry.B15 + " BLOB NOT NULL );";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CHECK_INFO_TABLE);
        db.execSQL(SQL_CREATE_BASE_INFO_TABLE);
        db.execSQL(SQL_CREATE_BARN_INFO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }

}
