package com.lucasgomes.android.justintime.helper;

import com.lucasgomes.android.justintime.model.Log;

import java.util.Calendar;
import java.util.Comparator;

public class SortLogByDate implements Comparator<Log> {

    @Override
    public int compare(Log log1, Log log2) {
        return log1.getStartTime().get(Calendar.DAY_OF_MONTH) - log2.getStartTime().get(Calendar.DAY_OF_MONTH);
    }
}
