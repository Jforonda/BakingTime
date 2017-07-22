package com.example.android.bakingtime;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingtime.provider.IngredientContract;
import com.example.android.bakingtime.provider.RecipeContract;
import com.example.android.bakingtime.provider.StepContract;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class WidgetRecipeConfigure extends Activity {

    private WidgetRecipeAdapter mWidgetRecipeAdapter;
    private RecyclerView mRecyclerView;
    private TextView mNextTextView;

    private ArrayList<Recipe> recipes;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public WidgetRecipeConfigure() {

    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.recipe_widget_configure);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        mRecyclerView = ButterKnife.findById(this, R.id.recycler_view_recipe_widget_configure);
        if (getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (getResources().getBoolean(R.bool.isTablet)) {
                // LANDSCAPE | Tablet - Grid Layout, 3 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                // LANDSCAPE | Phone - Grid Layout, 2 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }
        } else {

            if (getResources().getBoolean(R.bool.isTablet)) {
                // PORTRAIT | Tablet - Grid Layout, 2 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                // PORTRAIT | PHONE - 1 Column
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
        recipes = getRecipes(this);
        mWidgetRecipeAdapter = new WidgetRecipeAdapter(this, recipes, mAppWidgetId);
        mRecyclerView.setAdapter(mWidgetRecipeAdapter);

        mNextTextView = ButterKnife.findById(this, R.id.text_view_recipe_widget_configure);
        mNextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update widget view
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
                RecipeIngredientWidgetProvider.updateAppWidget(getBaseContext(), appWidgetManager,
                        mAppWidgetId);
                // Return to homescreen with Widget configured
                Intent recipeForWidget = new Intent();
                recipeForWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, recipeForWidget);
                finish();
            }
        });

        Toast.makeText(this, "Select which recipe to display as a home screen widget " +
                        "then press next.", Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<Recipe> getRecipes(Context context) {
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        ArrayList<Recipe> recipes = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            String tempId = c.getString(c.getColumnIndex(RecipeContract
                    .RecipeEntry.COLUMN_RECIPE_ID));
            String tempName = c.getString(c.getColumnIndex(RecipeContract
                    .RecipeEntry.COLUMN_RECIPE_NAME));
            ArrayList<Ingredient> tempIngredients = getIngredients(context, tempId);
            ArrayList<Step> tempSteps = getSteps(context, tempId);
            String tempServings = c.getString(c.getColumnIndex(RecipeContract
                    .RecipeEntry.COLUMN_RECIPE_SERVINGS));
            String tempImage = c.getString(c.getColumnIndex(RecipeContract
                    .RecipeEntry.COLUMN_RECIPE_IMAGE));
            Recipe tempRecipe = new Recipe(tempId, tempName, tempIngredients, tempSteps,
                    tempServings, tempImage);
            recipes.add(tempRecipe);
            c.moveToNext();

        }
        return recipes;
    }

    public static ArrayList<Ingredient> getIngredients(Context context, String recipeId) {
        Uri uri = IngredientContract.IngredientEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(recipeId).build();
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            String tempQuantity = c.getString(c.getColumnIndex(IngredientContract
                    .IngredientEntry.COLUMN_INGREDIENT_QUANTITY));
            String tempMeasure = c.getString(c.getColumnIndex(IngredientContract
                    .IngredientEntry.COLUMN_INGREDIENT_MEASURE));
            String tempName = c.getString(c.getColumnIndex(IngredientContract
                    .IngredientEntry.COLUMN_INGREDIENT_NAME));
            Ingredient tempIngredient = new Ingredient(tempQuantity, tempMeasure, tempName);
            ingredients.add(tempIngredient);
            c.moveToNext();
        }

        return ingredients;
    }

    public static ArrayList<Step> getSteps(Context context, String recipeId) {
        Uri uri = StepContract.StepEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(recipeId).build();
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        ArrayList<Step> steps = new ArrayList<>();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            String tempId = c.getString(c.getColumnIndex(StepContract.StepEntry
                    .COLUMN_STEP_ID));
            String tempShortDescription = c.getString(c.getColumnIndex(StepContract
                    .StepEntry.COLUMN_STEP_SHORT_DESCRIPTION));
            String tempDescription = c.getString(c.getColumnIndex(StepContract
                    .StepEntry.COLUMN_STEP_DESCRIPTION));
            String tempVideoUrl = c.getString(c.getColumnIndex(StepContract
                    .StepEntry.COLUMN_STEP_VIDEO_URL));
            String tempThumbnailUrl = c.getString(c.getColumnIndex(StepContract
                    .StepEntry.COLUMN_STEP_THUMBNAIL_URL));
            Step tempStep = new Step(tempId, tempShortDescription, tempDescription,
                    tempVideoUrl, tempThumbnailUrl);
            steps.add(tempStep);
            c.moveToNext();
        }

        return steps;
    }
}
