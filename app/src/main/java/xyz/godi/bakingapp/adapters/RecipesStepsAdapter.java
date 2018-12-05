package xyz.godi.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import xyz.godi.bakingapp.R;
import xyz.godi.bakingapp.holders.IngredientsViewHolder;
import xyz.godi.bakingapp.holders.StepsViewHolder;
import xyz.godi.bakingapp.models.Ingredients;
import xyz.godi.bakingapp.models.Recipe;
import xyz.godi.bakingapp.models.Steps;
import xyz.godi.bakingapp.utils.Listeners;

public class RecipesStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Steps> steps;
    private Recipe mRecipe;
    private int stepIndex;
    private Listeners.OnItemClickListener mListener;

    public RecipesStepsAdapter(Recipe recipe, int index) {
        steps = recipe.getSteps();
        this.stepIndex = index;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            return new IngredientsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ingredients_list_item, viewGroup, false));
        } else {
            return new StepsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recipe_step_item, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;
            // format the ingredients list text
            StringBuilder builder = new StringBuilder();
            // itterate trough ingredients list
            for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
                Ingredients ingredients = mRecipe.getIngredients().get(i);
                builder.append(String.format(Locale.getDefault(), "• %s (%d %s)",
                        ingredients.getIngredient(), ingredients.getQuantity(), ingredients.getMeasure()));
                if (i != mRecipe.getIngredients().size() - 1) {
                    builder.append("\n");
                }
            }
            // set the ingredients content
            viewHolder.ingredientsText.setText(builder.toString());
        } else {
            StepsViewHolder viewHolder = (StepsViewHolder) holder;
            // step order
            viewHolder.stepOrder.setText(String.format("%s.", String.valueOf(position - 1)));
            // step description
            viewHolder.stepName.setText(mRecipe.getSteps().get(position - 1).getDescription());

            // set the onClickListener
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemCLick(position - 1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mRecipe.getSteps().size() + 1;
    }
}