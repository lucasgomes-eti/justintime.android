package com.lucasgomes.android.justintime.di;

import com.lucasgomes.android.justintime.App;
import com.lucasgomes.android.justintime.ui.MainActivity;
import com.lucasgomes.android.justintime.viewmodel.MainViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DatabaseModule.class, RemoteModule.class})

public interface AppComponent {

    void inject(App app);

    void inject(MainActivity activity);

    void inject(MainViewModel mainViewModel);
}
