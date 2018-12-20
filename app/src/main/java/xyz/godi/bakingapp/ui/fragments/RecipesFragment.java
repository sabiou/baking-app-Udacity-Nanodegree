package xyz.godi.bakingapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.adapters.RecipesAdapter;
import xyz.godi.bakingapp.api.RecipesService;
import xyz.godi.bakingapp.api.RetrofitClient;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.ui.activities.StepsListActivity;
import xyz.godi.bakingapp.utils.SpacingItemDecoration;

public class RecipesFragment extends Fragment implements RecipesAdapter.RecipeClickListener {

    private Context mContext;
    private RecipesAdapter adapter;
    private List<Recipe> mRecipeList;

    public static final String TAG = RecipesFragment.class.getSimpleName();
    private static final String RECIPES_KEY = "recipes";

    @BindView(R.id.recipeRecycler)
    RecyclerView mRecipeRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        mContext = getActivity();
        initViews();

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES_KEY)) {
            mSwipeRefresh.setRefreshing(true);
            loadRecipes();
        } else {
            //mRecipeList = savedInstanceState.getParcelable(RECIPES_KEY);
        }

        // set refresh listener
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // call to refresh
                refresh();
            }
        });

        loadRecipes();

        return view;

    }

    private void refresh() {
        // delay the refresh for content fetch
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // load recipes
                loadRecipes();
                // stop the refreshing after loading complete
                mSwipeRefresh.setRefreshing(false);
            }
        },3000);
    }

    private void initViews() {
        adapter = new RecipesAdapter(mContext, this);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecipeRecycler.setLayoutManager(layoutManager);
        mRecipeRecycler.setHasFixedSize(true);
        mRecipeRecycler.addItemDecoration(new SpacingItemDecoration((int)
                getResources().getDimension(R.dimen.margin_medium)));
        mRecipeRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
        mRecipeRecycler.setAdapter(adapter);
    }

    // Load recipes from server
    private void loadRecipes() {
        // set refreshing if this method is called by our BroadcastReceiver
        RecipesService service = RetrofitClient.getClient().create(RecipesService.class);
        Call<List<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipeList = response.body();
                    //mRecipeRecycler.setAdapter(new RecipesAdapter(mContext,mRecipeList));
                    adapter.setData(mRecipeList);
                    adapter.notifyDataSetChanged();
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // log the error message
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(mContext, StepsListActivity.class);
        intent.putExtra(StepsListActivity.INTENT_EXTRA, recipe);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
