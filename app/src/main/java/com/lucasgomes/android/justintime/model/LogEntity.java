package com.lucasgomes.android.justintime.model;

import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

public class LogEntity {
    private String startTime;
    @Nullable private String endTime;
    private String logType;
    private Boolean isComplete;

    public LogEntity(Log log) {
        this.startTime = new Gson().toJson(log.getStartTime());
        this.endTime = new Gson().toJson(log.getEndTime());
        this.logType = log.getLogType();
        this.isComplete = log.getComplete();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable String endTime) {
        this.endTime = endTime;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }
}
