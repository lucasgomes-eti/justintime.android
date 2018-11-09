package com.lucasgomes.android.justintime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucasgomes.android.justintime.App;
import com.lucasgomes.android.justintime.R;
import com.lucasgomes.android.justintime.helper.CalendarUtils;
import com.lucasgomes.android.justintime.model.CalendarEntity;
import com.lucasgomes.android.justintime.model.Log;
import com.lucasgomes.android.justintime.model.LogEntity;
import com.lucasgomes.android.justintime.model.LogGroup;
import com.lucasgomes.android.justintime.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity
        extends AppCompatActivity
        implements YearMonthPickerDialogFragment.OnDateSelectedListener, ListItemClickListener {

    private MainViewModel viewModel;

    private final LogsAdapter logsAdapter = new LogsAdapter(
            new ArrayList<>(), this);

    private YearMonthPickerDialogFragment yearMonthPickerDialog = new YearMonthPickerDialogFragment();

    private Button monthButton;
    private RecyclerView recyclerView;

    private Calendar calendar = Calendar.getInstance();

    private static String CALENDAR_KEY = "calendar";

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar_main));
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }


        ((App) getApplication()).component.inject(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        monthButton = findViewById(R.id.btn_month);

        if (savedInstanceState == null) {
            monthButton.setText(CalendarUtils.getMonthName(this, calendar.get(Calendar.MONTH)));
        }

        recyclerView = findViewById(R.id.rv_logs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(logsAdapter);

        monthButton.setOnClickListener((v) -> {
            if (!yearMonthPickerDialog.isAdded()) {
                yearMonthPickerDialog.setTime(calendar.getTime());
                yearMonthPickerDialog.show(getSupportFragmentManager(), YearMonthPickerDialogFragment.class.getName());
            }
        });

        findViewById(R.id.fab_new_log).setOnClickListener((v -> {
            NewLogDialogFragment newLogDialogFragment = new NewLogDialogFragment();
            if (!newLogDialogFragment.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(android.R.id.content, newLogDialogFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }));

        new QueryLogs().execute();
    }

    private class QueryLogs extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            queryLogs();
            return null;
        }
    }

    private void queryLogs() {
        String userId = auth.getCurrentUser().getUid();
        String startTime = String.valueOf(String.valueOf(calendar.get(Calendar.YEAR)) +
                String.valueOf(calendar.get(Calendar.MONTH)));

        databaseReference.child(getString(R.string.log_db_node))
                .child(userId)
                .orderByChild(getString(R.string.start_time_db_node)).equalTo(startTime)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        logsAdapter.mLogs.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue() != null) {
                                Log log = new Log(snapshot.getValue(LogEntity.class));
                                log.setDatabaseReference(snapshot.getRef());
                                StringBuilder title = new StringBuilder();
                                if (log.getStartTime().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                                    title.append(getString(R.string.today));
                                } else {
                                    title.append(log.getStartTime().get(Calendar.DAY_OF_MONTH));
                                    title.append(',');
                                    title.append(getResources().getStringArray(R.array.workDaysValues)[log.getStartTime().get(Calendar.DAY_OF_WEEK)]);
                                }

                                LogGroup logGroupTitle = new LogGroup(title.toString(), null);
                                LogGroup logGroup = new LogGroup("", log);

                                Boolean containsTitle = false;
                                for (LogGroup mLog : logsAdapter.mLogs) {
                                    if (mLog.getTitle().equals(logGroupTitle.getTitle())) {
                                        containsTitle = true;
                                        break;
                                    }
                                }

                                if (containsTitle) {
                                    logsAdapter.mLogs.add(logGroup);
                                    logsAdapter.notifyDataSetChanged();
                                } else {
                                    logsAdapter.mLogs.add(logGroupTitle);
                                    logsAdapter.mLogs.add(logGroup);
                                    logsAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage(databaseError.getMessage(), true);
                    }
                });
    }

    private void showMessage(String message, Boolean dismissAction) {
        if (getCurrentFocus() != null) {
            if (dismissAction) {
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_INDEFINITE);
                snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
                snackbar.setAction(R.string.dismiss, (v) -> snackbar.dismiss()).show();
            } else {
                Snackbar.make(getCurrentFocus(), message, Snackbar.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onListItemClick(int itemIndex) {
        Log log = logsAdapter.mLogs.get(itemIndex).getLog();
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

                log.getDatabaseReference().setValue(new LogEntity(log));
                databaseReference.child(getString(R.string.log_db_node))
                        .child(auth.getCurrentUser().getUid())
                        .push()
                        .setValue(new LogEntity(new Log(calendarEndNormalWork, calendarEndTime, getString(R.string.extra_work), true)));
            } else {
                log.setEndTime(calendarEndTime);
                log.setComplete(true);

                log.getDatabaseReference().setValue(new LogEntity(log));
            }
        } else {
            log.setEndTime(calendarEndTime);
            log.setComplete(true);

            log.getDatabaseReference().setValue(new LogEntity(log));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_file_export: {
                return false;
            }
            case R.id.action_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CALENDAR_KEY, calendar);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        calendar = (Calendar) savedInstanceState.getSerializable(CALENDAR_KEY);
        if (calendar != null) {
            setMonthText(calendar);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDateSelected(Calendar calendar) {
        this.calendar = calendar;
        setMonthText(calendar);
        logsAdapter.mLogs.clear();
        logsAdapter.notifyDataSetChanged();
        queryLogs();
    }

    private void setMonthText(Calendar calendar) {
        Calendar currentDateCalendar = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == currentDateCalendar.get(Calendar.YEAR)) {
            monthButton.setText(CalendarUtils.getMonthName(this, calendar.get(Calendar.MONTH)));
        } else {
            monthButton.setText(String.format("%s, %s", CalendarUtils.getMonthName(this, calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.YEAR))));
        }
    }
}
