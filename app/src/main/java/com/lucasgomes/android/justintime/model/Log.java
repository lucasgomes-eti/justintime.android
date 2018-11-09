package com.lucasgomes.android.justintime.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

public class Log implements Parcelable {
    private Calendar startTime;
    @Nullable private Calendar endTime;
    private String logType;
    private Boolean isComplete;

    public Log(Calendar startTime, @Nullable Calendar endTime, String logType, Boolean isComplete) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.logType = logType;
        this.isComplete = isComplete;
    }

    public Log(LogEntity logEntity) {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR, logEntity.getStartTime().getYear());
        startTime.set(Calendar.MONTH, logEntity.getStartTime().getMonth());
        startTime.set(Calendar.DAY_OF_MONTH, logEntity.getStartTime().getDayOfMonth());
        startTime.set(Calendar.HOUR_OF_DAY, logEntity.getStartTime().getHourOfDay());
        startTime.set(Calendar.MINUTE, logEntity.getStartTime().getMinute());
        startTime.set(Calendar.SECOND, logEntity.getStartTime().getSecond());

        this.startTime = startTime;

        if (logEntity.getEndTime() != null) {
            Calendar endTime = Calendar.getInstance();
            endTime.set(Calendar.YEAR, logEntity.getEndTime().getYear());
            endTime.set(Calendar.MONTH, logEntity.getEndTime().getMonth());
            endTime.set(Calendar.DAY_OF_MONTH, logEntity.getEndTime().getDayOfMonth());
            endTime.set(Calendar.HOUR_OF_DAY, logEntity.getEndTime().getHourOfDay());
            endTime.set(Calendar.MINUTE, logEntity.getEndTime().getMinute());
            endTime.set(Calendar.SECOND, logEntity.getEndTime().getSecond());

            this.endTime = endTime;
        }

        this.logType = logEntity.getLogType();
        this.isComplete = logEntity.getComplete();
    }

    protected Log(Parcel in) {
        startTime = (Calendar) in.readSerializable();
        endTime = (Calendar) in.readSerializable();
        logType = in.readString();
        byte tmpIsComplete = in.readByte();
        isComplete = tmpIsComplete == 0 ? null : tmpIsComplete == 1;
    }

    public static final Creator<Log> CREATOR = new Creator<Log>() {
        @Override
        public Log createFromParcel(Parcel in) {
            return new Log(in);
        }

        @Override
        public Log[] newArray(int size) {
            return new Log[size];
        }
    };

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable Calendar endTime) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(startTime);
        dest.writeSerializable(endTime);
        dest.writeString(logType);
        dest.writeByte((byte) (isComplete == null ? 0 : isComplete ? 1 : 2));
    }
}
