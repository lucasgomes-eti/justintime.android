package com.lucasgomes.android.justintime;

import android.app.Application;

import com.lucasgomes.android.justintime.di.AppComponent;
import com.lucasgomes.android.justintime.di.DaggerAppComponent;
import com.lucasgomes.android.justintime.di.DatabaseModule;
import com.lucasgomes.android.justintime.di.RemoteModule;

public class App extends Application {
    public AppComponent component =
            DaggerAppComponent.builder()
                    .databaseModule(new DatabaseModule(this))
                    .remoteModule(new RemoteModule())
                    .build();

    @Override
    public void onCreate() {
        super.onCreate();
        component.inject(this);
    }
}
