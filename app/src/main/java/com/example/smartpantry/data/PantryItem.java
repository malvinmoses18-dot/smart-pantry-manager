package com.example.smartpantry.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * One ingredient the user currently has at home.
 * Stored in the "pantry_items" table.
 */
@Entity(tableName = "pantry_items")
public class PantryItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /** Ingredient name exactly as the user typed it, e.g. "Tomatoes". */
    @NonNull
    public String name = "";

    /** Amount in the unit below, e.g. 500 for "500 g". */
    public double quantity;

    /** Unit text, e.g. "g", "kg", "ml", "l", "pcs". */
    @NonNull
    public String unit = "";

    /** Expiry date as epoch milliseconds, or null if the user did not set one. */
    @Nullable
    public Long expiryDate;

    /** Required by Room. */
    public PantryItem() { }

    /** Convenience constructor used when creating items in code. */
    @Ignore
    public PantryItem(@NonNull String name, double quantity,
                      @NonNull String unit, @Nullable Long expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }
}
