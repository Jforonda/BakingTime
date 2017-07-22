package com.example.android.bakingtime.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.bakingtime.provider.IngredientContract.IngredientEntry;

public class IngredientContentProvider extends ContentProvider {

    public static final int INGREDIENT = 100;
    public static final int INGREDIENT_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = builderUriMatcher();

    public static UriMatcher builderUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS,
                INGREDIENT);
        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS + "/#",
                INGREDIENT_WITH_ID);
        return uriMatcher;
    }

    private IngredientDbHelper mIngredientDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mIngredientDbHelper = new IngredientDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mIngredientDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case INGREDIENT:
                long id = db.insert(IngredientEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(IngredientEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mIngredientDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case INGREDIENT:
                retCursor = db.query(IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = IngredientEntry.TABLE_NAME + "." + IngredientEntry.COLUMN_RECIPE_ID
                        + " = " + id;
                retCursor = db.query(IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mIngredientDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int recipesDeleted;
        switch (match) {
            case INGREDIENT:
                recipesDeleted = db.delete(IngredientEntry.TABLE_NAME, null, null);
                break;
            case INGREDIENT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                recipesDeleted = db.delete(IngredientEntry.TABLE_NAME, "recipeId=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (recipesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return recipesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mIngredientDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int recipesUpdated;

        switch (match) {
            case INGREDIENT:
                recipesUpdated = db.update(IngredientEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (recipesUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return recipesUpdated;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
