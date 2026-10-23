package com.example.smartpantry.logic;

import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.data.Recipe;
import com.example.smartpantry.data.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The core business logic of the app: the strict-matching rule.
 *
 * A recipe is suggested ONLY if every ingredient it needs is in the pantry in at
 * least the required quantity. One missing or short ingredient excludes the recipe.
 *
 * To cope with real-world messiness it:
 *  - ignores case, extra spaces and punctuation in names;
 *  - treats singular and plural names as the same ("tomato" = "tomatoes");
 *  - converts compatible units (1 kg = 1000 g, 1 l = 1000 ml, 1 cup = 240 ml);
 *  - adds up several pantry entries of the same ingredient.
 *
 * This class has no Android code, so it can be unit tested on a normal JVM.
 */
public final class IngredientMatcher {

    private static final double EPSILON = 1e-6;

    private static final String MASS = "mass";
    private static final String VOLUME = "volume";
    private static final String COUNT = "count";

    private IngredientMatcher() { }

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    /** Returns only the recipes the user can cook right now from the pantry. */
    public static List<Recipe> suggest(List<Recipe> recipes,
                                       Map<Long, List<RecipeIngredient>> ingredientsByRecipe,
                                       List<PantryItem> pantry) {
        List<Recipe> suggested = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<RecipeIngredient> needed = ingredientsByRecipe.get(recipe.id);
            if (needed != null && canMake(needed, pantry)) {
                suggested.add(recipe);
            }
        }
        return suggested;
    }

    /** True only if EVERY required ingredient is available in the required quantity. */
    public static boolean canMake(List<RecipeIngredient> needed, List<PantryItem> pantry) {
        if (needed.isEmpty()) {
            return false;
        }
        for (RecipeIngredient ingredient : needed) {
            if (!hasEnough(ingredient, pantry)) {
                return false;
            }
        }
        return true;
    }

    /** Adds up every pantry entry with the same ingredient and compares with the amount needed. */
    public static boolean hasEnough(RecipeIngredient needed, List<PantryItem> pantry) {
        Set<String> neededNames = nameVariants(needed.name);
        Measure required = toBase(needed.quantity, needed.unit);

        double available = 0;
        for (PantryItem item : pantry) {
            if (!sharesName(neededNames, nameVariants(item.name))) {
                continue;
            }
            Measure have = toBase(item.quantity, item.unit);
            // Only add amounts in the same family (grams with grams, millilitres with millilitres).
            // Incompatible units such as "3 pcs" against "200 g" are never guessed at, so a
            // recipe is never suggested by mistake.
            if (have.family.equals(required.family)) {
                available += have.value;
            }
        }
        return available + EPSILON >= required.value;
    }

    // ------------------------------------------------------------------
    // Name normalisation
    // ------------------------------------------------------------------

    /**
     * All spellings an ingredient name could be known by. Two names match when their
     * sets share at least one entry, e.g. "Tomatoes" gives {tomatoes, tomatoe, tomato},
     * which overlaps with "tomato".
     */
    public static Set<String> nameVariants(String rawName) {
        Set<String> variants = new HashSet<>();
        if (rawName == null) {
            return variants;
        }

        String name = rawName.toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (name.isEmpty()) {
            return variants;
        }
        variants.add(name);

        // Only the last word changes between singular and plural ("cherry tomatoes").
        int lastSpace = name.lastIndexOf(' ');
        String prefix = lastSpace >= 0 ? name.substring(0, lastSpace + 1) : "";
        String last = lastSpace >= 0 ? name.substring(lastSpace + 1) : name;

        boolean looksPlural = last.length() > 3
                && last.endsWith("s")
                && !last.endsWith("ss")
                && !last.endsWith("us")
                && !last.endsWith("is");
        if (looksPlural) {
            variants.add(prefix + last.substring(0, last.length() - 1));           // eggs -> egg
            if (last.endsWith("es")) {
                variants.add(prefix + last.substring(0, last.length() - 2));       // tomatoes -> tomato
            }
            if (last.endsWith("ies")) {
                variants.add(prefix + last.substring(0, last.length() - 3) + "y"); // berries -> berry
            }
        }
        return variants;
    }

    private static boolean sharesName(Set<String> a, Set<String> b) {
        for (String variant : a) {
            if (b.contains(variant)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Unit conversion
    // ------------------------------------------------------------------

    /** An amount converted to a base unit: grams, millilitres, or a plain count. */
    public static final class Measure {
        public final String family;
        public final double value;

        Measure(String family, double value) {
            this.family = family;
            this.value = value;
        }
    }

    /** Converts an amount and unit text to grams, millilitres or a count. */
    public static Measure toBase(double quantity, String rawUnit) {
        String unit = rawUnit == null ? "" : rawUnit.toLowerCase(Locale.ROOT).trim().replace(".", "");
        switch (unit) {
            case "g": case "gram": case "grams":
                return new Measure(MASS, quantity);
            case "kg": case "kgs": case "kilogram": case "kilograms":
                return new Measure(MASS, quantity * 1000);
            case "oz": case "ounce": case "ounces":
                return new Measure(MASS, quantity * 28.3495);
            case "lb": case "lbs": case "pound": case "pounds":
                return new Measure(MASS, quantity * 453.592);

            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters":
                return new Measure(VOLUME, quantity);
            case "l": case "litre": case "litres": case "liter": case "liters":
                return new Measure(VOLUME, quantity * 1000);
            case "tsp": case "teaspoon": case "teaspoons":
                return new Measure(VOLUME, quantity * 5);
            case "tbsp": case "tablespoon": case "tablespoons":
                return new Measure(VOLUME, quantity * 15);
            case "cup": case "cups":
                return new Measure(VOLUME, quantity * 240);

            case "": case "pcs": case "pc": case "piece": case "pieces":
            case "unit": case "units": case "whole": case "x":
                return new Measure(COUNT, quantity);

            default:
                // Unknown unit (e.g. "clove"): only comparable with the same unit.
                String singular = unit.endsWith("s") && unit.length() > 2
                        ? unit.substring(0, unit.length() - 1) : unit;
                return new Measure("other:" + singular, quantity);
        }
    }
}
