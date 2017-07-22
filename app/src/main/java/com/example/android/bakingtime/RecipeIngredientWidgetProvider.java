package com.example.android.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.bakingtime.provider.IngredientContract;
import com.example.android.bakingtime.provider.RecipeContract;
import com.example.android.bakingtime.provider.StepContract;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientWidgetProvider extends AppWidgetProvider {

    static int currentRecipePosition = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String widgetRecipeId = WidgetRecipeAdapter.loadRecipePref(context, appWidgetId);

        if (!widgetRecipeId.equals("empty")) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_widget_provider);

            // Initialize Recipe list and set title
            Recipe recipeToDisplay = getRecipeById(context, widgetRecipeId);
            views.setTextViewText(R.id.widget_text_view_recipe_name, recipeToDisplay.getRecipeName());

            // Adapter for Ingredient list view
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra("recipeId", recipeToDisplay.getRecipeID());
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            views.setRemoteAdapter(R.id.widget_list_view_ingredients, intent);

            // Click handler to open app
            Intent recipeAppIntent = new Intent(context, RecipeDetailActivity.class);
            recipeAppIntent.putExtra("recipe", recipeToDisplay);
            PendingIntent recipeAppPendingIntent = PendingIntent.getActivity(context, 0, recipeAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_text_view_recipe_name, recipeAppPendingIntent);


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int length = appWidgetIds.length;
        for (int i = 0; i < length; i++) {
            WidgetRecipeAdapter.deleteRecipePref(context, appWidgetIds[i]);
        }
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

    public static Recipe getRecipeById(Context context, String recipeId) {
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(recipeId).build();
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);

        c.moveToFirst();
        Recipe recipe = new Recipe(
                c.getString(c.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID)),
                c.getString(c.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME)),
                getIngredients(context, recipeId),
                getSteps(context, recipeId),
                c.getString(c.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS)),
                c.getString(c.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE)));
        return recipe;
    }

}

