package com.lucasgomes.android.justintime.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.lucasgomes.android.justintime.db.Database;
import com.lucasgomes.android.justintime.db.dao.MainDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private Context mContext;

    public DatabaseModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Database provideDatabase() {
        return Room.databaseBuilder(mContext, Database.class, "justintime.db").build();
    }

    @Provides
    @Singleton
    MainDao providesMainDao(Database database) {
        return database.mainDao();
    }
}