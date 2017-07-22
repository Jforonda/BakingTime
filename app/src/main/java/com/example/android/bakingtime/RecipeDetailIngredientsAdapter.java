package com.example.android.bakingtime;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeDetailIngredientsAdapter extends RecyclerView.
        Adapter<RecipeDetailIngredientsAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Ingredient> mIngredients;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView mTextView;
        public ViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
            mTextView = ButterKnife.findById(mLinearLayout, R.id.text_view_recipe_detail_ingredient);
            if (mLinearLayout.getResources().getBoolean(R.bool.isTablet)) {
                mTextView.setTextSize(15);
            }
        }
    }

    public RecipeDetailIngredientsAdapter(Context context, ArrayList<Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public RecipeDetailIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_detail_ingredients_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredient currentIngredient = mIngredients.get(position);
        String ingredientText = "\u25cf" +
                currentIngredient.getQuantity() + " " +
                currentIngredient.getMeasure() + " " +
                currentIngredient.getIngredientName();
        holder.mTextView.setText(ingredientText);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }


}
