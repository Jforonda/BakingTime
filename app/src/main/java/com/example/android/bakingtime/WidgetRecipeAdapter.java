package com.example.android.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class WidgetRecipeAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    static final String PREFS_NAME =
            "com.example.android.bakingtime.RecipeIngredientWidgetProvider";
    static final String PREF_PREFIX_KEY = "prefix_";

    ArrayList<Recipe> mRecipes;
    Context mContext;
    int mAppWidgetId;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view_recipe_list) CardView mCardView;
        @BindView(R.id.text_view_recipe_list_name) TextView mTextView;
        public LinearLayout mLinearLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            ButterKnife.bind(this, v);
            mLinearLayout = v;
        }
    }

    public WidgetRecipeAdapter(Context context, ArrayList<Recipe> recipes, int appWidgetId) {
        mContext = context;
        mRecipes = recipes;
        mAppWidgetId = appWidgetId;
    }

    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        RecipeListAdapter.ViewHolder vh = new RecipeListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.ViewHolder holder, final int position) {
        holder.mTextView.setText(mRecipes.get(position).getRecipeName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipePref(mContext,
                        mAppWidgetId,
                        mRecipes.get(position).getRecipeID());
            }
        };
        holder.mTextView.setOnClickListener(listener);
        holder.mCardView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    static void saveRecipePref(Context context, int appWidgetId, String recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    static String loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences pref = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = pref.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (prefix != null) {
            return prefix;
        } else {
            return "empty";
        }
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}
