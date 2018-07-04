package com.example.bruno.myapplication.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private Retrofit retrofit;
    private Retrofit ObsRetrofit;
    private RxJava2CallAdapterFactory rxAdapter;

    public RetrofitConfig() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.10:3000/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        this.ObsRetrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.10:3000/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    public UsuarioService getUsuarioService() {
        return this.retrofit.create(UsuarioService.class);
    }

    public ObservableUsarioService getObservableUsuarioService() {
        return this.ObsRetrofit.create(ObservableUsarioService.class);
    }
}
