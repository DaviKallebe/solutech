package com.example.bruno.myapplication.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.bruno.myapplication.Login;
import com.example.bruno.myapplication.commons.NetworkBoundSource;
import com.example.bruno.myapplication.commons.ResourceState;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.example.bruno.myapplication.room.AppDatabase;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;


public class UsuarioRepository {

    private AppDatabase appDatabase;
    private int retry;

    public UsuarioRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public LiveData<ResourceState<Usuario>> getCurrentUser(int id_user) {
        return new NetworkBoundSource<Usuario, Usuario>() {
            @Override
            protected void saveCallResult(@NonNull Usuario item) {
                appDatabase.getUsuarioDao().setCurrentUser(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Usuario data) {
                return data == null || true;
            }

            @NonNull
            @Override
            protected LiveData<Usuario> loadFromDb() {
                return appDatabase.getUsuarioDao().getCurrentUser(id_user);
            }

            @NonNull
            @Override
            protected LiveData<Response<Usuario>> createCall() {
                return LiveDataReactiveStreams.fromPublisher(
                        new RetrofitConfig()
                                .getObservableUsuarioService()
                                .getResponseProfile(id_user, null)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(Schedulers.newThread())
                                .onExceptionResumeNext(next -> UsuarioRepository.handleError("FALHOU")));
            }
        }.getAsLiveData();
    }

    @NonNull
    private RequestBody createPart(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private RequestBody createPart(Integer descriptionInteger) {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), Integer.toString(descriptionInteger));
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String formName, ByteArrayOutputStream file) {
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/jpeg"),
                        file.toByteArray()
                );

        return MultipartBody.Part.createFormData(formName, "camera.jpg", requestFile);
    }

    public void updateCurrentUser(Usuario user) {
        AsyncTask.execute(() -> appDatabase.getUsuarioDao().updateCurrentUser(user));
    }

    public void updateProfile(Integer id_user, String formName, ByteArrayOutputStream image) {
        MultipartBody.Part body = prepareFilePart("imagem", image);
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("id_user", createPart(id_user));

        Observable<Usuario> updateProfile = new RetrofitConfig()
                .getObservableUsuarioService()
                .updateUserProfile(map, body);

        Disposable disposable = updateProfile.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::updateCurrentUser)
                .subscribe();
    }

    private void handleInternalError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;

            if (httpException.code() == 404) {
                //
            }
        }
    }

    public static void handleError(Throwable e) {
        Log.e(UsuarioRepository.class.getSimpleName(), e.getMessage());
    }

    public static void handleError(String msg) {
        Log.e(UsuarioRepository.class.getSimpleName(), msg);
    }
}
