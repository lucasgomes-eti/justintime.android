package com.lucasgomes.android.justintime.model;

import com.lucasgomes.android.justintime.db.model.MainEntity;

public class Main {
    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public Main(String hello) {
        this.hello = hello;
    }

    public MainEntity map() {
        return new MainEntity(hello);
    }
}
