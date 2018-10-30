package com.lucasgomes.android.justintime.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.lucasgomes.android.justintime.db.model.MainEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void createMain(MainEntity mainEntity);

    @Query("SELECT * FROM main")
    LiveData<List<MainEntity>> readMain();

    @Update(onConflict = REPLACE)
    void updateMain(MainEntity mainEntity);

    @Delete
    void deleteMain(MainEntity mainEntity);
}
