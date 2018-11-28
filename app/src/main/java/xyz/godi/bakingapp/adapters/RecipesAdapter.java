package xyz.godi.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.ui.activities.RecipeDetailsActivity;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private static final String RECIPES_KEY = "recipes";

    private List<Recipe> mRecipes;
    private Context mContext;

    public RecipesAdapter(Context context, List<Recipe> recipes) {
        this.mContext = context;
        this.mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.recipes_item, viewGroup, false);

        return new RecipeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int i) {
        final Recipe recipeItem = mRecipes.get(i);
        // set the recipe name
        holder.tv_recipe_name.setText(recipeItem.getName());
        // the recipe image if there's one
        String img = recipeItem.getImage();
        if (!img.isEmpty()) {
            Picasso.get()
                    .load(img)
                    .placeholder(R.drawable.ic_tilda_icons_5ev_cake)
                    .into(holder.iv_recipe);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setData(List<Recipe> mRecipeList) {
        mRecipes = mRecipeList;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView tv_recipe_name;
        @BindView(R.id.iv_recipe_image)
        AppCompatImageView iv_recipe;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe recipeItem = mRecipes.get(getAdapterPosition());
            Intent intent = new Intent(mContext,RecipeDetailsActivity.class);
            intent.putExtra(RECIPES_KEY,recipeItem);
            v.getContext().startActivity(intent);
        }
    }
}