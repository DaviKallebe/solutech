package com.example.bruno.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.example.bruno.myapplication.commons.ResourceState;
import com.example.bruno.myapplication.repository.UsuarioRepository;
import com.example.bruno.myapplication.retrofit.Comentario;
import com.example.bruno.myapplication.retrofit.Hospedador;
import com.example.bruno.myapplication.retrofit.Mensagem;
import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.example.bruno.myapplication.room.AppDatabase;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class MainActivityViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private UsuarioRepository mUserRepository;
    private LiveData<ResourceState<Usuario>> currentUser;
    private LiveData<ResourceState<List<Pet>>> userPetList;
    private Integer id_user;

    private Hospedador hospedador;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        appDatabase = Room.databaseBuilder(
                    this.getApplication(),
                    AppDatabase.class,
                    "dbCacheData")
                .fallbackToDestructiveMigration()
                .build();

        mUserRepository = new UsuarioRepository(appDatabase);
    }

    public void loadCurrentUser(Integer id_user) {
        this.currentUser = mUserRepository.getCurrentUser(id_user);
    }

    public void loadPetList(int id_user) {
        this.userPetList = mUserRepository.getPetList(id_user);
    }

    public LiveData<ResourceState<Usuario>> getCurrentUser() {
        return this.currentUser;
    }

    public LiveData<ResourceState<List<Pet>>> getPetList() {
        return this.userPetList;
    }

    public LiveData<Pet> getPet(Integer id_pet) {
        return mUserRepository.getPet(id_pet);
    }

    public void inserPet(Pet pet) {
        mUserRepository.createNewPet(pet);
    }

    public Flowable<List<Usuario>> getUsersByName(String nome) {
        return null;
    }

    public Flowable<List<Comentario>> getMessages() {
        return mUserRepository.getComments(id_user);
    }

    //image perfil
    public void updateProfile(int id_user, ByteArrayOutputStream image) {
        mUserRepository.updateProfile(id_user, "imagem", image);
    }

    public <T> void updateProfile(int id_user, String fieldName, T fieldValue) {
        mUserRepository.updateProfile(id_user, fieldName, fieldValue);
    }

    public void updateRoomProfile(Usuario usuario) {
        appDatabase.getUsuarioDao().updateCurrentUser(usuario);
    }

    public void updateHospedadorCadastro(Hospedador hospedador) {
        if (this.hospedador == null)
            this.hospedador = new Hospedador();

        try {
            Field[] fields = hospedador.getClass().getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    Object fieldValue = field.get(hospedador);

                    if (fieldValue != null) {
                        field.set(this.hospedador, fieldValue);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Observable<Hospedador> createHospedador() {
        if (this.hospedador != null) {
            return mUserRepository.createHospedador(this.hospedador);
        }

        return null;
    }
}
