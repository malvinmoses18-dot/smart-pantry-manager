package com.example.smartpantry.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The single Room database for the app (SQLite under the hood).
 * Recipe tables are added in a later step, which bumps the version number.
 */
@Database(entities = {PantryItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();

    private static volatile AppDatabase INSTANCE;

    /** Room does not allow database work on the main (UI) thread, so writes run here. */
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
                }
            }
        }
        return INSTANCE;
    }
}
