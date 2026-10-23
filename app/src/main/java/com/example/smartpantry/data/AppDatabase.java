package com.example.smartpantry.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {PantryItem.class, Recipe.class, RecipeIngredient.class},
        version = 2,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();

    public abstract RecipeDao recipeDao();

    private static volatile AppDatabase INSTANCE;

   
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(2);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "smart_pantry.db")
                          
                            .fallbackToDestructiveMigration()
                            .build();

                    
                    final RecipeDao recipeDao = INSTANCE.recipeDao();
                    databaseExecutor.execute(() -> RecipeSeeder.seedIfEmpty(recipeDao));
                }
            }
        }
        return INSTANCE;
    }
}
