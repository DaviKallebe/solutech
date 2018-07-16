package com.example.bruno.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bruno.myapplication.commons.ResourceState;
import com.example.bruno.myapplication.repository.UsuarioRepository;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.example.bruno.myapplication.room.AppDatabase;

import java.io.ByteArrayOutputStream;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class LogadoViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private UsuarioRepository mUserRepository;
    private LiveData<ResourceState<Usuario>> currentUser;

    public LogadoViewModel(@NonNull Application application) {
        super(application);

        appDatabase = Room.databaseBuilder(
                    this.getApplication(),
                    AppDatabase.class,
                    "dbCacheData")
                .fallbackToDestructiveMigration()
                .build();

        mUserRepository = new UsuarioRepository(appDatabase);
    }

    public void loadCurrentUser(int id_user) {
        this.currentUser = mUserRepository.getCurrentUser(id_user);
    }

    public LiveData<ResourceState<Usuario>> getCurrentUser() {
        return this.currentUser;
    }

    //image perfil
    public void updateProfile(int id_user, ByteArrayOutputStream image) {
        mUserRepository.updateProfile(id_user, "imagem", image);
    }

    public <T> void updateProfile(int id_user, String fieldName, T fieldValue) {
        mUserRepository.updateProfile(id_user, fieldName, fieldValue);
    }
}
