package com.example.android.bakingtime;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private String mQuantity;
    private String mMeasure;
    private String mIngredientName;

    public Ingredient (String quantity, String measure, String ingredientName) {
        mQuantity = quantity;
        mMeasure = measure;
        mIngredientName = ingredientName;
    }

    // Getters and setters

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    public String getIngredientName() {
        return mIngredientName;
    }

    public void setIngredientName(String ingredientName) {
        mIngredientName = ingredientName;
    }

    // Parcelable methods

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mQuantity);
        out.writeString(mMeasure);
        out.writeString(mIngredientName);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel in) {
        mQuantity = in.readString();
        mMeasure = in.readString();
        mIngredientName = in.readString();
    }
}
