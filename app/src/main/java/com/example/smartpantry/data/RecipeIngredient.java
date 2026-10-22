package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One ingredient line of a recipe, e.g. "200 g flour".
 * Deleting a recipe automatically deletes its ingredient lines (CASCADE).
 */
@Entity(
        tableName = "recipe_ingredients",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("recipeId")})
public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /** The recipe this line belongs to. */
    public long recipeId;

    @NonNull
    public String name = "";

    /** Amount needed, in the unit below. */
    public double quantity;

    @NonNull
    public String unit = "";

    /** Required by Room. */
    public RecipeIngredient() { }

    @Ignore
    public RecipeIngredient(long recipeId, @NonNull String name, double quantity, @NonNull String unit) {
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}
