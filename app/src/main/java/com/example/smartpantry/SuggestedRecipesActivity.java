package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.adapter.RecipeAdapter;
import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.data.Recipe;
import com.example.smartpantry.data.RecipeIngredient;
import com.example.smartpantry.logic.IngredientMatcher;
import com.example.smartpantry.util.NavHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.Listener {

    private RecipeAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);

        emptyView = findViewById(R.id.text_empty);
        RecyclerView recycler = findViewById(R.id.recycler_recipes);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this);
        recycler.setAdapter(adapter);

        NavHelper.setup(this, R.id.nav_suggested);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavHelper.syncSelection(this, R.id.nav_suggested);
    
        loadSuggestions();
    }

    private void loadSuggestions() {
        final AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.databaseExecutor.execute(() -> {
            List<PantryItem> pantry = db.pantryDao().getAllNow();
            List<Recipe> recipes = db.recipeDao().getAllRecipes();

          
            Map<Long, List<RecipeIngredient>> byRecipe = new HashMap<>();
            for (RecipeIngredient ingredient : db.recipeDao().getAllIngredients()) {
                List<RecipeIngredient> list = byRecipe.get(ingredient.recipeId);
                if (list == null) {
                    list = new ArrayList<>();
                    byRecipe.put(ingredient.recipeId, list);
                }
                list.add(ingredient);
            }

            final List<Recipe> suggested = IngredientMatcher.suggest(recipes, byRecipe, pantry);

            runOnUiThread(() -> {
                adapter.setRecipes(suggested, byRecipe);
                // Friendly message instead of a blank screen when nothing matches.
                emptyView.setVisibility(suggested.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.id);
        startActivity(intent);
    }
}
