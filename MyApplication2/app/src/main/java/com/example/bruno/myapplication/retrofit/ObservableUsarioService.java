package com.example.bruno.myapplication.retrofit;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ObservableUsarioService {
    @GET("firebaselogin?")
    Single<Usuario> doFirebaseLogin(@Query("firebaseUid") String firebaseUid);

    @POST("createfirebaseuser?")
    Single<Usuario> doFirebaseCreateUser(@Query("firebaseUid") String firebaseUid,
                                         @Body RequestBody object);

    @GET("user/list_all")
    Observable<List<Usuario>> listUsers();

    @GET("user/get_comments/{id_user}")
    Observable<List<Comentario>> getComments(@Path("id_user") Integer id_user);

    @GET("user/get_messages/{id_user}")
    Observable<List<Mensagem>> getMessages(@Path("id_user") Integer id_user);
}
