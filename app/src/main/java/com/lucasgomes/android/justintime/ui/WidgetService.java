package com.lucasgomes.android.justintime.ui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucasgomes.android.justintime.R;
import com.lucasgomes.android.justintime.model.Log;
import com.lucasgomes.android.justintime.model.LogEntity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static com.lucasgomes.android.justintime.ui.LogAppWidget.LOG_KEY;

public class WidgetService extends Service {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() != null) {
            Log log = intent.getParcelableExtra(LOG_KEY);

            if (log != null && log.getEndTime() == null) {
                Calendar calendarEndTime = Calendar.getInstance();

                Set<String> workDays = getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE)
                        .getStringSet(getString(R.string.work_days_key),
                                new HashSet<>(Arrays.asList(getResources().getStringArray(R.array.workDaysValuesDefault))));

                int workload = Integer.valueOf(getSharedPreferences(getPackageName() + "_preferences", Context.MODE_PRIVATE)
                        .getString(getString(R.string.workload_key), "8"));

                int hoursWorked = calendarEndTime.get(Calendar.HOUR) - log.getStartTime().get(Calendar.HOUR);

                if (workDays.contains(String.valueOf(log.getStartTime().get(Calendar.DAY_OF_WEEK)))) {
                    if (hoursWorked > workload) {
                        int extraHoursWorked = hoursWorked - workload;
                        Calendar calendarEndNormalWork = calendarEndTime;
                        calendarEndNormalWork.set(Calendar.HOUR, calendarEndTime.get(Calendar.HOUR) - extraHoursWorked);

                        log.setEndTime(calendarEndNormalWork);
                        log.setComplete(true);

                        if (log.getDatabaseReference() != null) {
                            log.getDatabaseReference().setValue(new LogEntity(log));
                        }
                        databaseReference.child(getString(R.string.log_db_node))
                                .child(auth.getCurrentUser().getUid())
                                .push()
                                .setValue(new LogEntity(new Log(calendarEndNormalWork, calendarEndTime, getString(R.string.extra_work), true)));

                    } else {
                        log.setEndTime(calendarEndTime);
                        log.setComplete(true);

                        if (log.getDatabaseReference() != null) {
                            log.getDatabaseReference().setValue(new LogEntity(log));
                        }
                    }
                } else {
                    log.setEndTime(calendarEndTime);
                    log.setComplete(true);

                    if (log.getDatabaseReference() != null) {
                        log.getDatabaseReference().setValue(new LogEntity(log));
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public WidgetService() { }

    private class Binder extends android.os.Binder {
        WidgetService getWidgetService() {
            return WidgetService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
