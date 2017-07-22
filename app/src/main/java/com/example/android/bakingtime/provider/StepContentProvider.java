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

import com.example.android.bakingtime.provider.StepContract.StepEntry;

public class StepContentProvider extends ContentProvider {

    public static final int STEP = 100;
    public static final int STEP_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = builderUriMatcher();

    public static UriMatcher builderUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(StepContract.AUTHORITY, StepContract.PATH_STEPS, STEP);
        uriMatcher.addURI(StepContract.AUTHORITY, StepContract.PATH_STEPS + "/#",
                STEP_WITH_ID);
        return uriMatcher;
    }

    private StepDbHelper mStepDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mStepDbHelper = new StepDbHelper(context);
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mStepDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case STEP:
                long id = db.insert(StepEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(StepEntry.CONTENT_URI, id);
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

        final SQLiteDatabase db = mStepDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case STEP:
                retCursor = db.query(StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case STEP_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = StepEntry.TABLE_NAME + "." + StepEntry.COLUMN_RECIPE_ID
                        + " = " + id;
                retCursor = db.query(StepEntry.TABLE_NAME,
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

        final SQLiteDatabase db = mStepDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int recipesDeleted;
        switch (match) {
            case STEP:
                recipesDeleted = db.delete(StepEntry.TABLE_NAME, null, null);
                break;
            case STEP_WITH_ID:
                String id = uri.getPathSegments().get(1);
                recipesDeleted = db.delete(StepEntry.TABLE_NAME, "recipeId=?", new String[]{id});
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
        final SQLiteDatabase db = mStepDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int recipesUpdated;

        switch (match) {
            case STEP:
                recipesUpdated = db.update(StepEntry.TABLE_NAME, values, selection, selectionArgs);
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
