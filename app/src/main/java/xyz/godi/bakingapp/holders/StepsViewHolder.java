package xyz.godi.bakingapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.godi.bakingapp.R;

public class StepsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_step_order) public TextView stepOrder;
    @BindView(R.id.tv_step_name) public TextView stepName;

    public StepsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}