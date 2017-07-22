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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailStepsAdapter extends RecyclerView.Adapter<RecipeDetailStepsAdapter.
        ViewHolder> {
    Context mContext;
    ArrayList<Step> mSteps;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view_recipe_detail) CardView mCardView;
        @BindView(R.id.text_view_recipe_detail_name) TextView mTextView;
        @BindView(R.id.image_view_recipe_step_detail_photo) ImageView mImageView;
        public LinearLayout mLinearLayout;

        public ViewHolder(LinearLayout v) {
            super(v);
            ButterKnife.bind(this, v);
            mLinearLayout = v;
        }
    }

    public RecipeDetailStepsAdapter(Context context, ArrayList<Step> steps) {
        mContext = context;
        mSteps = steps;
    }

    @Override
    public RecipeDetailStepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_detail_steps_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.mTextView.setText(mSteps.get(position).getShortDescription());
        if (mSteps.get(position).getThumbnailURL().isEmpty() ||
                mSteps.get(position).getThumbnailURL() == null) {
            // If thumbnail url is empty or null, sets image to placeholder
            Picasso.with(mContext)
                    .load(R.drawable.untensil_placeholder)
                    .into(holder.mImageView);
        } else {
            // If thumbnail url exists, sets image from image url
            Picasso.with(mContext)
                    .load(mSteps.get(position).getThumbnailURL())
                    .into(holder.mImageView);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mLinearLayout.getResources().getBoolean(R.bool.isTablet)) {

                } else {
                    Intent intentToStartRecipeDetailActivity = new Intent(mContext,
                            RecipeStepDetailActivity.class);
                    intentToStartRecipeDetailActivity.putParcelableArrayListExtra("steps", mSteps);
                    intentToStartRecipeDetailActivity.putExtra("position", position);
                    mContext.startActivity(intentToStartRecipeDetailActivity);
                }
            }
        };
        holder.mTextView.setOnClickListener(listener);
        holder.mCardView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

}
