package com.lucasgomes.android.justintime.db.util;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;

import com.lucasgomes.android.justintime.model.api.Resource;

public abstract class DatabaseResource<T> {

    private final MediatorLiveData result = new MediatorLiveData<Resource<T>>();

    @MainThread
    protected DatabaseResource() {
        result.setValue(Resource.loading(null));

        LiveData<T> dbSource = loadFromDb();
        result.addSource(dbSource, responseObj -> {
            setValue(Resource.success((T) responseObj));
        });

    }

    @MainThread
    private void setValue(Resource<T> newValue) {
        if (result.getValue() == null || result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    public LiveData<Resource<T>> asLiveData() {
        return result;
    }

    @MainThread
    protected abstract LiveData<T> loadFromDb();
}