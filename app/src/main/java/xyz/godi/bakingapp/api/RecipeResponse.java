package xyz.godi.bakingapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import xyz.godi.bakingapp.models.Recipe;

public class RecipeResponse {
    @SerializedName("recipe")
    public List<Recipe> results;
}
