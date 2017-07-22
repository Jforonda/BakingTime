package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    Recipe mRecipe;
    RecipeDetailIngredientsAdapter mRecipeDetailIngredientsAdapter;
    RecipeDetailStepsAdapter mRecipeDetailStepsAdapter;
    RecyclerView mIngredientsRecyclerView;
    RecyclerView mStepsRecyclerView;

    RecipeStepDetailFragment recipeStepDetailFragment;
    FragmentManager fragmentManager;

    int currentPosition = 0;

    private final String STEP_RECYCLER_STATE = "step_recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getBoolean(R.bool.isTablet)) { // If running on TABLET
            // Set up Fragment for Recipe Details
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recipe_detail);
            Intent intentThatStartedThisActivity = getIntent();
            mRecipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
            getSupportActionBar().setTitle(mRecipe.getRecipeName());

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipe(mRecipe);

            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();

            // Set up Fragment for Step Details
            recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setStep(mRecipe.getSteps());
            recipeStepDetailFragment.setPosition(currentPosition);

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_video_container, recipeStepDetailFragment)
                    .commit();

        } else { // If running on PHONE
            // Set up Activity for Recipe Details
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recipe_detail);
            if (savedInstanceState == null) {
                Intent intentThatStartedThisActivity = getIntent();
                mRecipe = intentThatStartedThisActivity.getParcelableExtra("recipe");
            } else {
                mRecipe = savedInstanceState.getParcelable("recipe");
            }
            getSupportActionBar().setTitle(mRecipe.getRecipeName());

            // Set up view and adapter for Ingredients
            mIngredientsRecyclerView = ButterKnife.findById(this,
                    R.id.recycler_view_recipe_detail_ingredients_list);
            if (this.getResources().getConfiguration()
                    .orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // LANDSCAPE - Grid Layout, 2 Columns
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                mIngredientsRecyclerView.setLayoutManager(mLayoutManager);
            } else {
                // PORTRAIT - 1 Column
                mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }

            // Set up view and adapter for Steps
            mStepsRecyclerView = ButterKnife.findById(this, R.id.recycler_view_recipe_detail_steps);
            mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mRecipeDetailIngredientsAdapter = new RecipeDetailIngredientsAdapter(this,
                    mRecipe.getIngredients());
            mIngredientsRecyclerView.setAdapter(mRecipeDetailIngredientsAdapter);


            mRecipeDetailStepsAdapter = new RecipeDetailStepsAdapter(this, mRecipe.getSteps());
            mStepsRecyclerView.setAdapter(mRecipeDetailStepsAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable("recipe", mRecipe);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mStepsRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(STEP_RECYCLER_STATE, listState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(STEP_RECYCLER_STATE);
            mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
