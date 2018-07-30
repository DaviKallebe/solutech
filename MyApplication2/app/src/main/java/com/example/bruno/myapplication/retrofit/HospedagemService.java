package com.example.bruno.myapplication.retrofit;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HospedagemService {
    @GET("hospedagem/pedinte/{id_user_pedinte}")
    Flowable<List<Hospedagem>> selecionarHospedagemPedinte(@Path("id_user_pedinte") Integer id_user);

    @GET("hospedagem/hospedador/{id_user_hospedador}")
    Flowable<List<Hospedagem>> selecionarHospedagemHospedador(@Path("id_user_hospedador") Integer id_user);

    @POST("hospedagem/criar")
    Observable<Hospedagem> novaHospedagem(@Body RequestBody body);

    @PUT("hospedagem/atualizar")
    Maybe<Hospedagem> atualizarHospedagem(@Body RequestBody body);

    @GET("hospedagem/pets/{id_pets}")
    Flowable<List<Pet>> selecionarPetHospedagem(@Path("id_pets") String id_pets);
}
