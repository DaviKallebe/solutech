package com.example.bruno.myapplication.retrofit;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioService {
    @GET("login")
    Call<Usuario> doNormalLogin(@Query("email") String email,
                                      @Query("pword") String password);

    @GET("loginfacebook")
    Call<Usuario> doFacebookLogin(@Query("facebookId") String facebookId);

    @POST("createnormaluser")
    Call<Usuario> createNewUser(@Body RequestBody object);

    @POST("createfacebookuser")
    Call<Usuario> createNewFacebookUser(@Body RequestBody object);

    @GET("user/list_all")
    Call<List<Usuario>> listUsers();

    @GET("user/get_comments/{id_user}")
    Call<List<Comentario>> getComments(@Path("id_user") Integer id_user);

    @GET("user/get_messages/{id_user}")
    Call<List<Mensagem>> getMessages(@Path("id_user") Integer id_user);
}
