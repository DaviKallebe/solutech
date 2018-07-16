package com.example.bruno.myapplication.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.bruno.myapplication.retrofit.Usuario;

@Database(entities = {Usuario.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao getUsuarioDao();
}
