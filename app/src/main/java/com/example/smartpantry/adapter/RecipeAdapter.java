package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.data.Recipe;
import com.example.smartpantry.data.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Custom RecyclerView adapter that lists recipes as tappable cards. */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    /** The Activity implements this to open the recipe detail screen. */
    public interface Listener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes = new ArrayList<>();
    private final Map<Long, Integer> ingredientCounts = new HashMap<>();
    private final Listener listener;

    public RecipeAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> newRecipes,
                           Map<Long, List<RecipeIngredient>> ingredientsByRecipe) {
        recipes.clear();
        ingredientCounts.clear();
        if (newRecipes != null) {
            recipes.addAll(newRecipes);
            for (Recipe recipe : newRecipes) {
                List<RecipeIngredient> list = ingredientsByRecipe.get(recipe.id);
                ingredientCounts.put(recipe.id, list == null ? 0 : list.size());
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        Integer count = ingredientCounts.get(recipe.id);
        holder.name.setText(recipe.name);
        holder.info.setText((count == null ? 0 : count) + " ingredients, all in your pantry");
        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView info;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_recipe_name);
            info = itemView.findViewById(R.id.text_recipe_info);
        }
    }
}
