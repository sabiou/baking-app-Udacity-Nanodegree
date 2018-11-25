package xyz.godi.bakingapp.ui.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String RECIPES_KEY = "recipes";
    @BindView(R.id.recipeRecycler)
    RecyclerView mRecipeRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private RecipesAdapter adapter;
    private List<Recipe> mRecipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        if (savedInstanceState == null || !savedInstanceState.containsKey(RECIPES_KEY)) {
            mSwipeRefresh.setRefreshing(true);
            loadRecipes();
        } else {
            mRecipeList = savedInstanceState.getParcelable(RECIPES_KEY);
        }

        loadRecipes();
    }

    private void initView() {
        mRecipeList = new ArrayList<>();
        mRecipeRecycler.setHasFixedSize(true);
        adapter = new RecipesAdapter(this,mRecipeList);
        mRecipeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecipeRecycler.setAdapter(adapter);
        // add recycler item decoration
        mRecipeRecycler.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
        // on itemTouchListener
        mRecipeRecycler.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    private void loadRecipes() {
        // set refreshing if this method is called by our BroadcastReceiver

        RecipesService service = RetrofitClient.getClient().create(RecipesService.class);
        Call<List<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipeList = response.body();
                    mRecipeRecycler.setAdapter(new RecipesAdapter(getApplicationContext(),mRecipeList));
                    adapter.setData(mRecipeList);
                    adapter.notifyDataSetChanged();
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
            }
        });
    }
}