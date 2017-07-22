package com.example.android.bakingtime;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment{

    ArrayList<Step> mSteps;
    int mPosition;
    TextView mTextViewDescription;

    TextView mTextViewPreviousStep;
    TextView mTextViewNextStep;
    FrameLayout mFrameLayout;

    SimpleExoPlayer mExoPlayer;
    SimpleExoPlayerView mPlayerView;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    private boolean isTablet;

    public RecipeStepDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            mPosition = savedInstanceState.getInt("position");
        }
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        isTablet = rootView.getResources().getBoolean(R.bool.isTablet);

        if (this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            // LANDSCAPE & PHONE
            mPlayerView = ButterKnife.findById(rootView,
                    R.id.exoplayer_recipe_step_detail);
            mTextViewDescription = ButterKnife.findById(rootView,
                    R.id.text_view_recipe_step_detail_description);
            mFrameLayout = ButterKnife.findById(rootView,
                    R.id.frame_layout_recipe_step_detail);
            mTextViewPreviousStep = ButterKnife.findById(rootView,
                    R.id.text_view_previous_step);
            mTextViewNextStep = ButterKnife.findById(rootView,
                    R.id.text_view_next_step);


            if (mSteps != null) {
                mTextViewDescription.setText(mSteps.get(mPosition).getDescription());
                mTextViewDescription.setVisibility(View.INVISIBLE);
                mFrameLayout.setVisibility(View.INVISIBLE);
                mTextViewPreviousStep.setVisibility(View.INVISIBLE);
                mTextViewNextStep.setVisibility(View.INVISIBLE);

            }

        } else {
            mPlayerView = ButterKnife.findById(rootView,
                    R.id.exoplayer_recipe_step_detail);
            mTextViewDescription = ButterKnife.findById(rootView,
                    R.id.text_view_recipe_step_detail_description);
            mTextViewPreviousStep = ButterKnife.findById(rootView,
                    R.id.text_view_previous_step);
            mTextViewNextStep = ButterKnife.findById(rootView,
                    R.id.text_view_next_step);

            if (mSteps != null) {
                mTextViewDescription.setText(mSteps.get(mPosition).getDescription());
                updatePreviousNextStepView();
            }

            mTextViewPreviousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosition > 0) {
                        mPosition--;
                        mTextViewDescription.setText(mSteps.get(mPosition).getDescription());
                        playCurrentVideo();
                        updatePreviousNextStepView();
                    }
                }
            });

            mTextViewNextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPosition < mSteps.size() - 1) {
                        mPosition++;
                        mTextViewDescription.setText(mSteps.get(mPosition).getDescription());
                        playCurrentVideo();
                        updatePreviousNextStepView();
                    }
                }
            });
        }


        return rootView;
    }

    public void setStep(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);
        }

        if (mSteps.get(mPosition).getVideoURL() != null) {
            playCurrentVideo();
        }
    }

    private void playCurrentVideo() {
        if (mSteps.get(mPosition).getVideoURL().isEmpty()) {
            Toast.makeText(getContext(), "No video for this step.", Toast.LENGTH_SHORT).show();
            loadViewWithoutVideo();
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
            MediaSource mediaSource = buildMediaSource(Uri
                    .parse(mSteps.get(mPosition).getVideoURL()));
            mExoPlayer.prepare(mediaSource, true, false);
        }
    }

    private void loadViewWithoutVideo() {
        mExoPlayer.stop();
        mPlayerView.setVisibility(View.INVISIBLE);
        mTextViewDescription.setVisibility(View.VISIBLE);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri, new DefaultHttpDataSourceFactory("exoplayer"),
                new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void updatePreviousNextStepView() {
        if (mPosition == 0) {
            mTextViewPreviousStep.setVisibility(View.INVISIBLE);
        } else if (mPosition == mSteps.size()-1) {
            mTextViewNextStep.setVisibility(View.INVISIBLE);
        } else {
            mTextViewPreviousStep.setVisibility(View.VISIBLE);
            mTextViewNextStep.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (this.getResources().getConfiguration()
                .orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
            mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList("steps", mSteps);
        currentState.putInt("position", mPosition);
    }

}
