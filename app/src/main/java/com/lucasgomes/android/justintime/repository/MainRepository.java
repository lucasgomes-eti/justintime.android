package com.lucasgomes.android.justintime.repository;

import android.arch.lifecycle.LiveData;

import com.lucasgomes.android.justintime.AppExecutors;
import com.lucasgomes.android.justintime.db.dao.MainDao;
import com.lucasgomes.android.justintime.model.Main;
import com.lucasgomes.android.justintime.model.api.Resource;
import com.lucasgomes.android.justintime.remote.MainService;
import com.lucasgomes.android.justintime.remote.util.ApiResponse;
import com.lucasgomes.android.justintime.remote.util.NetworkResource;

import javax.inject.Inject;

public class MainRepository {

    private final MainService mainService;
    private final MainDao mainDao;
    private final AppExecutors appExecutors;

    @Inject
    public MainRepository(MainService mainService, MainDao mainDao, AppExecutors appExecutors) {
        this.mainService = mainService;
        this.mainDao = mainDao;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<Main>> loadMain() {
        return new NetworkResource<Main>(appExecutors) {
            @Override
            public LiveData<ApiResponse<Main>> createCall() {
                return mainService.getMain();
            }
        }.asLiveData();
    }
}
