package com.example.bruno.myapplication.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface LogradouroService {

    @GET("logradouro/selecionar/{id_user}")
    Observable<Logradouro> selecionarLogradouro(@Path("id_user") Integer id_user);

    @Multipart
    @POST("logradouro/criar")
    Observable<Logradouro> criarLogradouro(@PartMap Map<String, RequestBody> body,
                                       @Part MultipartBody.Part image);

    @Multipart
    @PUT("logradouro/atualizar")
    Observable<Logradouro> atualizarLogradouro(@PartMap Map<String, RequestBody> body,
                                               @Part MultipartBody.Part image);
}
