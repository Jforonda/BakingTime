package com.example.android.bakingtime;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Recipe implements Parcelable {

    private String mRecipeID;
    private String mName;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<Step> mSteps;
    private String mServings;
    private String mImage;

    public Recipe(String recipeID, String name, ArrayList<Ingredient> ingredients,
                   ArrayList<Step> steps, String servings, String image) {
        mRecipeID = recipeID;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImage = image;
    }


    // Getters and Setters

    public String getRecipeID() {
        return mRecipeID;
    }

    public void setRecipeID(String id) {
        mRecipeID = id;
    }

    public String getRecipeName() {
        return mName;
    }

    public void setRecipeName(String name) {
        mName = name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public String getServings() {
        return mServings;
    }

    public void setServings(String servings) {
        mServings = servings;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    // Parcelable methods

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mRecipeID);
        out.writeString(mName);
        out.writeTypedList(mIngredients);
        out.writeTypedList(mSteps);
        out.writeString(mServings);
        out.writeString(mImage);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        mRecipeID = in.readString();
        mName = in.readString();
        mIngredients = in.createTypedArrayList(Ingredient.CREATOR);
        mSteps = in.createTypedArrayList(Step.CREATOR);
        mServings = in.readString();
        mImage = in.readString();
    }
}
