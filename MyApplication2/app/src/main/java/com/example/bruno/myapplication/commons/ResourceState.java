package com.example.bruno.myapplication.commons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import static com.example.bruno.myapplication.commons.Status.ERROR;
import static com.example.bruno.myapplication.commons.Status.LOADING;
import static com.example.bruno.myapplication.commons.Status.SUCCESS;

public class ResourceState<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private ResourceState(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ResourceState<T> success(@NonNull T data) {
        return new ResourceState<>(SUCCESS, data, null);
    }

    public static <T> ResourceState<T> error(String msg, @Nullable T data) {
        return new ResourceState<>(ERROR, data, msg);
    }

    public static <T> ResourceState<T> loading(@Nullable T data) {
        return new ResourceState<>(LOADING, data, null);
    }
}