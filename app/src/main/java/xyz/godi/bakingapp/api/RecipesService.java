package xyz.godi.bakingapp.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<RecipeResponse> getRecipes();
}