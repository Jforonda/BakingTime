package com.example.android.bakingtime.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientContract {
    public static final String AUTHORITY = "com.example.android.bakingtime.provider" +
            ".IngredientContract";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_INGREDIENTS = "ingredients";

    public static final long INVALID_STEP_ID = -1;

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_INGREDIENT_QUANTITY = "quantity";
        public static final String COLUMN_INGREDIENT_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT_NAME = "name";
    }
}
