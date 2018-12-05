package xyz.godi.bakingapp.ui.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;

public class StepDetailsActivity extends AppCompatActivity {

    private static final String STEP_KEY = "step";
    private static final String RECIPE_KEY = "recipe";
    public int index;
    public Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Bundle recipeBundle = getIntent().getExtras();
        if (recipeBundle != null && recipeBundle.containsKey(RECIPE_KEY) && recipeBundle.containsKey(STEP_KEY)) {
            recipe = recipeBundle.getParcelable(RECIPE_KEY);
            index = recipeBundle.getInt(STEP_KEY);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}