package com.example.bruno.myapplication.retrofit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioService {

        @GET("/login")
        Call<Usuario> doNormalLogin(@Query("email") String email,
                                    @Query("password") String password);


        @POST("/createnormaluser")
        Call<Usuario> createNewUser(@Body RequestBody object);
}
