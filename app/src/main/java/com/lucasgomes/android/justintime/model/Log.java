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
