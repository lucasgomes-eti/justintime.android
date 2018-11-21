package com.lucasgomes.android.justintime.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucasgomes.android.justintime.R;
import com.lucasgomes.android.justintime.model.Log;
import com.lucasgomes.android.justintime.model.LogEntity;
import com.lucasgomes.android.justintime.model.LogGroup;

import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class LogAppWidget extends AppWidgetProvider {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;

    public static String LOG_KEY = "log";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.log_app_widget);

        if (databaseReference != null && auth != null && auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();

            databaseReference.child(context.getString(R.string.log_db_node))
                    .child(userId)
                    .limitToLast(1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getValue() != null) {
                                    Log log = new Log(snapshot.getValue(LogEntity.class));
                                    log.setDatabaseReference(snapshot.getRef());
                                    StringBuilder title = new StringBuilder();
                                    if (log.getStartTime().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                                        title.append(context.getString(R.string.today));
                                    } else {
                                        title.append(log.getStartTime().get(Calendar.DAY_OF_MONTH));
                                        title.append(", ");
                                        title.append(context.getResources().getStringArray(R.array.workDaysArray)[log.getStartTime().get(Calendar.DAY_OF_WEEK)]);
                                    }

                                    views.setTextViewText(R.id.tv_title, title);
                                    views.setTextViewText(R.id.tv_time, String.valueOf(
                                            String.valueOf(
                                                    (log.getStartTime().get(Calendar.HOUR) < 10 ?
                                                            "0" + log.getStartTime().get(Calendar.HOUR):
                                                            log.getStartTime().get(Calendar.HOUR)) + ":" +
                                                            (log.getStartTime().get(Calendar.MINUTE) < 10 ?
                                                                    "0" + log.getStartTime().get(Calendar.MINUTE):
                                                                    log.getStartTime().get(Calendar.MINUTE)) + " " +
                                                            (log.getStartTime().get(Calendar.AM_PM) == 0 ? "AM" : "PM") + " - " +

                                                            (log.getEndTime() == null ? context.getString(R.string.tap_to_complete) : (
                                                                    (log.getEndTime().get(Calendar.HOUR) < 10 ?
                                                                            "0" + log.getEndTime().get(Calendar.HOUR):
                                                                            log.getEndTime().get(Calendar.HOUR)) + ":" +
                                                                            (log.getEndTime().get(Calendar.MINUTE) < 10 ?
                                                                                    "0" + log.getEndTime().get(Calendar.MINUTE):
                                                                                    log.getEndTime().get(Calendar.MINUTE)) + " " +
                                                                            (log.getEndTime().get(Calendar.AM_PM) == 0 ? "AM" : "PM")
                                                            ))
                                            )
                                    ));
                                    views.setTextViewText(R.id.tv_type_log, log.getLogType());

                                    if (log.getEndTime() == null) {
                                        Intent intent = new Intent(context, WidgetService.class);
                                        intent.putExtra(LOG_KEY, log);
                                        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

                                        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
                                    }

                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.putExtra(MainActivity.OPEN_NEW_LOG_KEY, true);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                                    views.setOnClickPendingIntent(R.id.btn_new_log, pendingIntent);

                                    // Instruct the widget manager to update the widget
                                    appWidgetManager.updateAppWidget(appWidgetId, views);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            android.util.Log.i("DatabaseError", databaseError.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

