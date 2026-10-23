package com.example.smartpantry.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {


    @Insert
    long insert(PantryItem item);

    @Query("SELECT * FROM pantry_items ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<PantryItem>> getAllLive();


    @Query("SELECT * FROM pantry_items")
    List<PantryItem> getAllNow();

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItem getById(long id);


    @Update
    void update(PantryItem item);

    
    @Delete
    void delete(PantryItem item);
}
