package xyz.godi.bakingapp.ui.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.utils.Helpers;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe_key";

    private List<Recipe> mRecipes;
    private Recipe recipe;
    private View view;

    private boolean isTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // take data from child activity
        Bundle recipeBundle = getIntent().getExtras();
        if (recipeBundle != null && recipeBundle.containsKey(RECIPE_KEY)) {
            recipe = recipeBundle.getParcelable(RECIPE_KEY);
        } else {
            // show a message in SnackBar
            Helpers.createSnackBar(this,view,"Failed to load recipes");
        }

        // action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
        }

        // ui
        populateUI();
    }

    private void populateUI() {

    }
}