package com.example.bruno.myapplication.repository;

import com.example.bruno.myapplication.retrofit.Hospedagem;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.room.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class HospedagemRepository {
    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    public HospedagemRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
        this.compositeDisposable = new CompositeDisposable();
    }

    public Flowable<List<Hospedagem>> selecionarHospedagemPedinte(Integer id_user) {
        return new RetrofitConfig()
                .getHospedagemService()
                .selecionarHospedagemPedinte(id_user);
    }

    public Flowable<List<Hospedagem>> selecionarHospedagemHospedador(Integer id_user) {
        return new RetrofitConfig()
                .getHospedagemService()
                .selecionarHospedagemHospedador(id_user);
    }

    public Observable<Hospedagem> novaHospedagem(Hospedagem hospedagem) {
        return new RetrofitConfig()
                .getHospedagemService()
                .novaHospedagem(hospedagem.generateRequestBody());
    }

    public Observable<Hospedagem> atualizarHospedagem(Hospedagem hospedagem) {
        return new RetrofitConfig()
                .getHospedagemService()
                .atualizarHospedagem(hospedagem.generateRequestBody());
    }
}
