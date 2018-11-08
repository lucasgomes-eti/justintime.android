package com.lucasgomes.android.justintime.model;

import org.jetbrains.annotations.Nullable;

public class LogEntity {
    private CalendarEntity startTime;
    @Nullable private CalendarEntity endTime;
    private String logType;
    private Boolean isComplete;

    public LogEntity(Log log) {
        this.startTime = new CalendarEntity(log.getStartTime());
        if (log.getEndTime() != null)
            this.endTime = new CalendarEntity(log.getEndTime());
        this.logType = log.getLogType();
        this.isComplete = log.getComplete();
    }

    public CalendarEntity getStartTime() {
        return startTime;
    }

    public void setStartTime(CalendarEntity startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public CalendarEntity getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable CalendarEntity endTime) {
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
