package com.lucasgomes.android.justintime.remote.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.lucasgomes.android.justintime.AppExecutors;
import com.lucasgomes.android.justintime.model.api.Resource;

public abstract class NetworkBoundResource<ReturnType, DBType> {

    private final AppExecutors appExecutors;
    private final MediatorLiveData result = new MediatorLiveData<Resource<ReturnType>>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        initialize();
    }

    @MainThread
    private void initialize() {

    }

    @MainThread
    private void setValue(Resource<ReturnType> newValue) {
        if (result.getValue() == null || result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(LiveData<DBType> dbSource){
        LiveData<ApiResponse<ReturnType>> apiResponse = createCall();
        if (dbSource == null) {
            throw new IllegalArgumentException("DbSource is null");
        }
        if (apiResponse == null) {
            throw new IllegalArgumentException("ApiResponse is null");
        }
        result.addSource(dbSource, responseObj -> {
            DBType newData = (DBType) responseObj;
            setValue(Resource.loading(map(newData)));
        });
        result.addSource(apiResponse, responseObj -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            ApiResponse response = (ApiResponse) responseObj;
            if (response != null) {
                if (response.isSuccessful != null && response.isSuccessful) {
                    appExecutors.diskIO.execute(() -> {
                        ReturnType processedItem = (ReturnType) processResponse(response);
                        saveCallResult(processedItem);
                        appExecutors.mainThread.execute(() ->
                                result.addSource(loadFromDb(), newResponseObj -> {
                                    DBType newData = (DBType) newResponseObj;
                                    setValue(Resource.success(map(newData)));
                                }));
                    });
                } else {
                    onFetchFailed(response.error);
                    result.addSource(dbSource, newResponseObj -> {
                        DBType newData = (DBType) newResponseObj;
                        setValue(Resource.error(response.errorMessage, map(newData)));
                    });
                    setValue(Resource.error(response.errorMessage, null));
                }
            }
        });
    }

    public LiveData<Resource<ReturnType>> asLiveData() {
        return result;
    }

    @MainThread
    public abstract LiveData<DBType> loadFromDb();

    @MainThread
    public abstract Boolean shouldFetch(DBType data);

    @MainThread
    public abstract LiveData<ApiResponse<ReturnType>> createCall();

    @WorkerThread
    private ReturnType processResponse(ApiResponse<ReturnType> response) {
        return response.body;
    }

    public abstract ReturnType map(DBType data);

    @MainThread
    public void onFetchFailed(Throwable throwable) {}

    @WorkerThread
    public abstract void saveCallResult(ReturnType item);

}