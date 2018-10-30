package com.lucasgomes.android.justintime.db;

import android.arch.persistence.room.RoomDatabase;

import com.lucasgomes.android.justintime.db.dao.MainDao;
import com.lucasgomes.android.justintime.db.model.MainEntity;

@android.arch.persistence.room.Database(entities = {MainEntity.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract MainDao mainDao();
}
