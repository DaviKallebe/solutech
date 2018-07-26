package com.example.bruno.myapplication.retrofit;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ObservableUsarioService {
    @GET("firebaselogin?")
    Single<Usuario> doFirebaseLogin(@Query("firebaseUid") String firebaseUid);

    @POST("createfirebaseuser?")
    Single<Usuario> doFirebaseCreateUser(@Query("firebaseUid") String firebaseUid,
                                         @Body RequestBody object);

    @GET("/search/user/all")
    Flowable<List<Hospedador>> searchUsers(@Query("id_user") Integer id_user,
                                        @Query("nome") String name);

    @GET("user/get_comments/{id_user}")
    Flowable<List<Comentario>> getComments(@Path("id_user") Integer id_user);

    @GET("user/get_messages/{id_user}")
    Observable<List<Mensagem>> getMessages(@Path("id_user") Integer id_user);

    @GET("user/profile/get/{id_user}")
    Observable<Usuario> getProfile(@Path("id_user") Integer id_user,
                                   @Query("updatedAt") Date updatedAt);

    @GET("user/profile/get/{id_user}")
    Flowable<Response<Usuario>> getResponseProfile(@Path("id_user") Integer id_user,
                                                   @Query("updatedAt") Date updatedAt);

    @Multipart
    @PUT("user/profile/update/")
    Observable<Usuario> updateUserProfile(@PartMap Map<String, RequestBody> object,
                                          @Part MultipartBody.Part image);

    @PUT("user/profile/update/")
    Observable<Usuario> updateUserProfile(@Body RequestBody object);

    @GET("user/pet/getlist/{id_user}")
    Flowable<Response<List<Pet>>> getPetList(@Path("id_user") Integer id_user);

    @Multipart
    @PUT("user/pet/update/")
    Observable<Pet> updateUserPet(@PartMap Map<String, RequestBody> object,
                                  @Part MultipartBody.Part image);

    @PUT("user/pet/update/")
    Observable<Pet> updateUserPet(@Body RequestBody object);

    @POST("user/pet/create/")
    Observable<Pet> createUserPet(@Body RequestBody body);

    @GET("user/get/{nome}")
    Flowable<List<Usuario>> getUserByName(@Path("nome") String nome);

    @GET("user/host/get/:id_user")
    Flowable<Usuario> getHospedador(@Path("id_user") Integer id_user);

    @POST("/user/host/create")
    Observable<Hospedador> createHospedador(@Body RequestBody body);

    @PUT("/user/host/update")
    Observable<Hospedador> updateHospedador(@Body RequestBody body);
}
