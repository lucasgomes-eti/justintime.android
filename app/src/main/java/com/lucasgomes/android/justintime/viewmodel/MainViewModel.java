package com.lucasgomes.android.justintime.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.lucasgomes.android.justintime.App;
import com.lucasgomes.android.justintime.R;
import com.lucasgomes.android.justintime.repository.MainRepository;

import javax.inject.Inject;

public class MainViewModel extends AndroidViewModel {

    @Inject
    public MainRepository mainRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        ((App) application).component.inject(this);
        _hello.postValue(getApplication().getString(R.string.app_name));
    }

    private MutableLiveData<String> _hello = new MutableLiveData<>();
    public LiveData<String> getHello() {
        return _hello;
    }
}
