package xyz.godi.bakingapp.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;

public class RecipeStepDetailsFragment extends Fragment {

    private static int index;
    private Recipe recipe;
    private boolean isTablet;
    private SimpleExoPlayer mExoPlayer;
    private long playerPosition;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_step_description)
    TextView stepDescription;
    @BindView(R.id.iv_previous_step)
    ImageView previousStep;
    @BindView(R.id.iv_next_step) ImageView nextStep;
    @BindView(R.id.iv_video_thumbnail) ImageView videoThumbnail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragments_recipes_step_details, container, false);
    }
}