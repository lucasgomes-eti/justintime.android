package com.lucasgomes.android.justintime.di;

import android.content.Context;

import com.lucasgomes.android.justintime.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private App mApp;

    public AppModule(App app){
        mApp = app;
    }

    @Provides
    @Singleton
    App providesApp() {
        return mApp;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApp.getApplicationContext();
    }
}
