package com.example.smartpantry.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Pre-loads the recipe collection the first time the app runs.
 * Amounts use g, ml and pcs so they can be compared with pantry items in any compatible unit.
 */
public final class RecipeSeeder {

    private RecipeSeeder() { }

    /** Inserts the recipes only if the table is empty, so it is safe to call on every start. */
    public static synchronized void seedIfEmpty(RecipeDao dao) {
        if (dao.countRecipes() > 0) {
            return;
        }

        add(dao, "Cheese Omelette",
                "Beat the eggs with a pinch of salt.\nMelt the butter in a frying pan over medium heat.\nPour in the eggs and cook until nearly set.\nSprinkle over the cheese, fold the omelette and serve.",
                ing("egg", 3.0, "pcs"),
                ing("cheese", 50.0, "g"),
                ing("butter", 10.0, "g"),
                ing("salt", 5.0, "g"));

        add(dao, "Tomato Pasta",
                "Boil the pasta in salted water until tender.\nChop the tomatoes and garlic.\nFry the garlic in olive oil, add the tomatoes and simmer for 10 minutes.\nToss the drained pasta in the sauce and serve.",
                ing("pasta", 200.0, "g"),
                ing("tomato", 4.0, "pcs"),
                ing("garlic", 2.0, "pcs"),
                ing("olive oil", 30.0, "ml"),
                ing("salt", 5.0, "g"));

        add(dao, "Egg Fried Rice",
                "Chop the onion finely.\nScramble the eggs in a hot pan and set them aside.\nFry the onion, add the cooked rice and soy sauce.\nStir the eggs back in and serve hot.",
                ing("rice", 200.0, "g"),
                ing("egg", 2.0, "pcs"),
                ing("onion", 1.0, "pcs"),
                ing("soy sauce", 30.0, "ml"));

        add(dao, "Pancakes",
                "Whisk the flour, sugar, egg and milk into a smooth batter.\nHeat a lightly greased pan.\nPour in a ladle of batter and cook until bubbles appear.\nFlip, cook the other side and serve.",
                ing("flour", 200.0, "g"),
                ing("milk", 300.0, "ml"),
                ing("egg", 1.0, "pcs"),
                ing("sugar", 30.0, "g"));

        add(dao, "Mashed Potatoes",
                "Peel and cut the potatoes, then boil until soft.\nDrain well and mash.\nStir in the butter, warm milk and salt until creamy.",
                ing("potato", 4.0, "pcs"),
                ing("butter", 30.0, "g"),
                ing("milk", 100.0, "ml"),
                ing("salt", 5.0, "g"));

        add(dao, "Garlic Bread",
                "Crush the garlic and mix it into the soft butter.\nSpread the butter over the bread slices.\nBake at 200 C for 8 to 10 minutes until golden.",
                ing("bread", 4.0, "pcs"),
                ing("butter", 40.0, "g"),
                ing("garlic", 2.0, "pcs"));

        add(dao, "Peanut Butter Banana Toast",
                "Toast the bread.\nSpread the peanut butter on the toast.\nSlice the banana and arrange it on top.",
                ing("bread", 2.0, "pcs"),
                ing("peanut butter", 30.0, "g"),
                ing("banana", 1.0, "pcs"));

        add(dao, "Tuna Mayo Salad",
                "Drain the tuna and flake it into a bowl.\nDice the onion and add it with the mayonnaise.\nSqueeze in the lemon juice, mix well and serve.",
                ing("tuna", 1.0, "pcs"),
                ing("mayonnaise", 30.0, "g"),
                ing("onion", 1.0, "pcs"),
                ing("lemon", 1.0, "pcs"));

        add(dao, "Scrambled Eggs on Toast",
                "Beat the eggs with the salt.\nMelt the butter in a pan over low heat.\nStir the eggs gently until just set.\nServe on the toasted bread.",
                ing("egg", 3.0, "pcs"),
                ing("bread", 2.0, "pcs"),
                ing("butter", 10.0, "g"),
                ing("salt", 5.0, "g"));

        add(dao, "Tomato Soup",
                "Chop the onion, garlic and tomatoes.\nSoften the onion and garlic in olive oil.\nAdd the tomatoes and salt and simmer for 20 minutes.\nBlend until smooth and serve.",
                ing("tomato", 6.0, "pcs"),
                ing("onion", 1.0, "pcs"),
                ing("garlic", 2.0, "pcs"),
                ing("olive oil", 15.0, "ml"),
                ing("salt", 5.0, "g"));

        add(dao, "Banana Oatmeal",
                "Heat the milk in a saucepan.\nStir in the oats and cook for 5 minutes.\nTop with sliced banana and a drizzle of honey.",
                ing("oat", 80.0, "g"),
                ing("milk", 250.0, "ml"),
                ing("banana", 1.0, "pcs"),
                ing("honey", 20.0, "g"));

        add(dao, "Chicken Rice Bowl",
                "Cook the rice.\nSlice the chicken and onion and fry until the chicken is cooked through.\nAdd the soy sauce and stir for a minute.\nServe the chicken over the rice.",
                ing("chicken breast", 300.0, "g"),
                ing("rice", 200.0, "g"),
                ing("soy sauce", 30.0, "ml"),
                ing("onion", 1.0, "pcs"));

        add(dao, "Garlic Butter Pasta",
                "Boil the pasta in salted water.\nMelt the butter and fry the sliced garlic gently.\nToss the drained pasta in the garlic butter and serve.",
                ing("pasta", 200.0, "g"),
                ing("butter", 30.0, "g"),
                ing("garlic", 3.0, "pcs"),
                ing("salt", 5.0, "g"));

        add(dao, "Cheese Toastie",
                "Butter the outside of the bread slices.\nPlace the cheese between them.\nFry on both sides until golden and the cheese has melted.",
                ing("bread", 2.0, "pcs"),
                ing("cheese", 60.0, "g"),
                ing("butter", 10.0, "g"));

        add(dao, "Potato Wedges",
                "Cut the potatoes into wedges.\nToss them with the olive oil, salt and paprika.\nRoast at 220 C for 30 minutes, turning once.",
                ing("potato", 4.0, "pcs"),
                ing("olive oil", 30.0, "ml"),
                ing("salt", 5.0, "g"),
                ing("paprika", 5.0, "g"));

        add(dao, "Vegetable Stir Fry",
                "Slice the carrots, onion and bell pepper.\nHeat the oil in a wok.\nStir fry the vegetables for 5 minutes.\nAdd the soy sauce, toss and serve.",
                ing("carrot", 2.0, "pcs"),
                ing("onion", 1.0, "pcs"),
                ing("bell pepper", 1.0, "pcs"),
                ing("soy sauce", 30.0, "ml"),
                ing("oil", 15.0, "ml"));

        add(dao, "French Toast",
                "Whisk the eggs, milk and sugar together.\nDip each bread slice in the mixture.\nFry until golden on both sides.",
                ing("bread", 4.0, "pcs"),
                ing("egg", 2.0, "pcs"),
                ing("milk", 100.0, "ml"),
                ing("sugar", 15.0, "g"));

        add(dao, "Simple Guacamole",
                "Mash the avocados in a bowl.\nDice the tomato and stir it in.\nAdd the lemon juice and salt and mix well.",
                ing("avocado", 2.0, "pcs"),
                ing("lemon", 1.0, "pcs"),
                ing("tomato", 1.0, "pcs"),
                ing("salt", 5.0, "g"));

        add(dao, "Lentil Soup",
                "Chop the onion, carrots and garlic.\nFry them in a pot until soft.\nAdd the lentils, salt and enough water to cover.\nSimmer for 30 minutes until the lentils are tender.",
                ing("lentil", 200.0, "g"),
                ing("onion", 1.0, "pcs"),
                ing("carrot", 2.0, "pcs"),
                ing("garlic", 2.0, "pcs"),
                ing("salt", 5.0, "g"));

        add(dao, "Rice Pudding",
                "Combine the rice, milk and sugar in a saucepan.\nSimmer gently for 30 minutes, stirring often.\nServe warm or chilled.",
                ing("rice", 100.0, "g"),
                ing("milk", 500.0, "ml"),
                ing("sugar", 45.0, "g"));

    }

    /** Inserts one recipe, then its ingredient lines linked by the new recipe id. */
    private static void add(RecipeDao dao, String name, String steps, RecipeIngredient... ingredients) {
        long recipeId = dao.insertRecipe(new Recipe(name, steps));
        List<RecipeIngredient> list = new ArrayList<>();
        for (RecipeIngredient ingredient : ingredients) {
            ingredient.recipeId = recipeId;
            list.add(ingredient);
        }
        dao.insertIngredients(list);
    }

    private static RecipeIngredient ing(String name, double quantity, String unit) {
        return new RecipeIngredient(0, name, quantity, unit);
    }
}
