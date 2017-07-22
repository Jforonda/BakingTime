package com.example.android.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.provider.IngredientContract;

import java.util.ArrayList;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private Context mContext;
    private String mRecipeId;
    private Cursor mCursor;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        mContext = context;
        mRecipeId = intent.getStringExtra("recipeId");
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Uri ingredientUri = IngredientContract.IngredientEntry.CONTENT_URI;
        ingredientUri = ingredientUri.buildUpon().appendPath(mRecipeId).build();
        if (mCursor != null) {
            mCursor.close();
        }
        final long token = Binder.clearCallingIdentity();
        try {
            mCursor = mContext.getContentResolver().query(
                    ingredientUri,
                    null,
                    null,
                    null,
                    null
            );
        } finally {
            Binder.restoreCallingIdentity(token);
        }
        mCursor.moveToFirst();
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }


    @Override
    public int getCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) {
            return null;
        }
        mCursor.moveToPosition(position);
        String ingredientQuantity = mCursor.getString(mCursor.getColumnIndex(IngredientContract
                .IngredientEntry.COLUMN_INGREDIENT_QUANTITY));
        String ingredientMeasure = mCursor.getString(mCursor.getColumnIndex(IngredientContract
                .IngredientEntry.COLUMN_INGREDIENT_MEASURE));
        String ingredientName = mCursor.getString(mCursor.getColumnIndex(IngredientContract
                .IngredientEntry.COLUMN_INGREDIENT_NAME));
        String displayIngredientText = ingredientQuantity + " " + ingredientMeasure + " - "
                + ingredientName;
        RemoteViews remoteViews = new RemoteViews(
                mContext.getPackageName(), R.layout.recipe_detail_ingredients_item);
        remoteViews.setTextViewText(R.id.text_view_recipe_detail_ingredient,
                displayIngredientText);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
