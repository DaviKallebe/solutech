package com.example.bruno.myapplication.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.bruno.myapplication.retrofit.Hospedador;

@Dao
public interface HospedadorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHospedador(Hospedador hospedador);

    @Query("SELECT * FROM Hospedador LIMIT 1")
    LiveData<Hospedador> getHospedador();
}
