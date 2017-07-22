package com.example.android.bakingtime;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingtime.provider.IngredientContract;
import com.example.android.bakingtime.provider.RecipeContract;
import com.example.android.bakingtime.provider.StepContract;
import com.example.android.bakingtime.utilities.NetworkUtils;
import com.example.android.bakingtime.utilities.OpenRecipeJsonUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {

    private RecipeListAdapter mRecipeListAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mTextViewNoRecipes;
    private TextView mTextViewErrorMessage;

    private final String RECIPE_RECYCLER_STATE = "recipe_recycler_state";
    private static Bundle mBundleRecyclerViewState;

    public RecipeListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        mRecyclerView = ButterKnife.findById(rootView, R.id.recycler_view_recipe);
        mProgressBar = ButterKnife.findById(rootView, R.id.pb_loading_indicator);
        mTextViewNoRecipes = ButterKnife.findById(rootView, R.id.tv_no_recipes);
        mTextViewErrorMessage = ButterKnife.findById(rootView, R.id.tv_error_message);

        if (this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (rootView.getResources().getBoolean(R.bool.isTablet)) {
                // LANDSCAPE | Tablet - Grid Layout, 3 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                // LANDSCAPE | Phone - Grid Layout, 2 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
            }
        } else {

            if (rootView.getResources().getBoolean(R.bool.isTablet)) {
                // PORTRAIT | Tablet - Grid Layout, 2 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                mRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                // PORTRAIT | PHONE - 1 Column
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }

        loadRecipeData();

        return rootView;
    }

    public void showRecipes() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void loadingRecipes() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showTextForNoRecipes() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextViewNoRecipes.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextViewErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loadRecipeData() {
        loadingRecipes();
        new FetchRecipeTask().execute();
    }

    public class FetchRecipeTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {
            try {String jsonRecipeResponse = NetworkUtils
                        .getResponseFromHttpUrl(NetworkUtils.buildRecipeUrl());
                ArrayList<Recipe> recipeJsonData = OpenRecipeJsonUtils
                        .getRecipeListFromJson(getActivity(), jsonRecipeResponse);
                return recipeJsonData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            if (recipes != null) {
                if (recipes.isEmpty()) {
                    showTextForNoRecipes();
                } else {
                    // Set RecipeAdapter
                    mRecipeListAdapter = new RecipeListAdapter(getActivity(), recipes);
                    mRecyclerView.setAdapter(mRecipeListAdapter);
                    showRecipes();
                    for (int i = 0; i < recipes.size(); i++) {
                        addRecipesToDatabase(recipes.get(i));
                    }
                }
            } else  {
                showErrorMessage();
            }
        }
    }

    public void addRecipesToDatabase(Recipe recipe) {
        if (!checkRecipeInDatabase(recipe)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,
                    recipe.getRecipeID());
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME,
                    recipe.getRecipeName());
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVINGS,
                    recipe.getServings());
            contentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE,
                    recipe.getImage());
            Uri recipeUri = getContext().getContentResolver()
                    .insert(RecipeContract.RecipeEntry.CONTENT_URI, contentValues);

            if (recipe.getIngredients() != null) {
                if (recipe.getIngredients().size() > 0) {
                    ArrayList<Ingredient> ingredients = recipe.getIngredients();
                    for (int i = 0; i < ingredients.size(); i++) {
                        ContentValues ingredientContentValues = new ContentValues();
                        Ingredient currentIngredient = ingredients.get(i);
                        ingredientContentValues.put(IngredientContract.IngredientEntry
                                .COLUMN_RECIPE_ID, recipe.getRecipeID());
                        ingredientContentValues.put(IngredientContract.IngredientEntry
                                .COLUMN_INGREDIENT_MEASURE, currentIngredient.getMeasure());
                        ingredientContentValues.put(IngredientContract.IngredientEntry
                                .COLUMN_INGREDIENT_QUANTITY, currentIngredient.getQuantity());
                        ingredientContentValues.put(IngredientContract.IngredientEntry
                                .COLUMN_INGREDIENT_NAME, currentIngredient.getIngredientName());
                        Uri ingredientUri = getContext().getContentResolver().insert(
                                IngredientContract.IngredientEntry.CONTENT_URI,
                                ingredientContentValues);
                    }

                }
            }

            if (recipe.getSteps() != null) {
                if (recipe.getSteps().size() > 0) {
                    ArrayList<Step> steps = recipe.getSteps();
                    for (int i = 0; i < steps.size(); i++) {
                        ContentValues stepContentValues = new ContentValues();
                        Step currentStep = steps.get(i);
                        stepContentValues.put(StepContract.StepEntry.COLUMN_STEP_ID,
                                currentStep.getStepID());
                        stepContentValues.put(StepContract.StepEntry.COLUMN_RECIPE_ID,
                                recipe.getRecipeID());
                        stepContentValues.put(StepContract.StepEntry.COLUMN_STEP_SHORT_DESCRIPTION,
                                currentStep.getShortDescription());
                        stepContentValues.put(StepContract.StepEntry.COLUMN_STEP_DESCRIPTION,
                                currentStep.getDescription());
                        stepContentValues.put(StepContract.StepEntry.COLUMN_STEP_VIDEO_URL,
                                currentStep.getVideoURL());
                        stepContentValues.put(StepContract.StepEntry.COLUMN_STEP_THUMBNAIL_URL,
                                currentStep.getThumbnailURL());
                        Uri stepUri = getContext().getContentResolver().insert(
                                StepContract.StepEntry.CONTENT_URI,
                                stepContentValues);
                    }
                }
            }

        } else {

        }
    }

    public void removeRecipesFromDatabase() {
        Uri recipeUri = RecipeContract.RecipeEntry.CONTENT_URI;
        getContext().getContentResolver().delete(recipeUri, null, null);

        Uri ingredientUri = IngredientContract.IngredientEntry.CONTENT_URI;
        getContext().getContentResolver().delete(ingredientUri, null, null);

        Uri stepUri = StepContract.StepEntry.CONTENT_URI;
        getContext().getContentResolver().delete(stepUri, null, null);
    }

    public boolean checkRecipeInDatabase(Recipe recipe) {
        Uri uri = RecipeContract.RecipeEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(recipe.getRecipeID()).build();
        Cursor c = getContext().getContentResolver().query(uri, null, null, null, null);
        if (c == null) {
            return false;
        } else {
            return (c.getCount() > 0);
        }
    }

    public void removeRecipeByIdFromDatabase(Recipe recipe) {
        Uri recipeUri = RecipeContract.RecipeEntry.CONTENT_URI;
        recipeUri = recipeUri.buildUpon().appendPath(recipe.getRecipeID()).build();
        getContext().getContentResolver().delete(recipeUri, null, null);

        Uri ingredientUri = IngredientContract.IngredientEntry.CONTENT_URI;
        ingredientUri = ingredientUri.buildUpon().appendPath(recipe.getRecipeID()).build();
        getContext().getContentResolver().delete(ingredientUri, null, null);

        Uri stepUri = StepContract.StepEntry.CONTENT_URI;
        stepUri = stepUri.buildUpon().appendPath(recipe.getRecipeID()).build();
        getContext().getContentResolver().delete(stepUri, null, null);
    }



    @Override
    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(RECIPE_RECYCLER_STATE, listState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(RECIPE_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
