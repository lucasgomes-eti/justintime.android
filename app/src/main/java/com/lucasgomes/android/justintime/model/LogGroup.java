package com.lucasgomes.android.justintime.model;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class LogGroup {
    private String title;
    @Nullable private Log log;

    public LogGroup(String title, @Nullable Log log) {
        this.title = title;
        this.log = log;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public Log getLog() {
        return log;
    }

    public void setLog(@Nullable Log log) {
        this.log = log;
    }
}
