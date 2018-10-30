package com.lucasgomes.android.justintime.remote;

import android.arch.lifecycle.LiveData;

import com.lucasgomes.android.justintime.model.Main;
import com.lucasgomes.android.justintime.remote.util.ApiResponse;

import retrofit2.http.GET;

public interface MainService {

    @GET("main")
    LiveData<ApiResponse<Main>> getMain();
}