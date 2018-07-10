package com.example.bruno.myapplication.commons;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import retrofit2.Response;

public abstract class NetworkBoundSource<ResultType, RequestType> {
    private final MediatorLiveData<ResourceState<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundSource() {
        result.setValue(ResourceState.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource,
                        newData -> result.setValue(ResourceState.success(newData)));
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<Response<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(dbSource,
                newData -> result.setValue(ResourceState.loading(newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                saveResultAndReInit(response.body());
            } else {
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> result.setValue(
                                ResourceState.error(response.errorBody().toString(), newData)));
            }
        });
    }

    @MainThread
    private void saveResultAndReInit(RequestType response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(),
                        newData -> result.setValue(ResourceState.success(newData)));
            }
        }.execute();
    }

    public final LiveData<ResourceState<ResultType>> getAsLiveData() {
        return result;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    @NonNull @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @NonNull @MainThread
    protected abstract LiveData<Response<RequestType>> createCall();

    @MainThread
    protected void onFetchFailed() {
    }

}