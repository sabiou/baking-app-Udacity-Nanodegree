package xyz.godi.bakingapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import xyz.godi.bakingapp.models.Recipe;

public interface RecipesService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}