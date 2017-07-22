package com.example.android.bakingtime.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bakingtime.provider.IngredientContract.IngredientEntry;

public class IngredientDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredientsDb.db";

    private static final int DATABASE_VERSION = 1;

    public IngredientDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL," +
                IngredientEntry.COLUMN_INGREDIENT_QUANTITY + " TEXT NOT NULL," +
                IngredientEntry.COLUMN_INGREDIENT_MEASURE + " TEXT NOT NULL," +
                IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
