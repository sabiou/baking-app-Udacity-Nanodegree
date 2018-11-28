package xyz.godi.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe_key";

    private List<Recipe> mRecipes;
    private Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // take data from child activity
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(RECIPE_KEY);

        // action bar
        ActionBar actionBar = getSupportActionBar();
        setTitle(recipe.getName());

        // ui
        populateUI();
    }

    private void populateUI() {
        
    }
}