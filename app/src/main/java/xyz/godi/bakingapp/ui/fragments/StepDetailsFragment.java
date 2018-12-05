package xyz.godi.bakingapp.ui.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.models.Steps;
import xyz.godi.bakingapp.ui.activities.StepDetailsActivity;

public class StepDetailsFragment extends Fragment {

    private static int index;
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_step_description)
    TextView stepDescription;
    @BindView(R.id.iv_previous_step)
    ImageView previousStep;
    @BindView(R.id.iv_next_step)
    ImageView nextStep;
    @BindView(R.id.iv_video_thumbnail)
    ImageView videoThumbnail;
    @BindView(R.id.tv_no_video)
    TextView noVideo;
    private Recipe mRecipe;
    private boolean isTablet;
    private SimpleExoPlayer mExoPlayer;
    private long playerPosition;
    private boolean playWhenReady;

    public StepDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater
                .inflate(R.layout.fragments_recipes_step_details, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            restoreIntanceState(savedInstanceState);
        } else {
            getParamsFromActivity();
            playWhenReady = true;
        }

        // set the step description textView
        stepDescription.setText(mRecipe.getSteps().get(index).getDescription());

        initMedia();
        initNavigation();
        initButtonListener();

        return rootView;
    }

    private void initMedia() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
        String videoUri = mRecipe.getSteps().get(index).getVideoURL();
        if (videoUri != null || videoUri.isEmpty()) {
            initThumnail();
        } else {
            initExoPlayer(videoUri);
        }
    }

    private void initThumnail() {
        String thumbnailUrl = mRecipe.getSteps().get(index).getThumbnailURL();
        if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
            hidePlayer();
        } else {
            displayThumbnail(thumbnailUrl);
        }
    }

    private void displayThumbnail(String thumbnailUrl) {
        Picasso.get().load(thumbnailUrl).error(R.color.colorAccent).into(videoThumbnail);
        videoThumbnail.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
    }

    private void initButtonListener() {
        setNextStepListener();
        setPreviousStepListener();
    }

    private void setPreviousStepListener() {
        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0) index--;
                Steps steps = mRecipe.getSteps().get(index);
                stepDescription.setText(steps.getDescription());
                initMedia();
                prevButtonState();
            }

            private void prevButtonState() {
                if (index == 0) {
                    nextStep.setVisibility(View.VISIBLE);
                    previousStep.setVisibility(View.INVISIBLE);
                } else {
                    nextStep.setVisibility(View.VISIBLE);
                    previousStep.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setNextStepListener() {
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                Steps steps = mRecipe.getSteps().get(index);
                stepDescription.setText(steps.getDescription());
                initMedia();
                nextButtonState();
            }

            private void nextButtonState() {
                if (mRecipe.getSteps().size() == index) {
                    nextStep.setVisibility(View.INVISIBLE);
                    previousStep.setVisibility(View.VISIBLE);
                } else {
                    nextStep.setVisibility(View.VISIBLE);
                    previousStep.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initNavigation() {
        if (isTablet) {
            previousStep.setVisibility(View.GONE);
            nextStep.setVisibility(View.GONE);
        } else {
            if (index == 0) {
                previousStep.setVisibility(View.GONE);
            } else {
                nextStep.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initExoPlayer(String videoUri) {
        displayPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.prepare(getMediaSource(videoUri));
            mExoPlayer.seekTo(playerPosition);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private MediaSource getMediaSource(String videoUri) {
        String userAgent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        }
        DefaultDataSourceFactory dataSourceFactory = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
        }
        Uri uri = Uri.parse(videoUri);
        return new ExtractorMediaSource(uri, dataSourceFactory,
                new DefaultExtractorsFactory(), null, null);
    }

    private void getParamsFromActivity() {
        if (getActivity() instanceof StepDetailsActivity) {
            StepDetailsActivity activity = (StepDetailsActivity) getActivity();
            index = activity.index;
            mRecipe = activity.recipe;
        }
    }

    private void hidePlayer() {
        videoThumbnail.setVisibility(View.GONE);
        noVideo.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);
    }

    private void displayPlayer() {
        noVideo.setVisibility(View.GONE);
        videoThumbnail.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void restoreIntanceState(Bundle savedInstanceState) {

    }

}