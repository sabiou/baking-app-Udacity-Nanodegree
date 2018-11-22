package xyz.godi.bakingapp.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.godi.bakingapp.R;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipe_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
    }
}