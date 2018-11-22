package xyz.godi.bakingapp.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.ui.fragments.RecipesFragment;

public class MainActivity extends AppCompatActivity implements RecipesFragment.OnRecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.RECIPE_KEY, recipe);
        startActivity(intent);
    }
}