package com.example.android.bakingtime;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    Recipe mRecipe;
    RecipeDetailIngredientsAdapter mRecipeDetailIngredientsAdapter;
    RecipeDetailStepsAdapter mRecipeDetailStepsAdapter;
    RecyclerView mIngredientsRecyclerView;
    RecyclerView mStepsRecyclerView;

    public RecipeDetailFragment () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mIngredientsRecyclerView = ButterKnife.findById(rootView,
                R.id.recycler_view_recipe_detail_ingredients_list_tablet);
        mStepsRecyclerView = ButterKnife.findById(rootView,
                R.id.recycler_view_recipe_detail_steps_tablet);

        if (this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // LANDSCAPE - Grid Layout, 2 Columns
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            mIngredientsRecyclerView.setLayoutManager(mLayoutManager);
        } else {
            // PORTRAIT - 1 Column
            mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        // Set up view and adapter for Steps
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecipeDetailIngredientsAdapter = new RecipeDetailIngredientsAdapter(getContext(),
                mRecipe.getIngredients());
        mIngredientsRecyclerView.setAdapter(mRecipeDetailIngredientsAdapter);


        mRecipeDetailStepsAdapter = new RecipeDetailStepsAdapter(getContext(), mRecipe.getSteps());
        mStepsRecyclerView.setAdapter(mRecipeDetailStepsAdapter);

        // Set a listener for steps and triggers callback
        mStepsRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}
