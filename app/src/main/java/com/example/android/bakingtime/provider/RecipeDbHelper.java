package com.example.android.bakingtime.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakingtime.provider.RecipeContract.RecipeEntry;

public class RecipeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bakingtime.db";

    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL," +
                RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL," +
                RecipeEntry.COLUMN_RECIPE_SERVINGS + " TEXT NOT NULL," +
                RecipeEntry.COLUMN_RECIPE_IMAGE + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
