package xyz.godi.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.adapters.RecipesAdapter;
import xyz.godi.bakingapp.api.RecipeResponse;
import xyz.godi.bakingapp.api.RecipesService;
import xyz.godi.bakingapp.api.RetrofitClient;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.utils.Listeners;
import xyz.godi.bakingapp.utils.SpacingItemDecoration;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesOnClickHandler {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String RECIPES_KEY = "recipes";
    @BindView(R.id.recipeRecycler)
    RecyclerView mRecipeRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private RecipesAdapter adapter;
    private List<Recipe> mRecipeList;
    private RecipesAdapter.RecipesOnClickHandler mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupRecycler();

        if (savedInstanceState == null || !savedInstanceState.containsKey(RECIPES_KEY)) {
            mSwipeRefresh.setRefreshing(true);
            loadRecipes();
        } else {
            mRecipeList = savedInstanceState.getParcelable(RECIPES_KEY);
        }
    }

    private void setupRecycler() {
        mRecipeRecycler.setHasFixedSize(true);
        boolean isTwoPane = getResources().getBoolean(R.bool.istTwoPane);
        mRecipeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // add recycler item decoration
        mRecipeRecycler.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
        // on itemTouchListener
        mRecipeRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    private void loadRecipes() {
        // set refreshing if this method is called by our BroadcastReceiver

        RecipesService service = RetrofitClient.getClient().create(RecipesService.class);
        Call<RecipeResponse> call = service.getRecipes();
        call.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                mRecipeList = fetchResults(response);
                adapter = new RecipesAdapter(getApplicationContext(), mRecipeList, new Listeners.OnItemClickListener() {
                    @Override
                    public void onItemCLick(int position) {
                        mListener.onClick(mRecipeList.get(position));
                    }
                });
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(),t.toString());
            }
        });
    }

    private List<Recipe> fetchResults(Response<RecipeResponse> response) {
        RecipeResponse recipeResponse = response.body();
        return recipeResponse.results;
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.RECIPE_KEY, recipe);
        startActivity(intent);
    }
}