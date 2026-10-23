package com.example.smartpantry;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.data.AppDatabase;
import com.example.smartpantry.data.Recipe;
import com.example.smartpantry.data.RecipeDao;
import com.example.smartpantry.data.RecipeIngredient;
import com.example.smartpantry.util.Format;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

/** Recipe Detail screen: the full ingredient list and the method for one recipe. */
public class RecipeDetailActivity extends AppCompatActivity {

    /** Intent extra holding the id of the recipe to show. */
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private MaterialToolbar toolbar;
    private TextView ingredientsView;
    private TextView stepsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        ingredientsView = findViewById(R.id.text_ingredients);
        stepsView = findViewById(R.id.text_steps);

        final long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        final RecipeDao dao = AppDatabase.getInstance(this).recipeDao();

        AppDatabase.databaseExecutor.execute(() -> {
            final Recipe recipe = dao.getRecipe(recipeId);
            final List<RecipeIngredient> ingredients = dao.getIngredientsFor(recipeId);
            runOnUiThread(() -> {
                if (recipe == null) {
                    finish();
                    return;
                }
                toolbar.setTitle(recipe.name);
                ingredientsView.setText(buildIngredientText(ingredients));
                stepsView.setText(buildStepsText(recipe.steps));
            });
        });
    }

    private static String buildIngredientText(List<RecipeIngredient> ingredients) {
        StringBuilder text = new StringBuilder();
        for (RecipeIngredient ingredient : ingredients) {
            text.append("\u2022 ").append(Format.quantity(ingredient.quantity));
            if ("pcs".equals(ingredient.unit)) {
                text.append(" \u00D7 ");
            } else {
                text.append(' ').append(ingredient.unit).append(' ');
            }
            text.append(ingredient.name).append('\n');
        }
        return text.toString().trim();
    }

    /** Numbers each line of the stored steps: "1. ...", "2. ...". */
    private static String buildStepsText(String steps) {
        String[] lines = steps.split("\n");
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                text.append("\n\n");
            }
            text.append(i + 1).append(". ").append(lines[i]);
        }
        return text.toString();
    }
}
