package com.example.android.bakingtime.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import com.example.android.bakingtime.Ingredient;
import com.example.android.bakingtime.Recipe;
import com.example.android.bakingtime.Step;

public class OpenRecipeJsonUtils {

    public static ArrayList<Recipe> getRecipeListFromJson(Context context, String recipeJsonString)
            throws JSONException, IOException {

        ArrayList<Recipe> parsedRecipeData = new ArrayList<>();

        JSONArray recipeListArray = new JSONArray(recipeJsonString);

        for (int i = 0; i < recipeListArray.length(); i++) {
            JSONObject jsonObject = recipeListArray.getJSONObject(i);
            String tempID = jsonObject.getString("id");
            String tempName = jsonObject.getString("name");
            ArrayList<Ingredient> tempIngredients = getIngredientsListFromJson(jsonObject);
            ArrayList<Step> tempSteps = getStepsListFromJson(jsonObject);
            String tempServing = jsonObject.getString("servings");
            String tempImage = jsonObject.getString("image");

            Recipe tempRecipe = new Recipe(tempID, tempName, tempIngredients, tempSteps,
                    tempServing, tempImage);

            parsedRecipeData.add(tempRecipe);
        }

        return parsedRecipeData;
    }

    static ArrayList<Ingredient> getIngredientsListFromJson(JSONObject jsonObject) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("ingredients");
            for (int i = 0; i < jsonArray.length(); i++) {
                Ingredient tempIngredient;
                JSONObject ingredientJsonObject = jsonArray.getJSONObject(i);
                String tempQuantity = ingredientJsonObject.getString("quantity");
                String tempMeasure = ingredientJsonObject.getString("measure");
                String tempIngredientName = ingredientJsonObject.getString("ingredient");
                tempIngredient = new Ingredient(tempQuantity, tempMeasure, tempIngredientName);
                ingredients.add(tempIngredient);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return ingredients;
    }

    static ArrayList<Step> getStepsListFromJson(JSONObject jsonObject) {
        ArrayList<Step> steps = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("steps");
            for (int i = 0; i < jsonArray.length(); i++) {
                Step tempStep;
                JSONObject stepJsonObject = jsonArray.getJSONObject(i);
                String tempID = stepJsonObject.getString("id");
                String tempShortDescription = stepJsonObject.getString("shortDescription");
                String tempDescription = stepJsonObject.getString("description");
                String tempVideoUrl = stepJsonObject.getString("videoURL");
                String tempThumbnailUrl = stepJsonObject.getString("thumbnailURL");
                tempStep = new Step(tempID, tempShortDescription, tempDescription, tempVideoUrl,
                        tempThumbnailUrl);
                steps.add(tempStep);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return steps;
    }
}
