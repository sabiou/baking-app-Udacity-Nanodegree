package xyz.godi.bakingapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.adapters.RecipesStepsAdapter;
import xyz.godi.bakingapp.ui.activities.RecipeDetailsActivity;

public class StepListFragment extends Fragment {

    private static final String STEP_KEY = "step";
    @BindView(R.id.rv_recipe_step_list) RecyclerView recyclerView;

    public StepListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecipeDetailsActivity activity = (RecipeDetailsActivity) getActivity();

        // Inflate the layout for this fragment
        final View rootView = inflater
                .inflate(R.layout.fragment_recipe_step_list, container, false);
        initView(activity, rootView);
        ButterKnife.bind(this,rootView);

        return rootView;
    }

    private void initView(RecipeDetailsActivity activity, View rootView) {
        RecipesStepsAdapter stepsAdapter =
                new RecipesStepsAdapter(activity.recipe, activity.index);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(stepsAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static StepListFragment newInstance(int selectedStep) {
        StepListFragment fragment = new StepListFragment();
        // Set the bundle arguments for the fragment.
        Bundle args = new Bundle();
        args.putInt(STEP_KEY, selectedStep);
        fragment.setArguments(args);
        return fragment;
    }
}