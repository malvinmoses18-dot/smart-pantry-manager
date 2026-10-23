package com.example.smartpantry.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The single Room database for the app (SQLite under the hood).
 * Tables: pantry_items, recipes, recipe_ingredients.
 */
@Database(
        entities = {PantryItem.class, Recipe.class, RecipeIngredient.class},
        version = 2,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();

    public abstract RecipeDao recipeDao();

    private static volatile AppDatabase INSTANCE;

    /** Room does not allow database work on the main (UI) thread, so it runs here. */
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(2);

    /** Returns the one shared database instance, creating it on first use. */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "smart_pantry.db")
                            // Acceptable during development: rebuilds the DB when the schema changes.
                            .fallbackToDestructiveMigration()
                            .build();

                    // On the first run the recipe table is empty, so load the recipe collection.
                    final RecipeDao recipeDao = INSTANCE.recipeDao();
                    databaseExecutor.execute(() -> RecipeSeeder.seedIfEmpty(recipeDao));
                }
            }
        }
        return INSTANCE;
    }
}
