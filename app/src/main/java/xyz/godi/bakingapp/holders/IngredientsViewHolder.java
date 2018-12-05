package xyz.godi.bakingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.bakingapp.R;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_ingredients_list)
    public TextView ingredientsText;

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}