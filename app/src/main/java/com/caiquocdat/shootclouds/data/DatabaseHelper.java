package com.caiquocdat.shootclouds.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "scoreDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCORE = "scoreTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RANK = "rank";
    public static final String COLUMN_POINT = "point";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SCORE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RANK + " INTEGER, " +
                    COLUMN_POINT + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }
}
