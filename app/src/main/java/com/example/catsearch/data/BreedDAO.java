package com.example.catsearch.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.catsearch.Breed;

import java.util.List;

/*
 *  BreedDAO.java
 *  Room (DAO - Data Access Object)
 *  Here you can define all the Database queries in form of Java methods.
 */

@Dao
public interface BreedDAO {

    // Query to get a list of all breeds:
    @Query("SELECT * FROM Breeds")
    List<Breed> getAllBreeds();

    // Query to get a breed by its ID:
    @Query("SELECT * FROM Breeds WHERE id = :id")
    Breed getBreedByID(String id);

    // Query to insert an Array of breeds:
    @Insert(onConflict = OnConflictStrategy.REPLACE)                                                // I need this "onConflict", because otherwise the second time it tries to load data into the database, it can't save the data, because the primary keys (id) already exist in the database.
    void insert(Breed[] b);

    // Query to set a certain breed to be a favourite (boolean):                                    --> '0' for false and '1' for true
    @Query("UPDATE Breeds SET favourite = :yesORno WHERE id = :id")
    void setFavouriteValueByID(String id, int yesORno);

    // Query to get a certain favourite breed:
    @Query("SELECT favourite FROM Breeds WHERE id = :id")
    int getFavouriteValueByID(String id);

    // Query to get all favourite breeds:
    @Query("SELECT * FROM Breeds WHERE favourite = 1")
    Breed[] getAllFavourites();
}