package com.example.bruno.myapplication.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bruno.myapplication.commons.NetworkBoundSource;
import com.example.bruno.myapplication.commons.ResourceState;
import com.example.bruno.myapplication.retrofit.Comentario;
import com.example.bruno.myapplication.retrofit.Hospedador;
import com.example.bruno.myapplication.retrofit.Mensagem;
import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.example.bruno.myapplication.retrofit.Usuario;
import com.example.bruno.myapplication.room.AppDatabase;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;


public class UsuarioRepository {

    private AppDatabase appDatabase;
    private CompositeDisposable compositeDisposable;

    public UsuarioRepository(AppDatabase appDatabase) {
        this.compositeDisposable = new CompositeDisposable();
        this.appDatabase = appDatabase;
    }

    @NonNull
    private RequestBody createPart(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private RequestBody createPart(Integer descriptionInteger) {
        return RequestBody.create(
                MultipartBody.FORM, Integer.toString(descriptionInteger));
        //MediaType.parse("multipart/form-data"), Integer.toString(descriptionInteger));
    }

    @NonNull
    private RequestBody createPart(Date descriptionDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, df.format(descriptionDate));
    }

    @NonNull
    private RequestBody createPart(Double descriptionReal) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, Double.toString(descriptionReal));
    }

    @NonNull
    private RequestBody createPart(Boolean descriptionBool) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, Boolean.toString(descriptionBool));
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
                return LiveDataReactiveStreams.fromPublisher(new RetrofitConfig()
                        .getObservableUsuarioService()
                        .getResponseProfile(id_user, null)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .onExceptionResumeNext(next -> UsuarioRepository.handleError("FALHOU")));
            }
        }.getAsLiveData();
    }

    public LiveData<ResourceState<List<Pet>>> getPetList(int id_user) {
        return new NetworkBoundSource<List<Pet>, List<Pet>>() {
            @Override
            protected void saveCallResult(@NonNull List<Pet> item) {
                appDatabase.getPetDao().inserPets(item.toArray(new Pet[item.size()]));
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Pet> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Pet>> loadFromDb() {
                return appDatabase.getPetDao().gePetList();
            }

            @NonNull
            @Override
            protected LiveData<Response<List<Pet>>> createCall() {
                return LiveDataReactiveStreams.fromPublisher(new RetrofitConfig()
                        .getObservableUsuarioService()
                        .getPetList(id_user)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .onExceptionResumeNext(next -> UsuarioRepository.handleError("FALHOU")));
            }
        }.getAsLiveData();
    }

    public LiveData<Pet> getPet(Integer id_pet) {
        return appDatabase.getPetDao().getPet(id_pet);
    }

    //upate user information
    private void insertPetRoom(Pet pet) {
        AsyncTask.execute(() -> appDatabase.getPetDao().inserPets(pet));
    }

    public void createNewPet(Pet pet) {
        try {
            RequestBody body = RequestBody
                    .create(MediaType.parse("application/json; charset=utf-8"), pet.getFieldsJson().toString());

            Observable<Pet> createPet = new RetrofitConfig()
                    .getObservableUsuarioService()
                    .createUserPet(body);

            Disposable disposable = createPet.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::insertPetRoom, this::handleInternalError);

            compositeDisposable.add(disposable);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //messages
    public Flowable<List<Comentario>> getComments(Integer id_user) {
        return new RetrofitConfig()
                .getObservableUsuarioService()
                .getComments(id_user);
    }

    //upate user information
    public void updateCurrentUser(Usuario user) {
        AsyncTask.execute(() -> appDatabase.getUsuarioDao().updateCurrentUser(user));
    }

    //update image
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

        compositeDisposable.add(disposable);
    }

    //update anything else
    public <T> void updateProfile(Integer id_user, String fieldName, T fieldValue) {
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("id_user", createPart(id_user));

        if (fieldValue instanceof Integer)
            map.put(fieldName, createPart((Integer)fieldValue));
        if (fieldValue instanceof String)
            map.put(fieldName, createPart((String)fieldValue));
        if (fieldValue instanceof Date)
            map.put(fieldName, createPart((Date)fieldValue));

        Observable<Usuario> updateProfile = new RetrofitConfig()
                .getObservableUsuarioService()
                .updateUserProfile(map, null);

        Disposable disposable = updateProfile.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCurrentUser);

        compositeDisposable.add(disposable);
    }

    private void insertHospedadorRoom(Hospedador hospedador) {
        AsyncTask.execute(() -> appDatabase.getHospedadorDao().insertHospedador(hospedador));
    }

    public Observable<Hospedador> createHospedador(Hospedador hospedador) {
        Observable<Hospedador> hospedadorObservable = new RetrofitConfig()
                .getObservableUsuarioService()
                .createHospedador(hospedador.generateRequestBody())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .share();

        compositeDisposable.add(hospedadorObservable.subscribe(this::insertHospedadorRoom));

        return hospedadorObservable;
    }

    //handle error in general
    private void handleInternalError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;

            Log.d("URLRetro", httpException.response().raw().request().url().toString());
            Log.d("CODERetro", Integer.toString(httpException.code()));

            httpException.printStackTrace();
        }
    }

    public static void handleError(String msg) {
        Log.e(UsuarioRepository.class.getSimpleName(), msg);
    }
}
