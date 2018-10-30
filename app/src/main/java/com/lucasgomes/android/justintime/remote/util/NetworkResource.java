package com.lucasgomes.android.justintime.remote.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.lucasgomes.android.justintime.AppExecutors;
import com.lucasgomes.android.justintime.model.api.Resource;

public abstract class NetworkResource<T> {

    private final AppExecutors appExecutors;
    private final MediatorLiveData result = new MediatorLiveData<Resource<T>>();

    @MainThread
    protected NetworkResource(AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
        fetchFromNetwork();
    }

    private void fetchFromNetwork(){
        LiveData<ApiResponse<T>> apiResponse = createCall();
        result.addSource(apiResponse, responseObj -> {
            result.removeSource(apiResponse);
            ApiResponse response = (ApiResponse) responseObj;
            if (response != null) {
                if (response.isSuccessful != null && response.isSuccessful) {
                    appExecutors.mainThread.execute(() ->
                            setValue(Resource.success((T) processResponse(response))));
                } else {
                    onFetchFailed(response.error);
                    setValue(Resource.error(response.errorMessage, null));
                }
            }
        });
    }

    @MainThread
    public abstract LiveData<ApiResponse<T>> createCall();

    @WorkerThread
    private T processResponse(ApiResponse<T> response) {
        return response.body;
    }

    @MainThread
    private void setValue(Resource<T> newValue) {
        if (result.getValue() == null || result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    @MainThread
    public void onFetchFailed(Throwable throwable) {}

    public LiveData<Resource<T>> asLiveData() {
        return result;
    }
}