package com.example.smartpantry.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object: the CRUD operations for the pantry_items table.
 * Create = insert, Read = getAllLive/getById, Update = update, Delete = delete.
 */
@Dao
public interface PantryDao {

    // ---- Create ----
    @Insert
    long insert(PantryItem item);

    // ---- Read ----
    /** Observed by the Pantry List screen; the list refreshes automatically on any change. */
    @Query("SELECT * FROM pantry_items ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<PantryItem>> getAllLive();

    /** Plain list for the matching logic. Must be called off the main thread. */
    @Query("SELECT * FROM pantry_items")
    List<PantryItem> getAllNow();

    /** Used by the Edit screen to load one item. Must be called off the main thread. */
    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItem getById(long id);

    // ---- Update ----
    @Update
    void update(PantryItem item);

    // ---- Delete ----
    @Delete
    void delete(PantryItem item);
}
