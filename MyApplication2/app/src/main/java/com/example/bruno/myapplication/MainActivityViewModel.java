package com.example.bruno.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.example.bruno.myapplication.commons.ResourceState;
import com.example.bruno.myapplication.repository.HospedagemRepository;
import com.example.bruno.myapplication.repository.UsuarioRepository;
import com.example.bruno.myapplication.retrofit.Comentario;
import com.example.bruno.myapplication.retrofit.Hospedador;
import com.example.bruno.myapplication.retrofit.Hospedagem;
import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.example.bruno.myapplication.room.AppDatabase;

import java.io.ByteArrayOutputStream;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class MainActivityViewModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private UsuarioRepository mUserRepository;
    private HospedagemRepository mHospedagemRepository;
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
        mHospedagemRepository = new HospedagemRepository(appDatabase);
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

    public Flowable<List<Comentario>> getComments(Integer id_user) {
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

        this.hospedador.updateFields(hospedador);
    }

    public Observable<Hospedador> createHospedador() {
        if (this.hospedador != null) {
            return mUserRepository.createHospedador(this.hospedador);
        }

        return null;
    }

    public Flowable<List<Hospedador>> searchUsers(Integer id_user, String nome) {
        return mUserRepository.searchUsers(id_user, nome);
    }

    public Observable<Hospedagem> novaHospedagem(Hospedagem hospedagem) {
        return mHospedagemRepository.novaHospedagem(hospedagem);
    }

    public Flowable<List<Hospedagem>> selecionarHospedagemHospedador(Integer id_user) {
        return mHospedagemRepository.selecionarHospedagemHospedador(id_user);
    }

    public Flowable<List<Hospedagem>> selecionarHospedagemPedinte(Integer id_user) {
        return mHospedagemRepository.selecionarHospedagemPedinte(id_user);
    }
}
