package com.lucasgomes.android.justintime.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.lucasgomes.android.justintime.model.Log;

@Entity(tableName = "main")
public class MainEntity {

    @PrimaryKey
    @NonNull
    public String hello;

    public MainEntity(String hello) {
        this.hello = hello;
    }
}