package xyz.godi.bakingapp.adapters;

import android.content.Context;
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
import xyz.godi.bakingapp.utils.Listeners;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private Context mContext;
    private Listeners.OnItemClickListener mListener;

    public RecipesAdapter(Context context, List<Recipe> recipes, Listeners.OnItemClickListener listener) {
        this.mRecipes = recipes;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_item, viewGroup, false);

        return new RecipeViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int i) {
        // set the recipe name
        holder.tv_recipe_name.setText(mRecipes.get(i).getName());
        // the recipes servings
        holder.tv_servings.setText(mRecipes.get(i).getServings());
        // the recipe image if there's one
        String img = mRecipes.get(i).getImage();
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.ic_tilda_icons_5ev_cake)
                .into(holder.iv_recipe);
        // set on item clickListener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemCLick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipeName)
        TextView tv_recipe_name;
        @BindView(R.id.tv_servings)
        TextView tv_servings;
        @BindView(R.id.iv_recipe)
        AppCompatImageView iv_recipe;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}