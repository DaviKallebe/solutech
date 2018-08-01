package com.example.bruno.myapplication.repository;

import com.example.bruno.myapplication.retrofit.Logradouro;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;
import io.reactivex.Single;

public class LogradouroRepository {

    public Observable<Logradouro> selecionarLogradouro(Integer id_user) {
        return new RetrofitConfig()
                .getLogradouroService()
                .selecionarLogradouro(id_user);
    }

    public Observable<Logradouro> atualizarLogradouro(Logradouro logradouro, ByteArrayOutputStream image) {
        return new RetrofitConfig()
                .getLogradouroService()
                .atualizarLogradouro(logradouro.getHashMapStringRequestBody(), null);
    }

    public Observable<Logradouro> criarLogradouro(Logradouro logradouro, ByteArrayOutputStream image) {
        return new RetrofitConfig()
                .getLogradouroService()
                .criarLogradouro(logradouro.getHashMapStringRequestBody(), null);
    }
}
