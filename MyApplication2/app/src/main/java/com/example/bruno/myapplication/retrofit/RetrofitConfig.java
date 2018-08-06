package com.example.bruno.myapplication.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private static String IP = "192.168.1.15";//"52.67.64.207";
    private static String PORT = "3000";
    private static String URL = "http://" + RetrofitConfig.IP + ":" + RetrofitConfig.PORT;

    private Retrofit ObsRetrofit;

    public RetrofitConfig() {
        RxJava2CallAdapterFactory rxAdapter =
                RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        this.ObsRetrofit = new Retrofit.Builder()
                .baseUrl(RetrofitConfig.URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ObservableUsarioService getObservableUsuarioService() {
        return this.ObsRetrofit.create(ObservableUsarioService.class);
    }

    public HospedagemService getHospedagemService() {
        return this.ObsRetrofit.create(HospedagemService.class);
    }

    public LogradouroService getLogradouroService() {
        return this.ObsRetrofit.create(LogradouroService.class);
    }
}
