package com.example.bruno.myapplication.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.bruno.myapplication.retrofit.Pet;

import java.util.List;

@Dao
public interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserPets(Pet... pets);

    @Query("SELECT * FROM Pet")
    LiveData<List<Pet>> gePetList();

    @Query("SELECT * FROM Pet WHERE id_pet = :id_pet")
    LiveData<Pet> getPet(Integer id_pet);
}
