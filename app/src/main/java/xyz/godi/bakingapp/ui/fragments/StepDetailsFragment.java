package xyz.godi.bakingapp.ui.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.databinding.RecipesStepDetailsBinding;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.models.Steps;

public class StepDetailsFragment extends Fragment implements Player.EventListener, View.OnClickListener {

    private static final String EXTRA = "step" ;
    private static final String POSITION = "position";
    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();

    PlayerView mPlayerView;
    TextView stepDescription;
    ImageView videoThumbnail;
    TextView noVideo;

    private Context mContext;
    private Steps steps;
    private Recipe mRecipe;
    private boolean isTablet;
    private String videoUrl;
    private SimpleExoPlayer mExoPlayer;
    private long playerPosition;
    private boolean playWhenReady;
    private int scrennOrientation;

    private OnStepClickListener mListener;

    public StepDetailsFragment() {
    }

    public static StepDetailsFragment newInstance(Steps steps) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA, steps);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            steps = getArguments().getParcelable(EXTRA);
        }
         videoUrl = steps != null ? steps.getVideoURL() : null;
        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(POSITION);
        } else {
            playerPosition = 0;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecipesStepDetailsBinding binding =
                RecipesStepDetailsBinding.inflate(inflater, container, false);
        binding.setSteps(steps);

        mContext = getActivity();
        mPlayerView = binding.exoPlayerView;
        videoThumbnail = binding.ivVideoThumbnail;
        stepDescription = binding.tvStepDescription;
        isTablet = getResources().getBoolean(R.bool.istTwoPane);
        scrennOrientation = getResources().getConfiguration().orientation;

        // If screen is in landscape mode, show video in full screen
        // else show nav buttons and indicator
        if (scrennOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullScreenPlayer();
        } else {
            Button nextStep = binding.btnNextStep;
            nextStep.setOnClickListener(this);
            Button previousStep = binding.btnPreviousStep;
            previousStep.setOnClickListener(this);
        }

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mListener = (OnStepClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + getString(R.string.error_interface));
        }
    }


    // init exoPlayer
    private void initExoPlayer() {
        // check if mExoPlayer is not null before init new
        if (mExoPlayer == null && !(videoUrl.isEmpty()) ) {
            // show the player
            mPlayerView.setVisibility(View.VISIBLE);
            // default trackselector
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            // user agent
            String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
            DataSource.Factory factory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource =
                    new ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(videoUrl));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(playerPosition);
            mExoPlayer.setPlayWhenReady(true);
        } else {
            // hide the video view in landscape
            if (scrennOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPlayerView.setVisibility(View.GONE);
                videoThumbnail.setVisibility(View.VISIBLE);
                stepDescription.setVisibility(View.VISIBLE);
            } else  {
                // in portrait
                mPlayerView.setVisibility(View.GONE);
                videoThumbnail.setVisibility(View.VISIBLE);
            }
        }
    }

    void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    // method to hide the system UI for full screnn mode
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        Objects.requireNonNull(((AppCompatActivity)
                Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        initExoPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            playerPosition = mExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void setFullScreenPlayer() {
        if (!videoUrl.isEmpty() && !isTablet) {
            hideSystemUI();
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_READY) {
            Log.d(LOG_TAG, "Player is playing");
        } else if (playbackState == Player.STATE_READY) {
            Log.d(LOG_TAG, "Player is paused");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(POSITION, playerPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_next_step:
                mListener.onNextStepClick(steps);
                break;
            case R.id.btn_previous_step:
                mListener.onPreviousStepClick(steps);
                break;
        }
    }

    public interface OnStepClickListener {
        void onPreviousStepClick(Steps steps);
        void onNextStepClick(Steps steps);
    }
}