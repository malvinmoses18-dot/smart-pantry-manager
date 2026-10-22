package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/** A recipe. Its ingredients live in the recipe_ingredients table. */
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name = "";

    /** Preparation steps, one step per line. */
    @NonNull
    public String steps = "";

    /** Required by Room. */
    public Recipe() { }

    @Ignore
    public Recipe(@NonNull String name, @NonNull String steps) {
        this.name = name;
        this.steps = steps;
    }
}
