package xyz.godi.bakingapp.ui.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.adapters.RecipesAdapter;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.utils.Listeners;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipesFragment.OnRecipeClickListener} interface
 * to handle interaction events.
 */
public class RecipesFragment extends Fragment {

    public static final String LOG_TAG = RecipesFragment.class.getSimpleName();
    private static final String RECIPES_KEY = "recipes";
    @BindView(R.id.recipeRecycler)
    RecyclerView mRecipeRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    public static final String RECIPE_KEY = "recipe_key";
    private OnRecipeClickListener mListener;
    private List<Recipe> mRecipeList;
    private Unbinder unbinder;

    public RecipesFragment() {
        // Required empty public constructor
    }

    /**
     * This medthod will load the recipes when the app launch,
     * or if the app will launch without an internet connection
     * and then reconnects, will load them without the need for user to perform a (pull to refresh)
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecipeList == null) {
                loadRecipes();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, root);

        // set swipe onRefresh listener
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipes();
            }
        });

        // saving state for rotation purposes
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_KEY)) {
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPES_KEY);
            // TODO set recipes adapter
            mRecipeRecycler.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(),
                    mRecipeList, new Listeners.OnItemClickListener() {
                @Override
                public void onItemCLick(int position) {
                    mListener.onRecipeSelected(mRecipeList.get(position));
                }
            }));
        }

        return root;
    }

    // Load recipes from api
    private void loadRecipes() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mListener = (OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // OnRecipeClickListener interface
    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }
}
