package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name = "";

    @NonNull
    public String steps = "";

   
    public Recipe() { }

    @Ignore
    public Recipe(@NonNull String name, @NonNull String steps) {
        this.name = name;
        this.steps = steps;
    }
}
