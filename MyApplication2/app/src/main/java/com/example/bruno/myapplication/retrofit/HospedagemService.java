package com.example.bruno.myapplication.retrofit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HospedagemService {
    @GET("hospedagem/{id_user_pedinte}")
    Flowable<Hospedagem> selecionarHospedagemPedinte(@Path("id_user_pedinte") Integer id_user);

    @GET("hospedagem/{id_user_hospedador}")
    Flowable<Hospedagem> selecionarHospedagemHospedador(@Path("id_user_hospedador") Integer id_user);

    @POST("hospedagem/criar")
    Observable<Hospedagem> novaHospedagem(@Body RequestBody body);

    @PUT("hospedagem/atualizar")
    Observable<Hospedagem> atualizarHospedagem(@Body RequestBody body);
}
