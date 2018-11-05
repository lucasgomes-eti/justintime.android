package com.lucasgomes.android.justintime.helper;

import android.content.Context;

import com.lucasgomes.android.justintime.R;

public class CalendarUtils {

    public static String getMonthName(Context context, int monthId) {
        return context.getResources().getStringArray(R.array.months)[monthId];
    }
}
