package com.example.bruno.myapplication.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.Usuario;

@Database(entities = {Usuario.class, Pet.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao getUsuarioDao();
    public abstract PetDao getPetDao();
}
