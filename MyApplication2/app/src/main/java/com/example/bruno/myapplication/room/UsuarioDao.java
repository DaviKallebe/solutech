package com.example.bruno.myapplication.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.bruno.myapplication.retrofit.Usuario;

import io.reactivex.Single;

@Dao
public interface UsuarioDao {

    @Query("SELECT * FROM Usuario WHERE id_user = :id_user")
    LiveData<Usuario> getCurrentUser(int id_user);

    @Query("SELECT * FROM Usuario WHERE id_user = :id_user")
    Single<Usuario> getUser(int id_user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setCurrentUser(Usuario user);

    @Update
    void updateCurrentUser(Usuario user);
}
