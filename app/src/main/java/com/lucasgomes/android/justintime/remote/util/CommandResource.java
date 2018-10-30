package com.lucasgomes.android.justintime.remote.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.lucasgomes.android.justintime.AppExecutors;
import com.lucasgomes.android.justintime.model.api.Resource;

public abstract class CommandResource<T> {

    private final AppExecutors appExecutors;
    private final MediatorLiveData result = new MediatorLiveData<Resource<T>>();

    @MainThread
    protected CommandResource(AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
        executeCommand();
    }

    private void executeCommand(){
        LiveData<ApiResponse<T>> apiResponse = createCommandCall();
        result.addSource(apiResponse, responseObj -> {
            result.removeSource(apiResponse);
            ApiResponse response = (ApiResponse) responseObj;
            if (response != null) {
                if (response.isSuccessful != null && response.isSuccessful) {
                    appExecutors.mainThread.execute(() ->
                            setValue(Resource.success((T) processResponse(response))));
                } else {
                    onCommandFailed(response.error);
                    setValue(Resource.error(response.errorMessage, null));
                }
            }
        });
    }

    @MainThread
    public abstract LiveData<ApiResponse<T>> createCommandCall();

    @WorkerThread
    public abstract void saveResult(T item);

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
    public void onCommandFailed(Throwable throwable) {}

    public LiveData<Resource<T>> asLiveData() {
        return result;
    }
}
