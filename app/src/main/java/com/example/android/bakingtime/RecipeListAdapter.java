package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {
    ArrayList<Recipe> mRecipes;
    Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view_recipe_list) CardView mCardView;
        @BindView(R.id.text_view_recipe_list_name) TextView mTextView;
        @BindView(R.id.image_view_recipe_list_photo) ImageView mImageView;
        public LinearLayout mLinearLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            ButterKnife.bind(this, v);
            mLinearLayout = v;
        }
    }

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mRecipes.get(position).getRecipeName());
        if (mRecipes.get(position).getImage().equals("") ||
                mRecipes.get(position).getImage() == null) {
            // If image url is empty or null, sets image to placeholder
            Picasso.with(mContext)
                    .load(R.drawable.dish_placeholder)
                    .into(holder.mImageView);
        } else {
            // If image url exists, sets image from image url
            Picasso.with(mContext)
                    .load(mRecipes.get(position).getImage())
                    .into(holder.mImageView);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create recipe detail Activity here
                Intent intentToStartRecipeDetailActivity = new Intent(mContext,
                        RecipeDetailActivity.class);
                intentToStartRecipeDetailActivity.putExtra("recipe", mRecipes.get(position));
                mContext.startActivity(intentToStartRecipeDetailActivity);
            }
        };
        holder.mTextView.setOnClickListener(listener);
        holder.mCardView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}
