package com.lucasgomes.android.justintime.model;

import android.support.annotation.Nullable;

public class LogEntity {
    private CalendarEntity startTime;
    @Nullable
    private CalendarEntity endTime;
    private String logType;
    private Boolean isComplete;

    private String startTime_year_month;

    public LogEntity() {
    }

    public LogEntity(Log log) {
        this.startTime = new CalendarEntity(log.getStartTime());
        this.startTime_year_month = String.valueOf(
                String.valueOf(startTime.getYear()) +
                        String.valueOf(startTime.getMonth()));
        if (log.getEndTime() != null) {
            this.endTime = new CalendarEntity(log.getEndTime());
        }
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

    public String getStartTime_year_month() {
        return startTime_year_month;
    }

    public void setStartTime_year_month(String startTime_year_month) {
        this.startTime_year_month = startTime_year_month;
    }
}
