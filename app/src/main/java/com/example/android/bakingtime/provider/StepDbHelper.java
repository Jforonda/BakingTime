package com.example.android.bakingtime.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakingtime.provider.StepContract.StepEntry;


public class StepDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stepsDb.db";

    private static final int DATABASE_VERSION = 1;

    public StepDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_STEP_TABLE = "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                StepEntry.COLUMN_STEP_ID + " TEXT NOT NULL," +
                StepEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL," +
                StepEntry.COLUMN_STEP_SHORT_DESCRIPTION + " TEXT NOT NULL," +
                StepEntry.COLUMN_STEP_DESCRIPTION + " TEXT NOT NULL," +
                StepEntry.COLUMN_STEP_VIDEO_URL + " TEXT NOT NULL," +
                StepEntry.COLUMN_STEP_THUMBNAIL_URL + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
