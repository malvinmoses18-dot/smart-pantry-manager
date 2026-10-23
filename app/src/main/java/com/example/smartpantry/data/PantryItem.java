package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name = "";

    public double quantity;


    @NonNull
    public String unit = "";

    @Nullable
    public Long expiryDate;

   
    public PantryItem() { }

    @Ignore
    public PantryItem(@NonNull String name, double quantity,
                      @NonNull String unit, @Nullable Long expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }
}
