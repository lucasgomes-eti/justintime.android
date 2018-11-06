package com.lucasgomes.android.justintime.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucasgomes.android.justintime.App;
import com.lucasgomes.android.justintime.R;
import com.lucasgomes.android.justintime.helper.CalendarUtils;
import com.lucasgomes.android.justintime.model.Log;
import com.lucasgomes.android.justintime.model.LogGroup;
import com.lucasgomes.android.justintime.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity
        extends AppCompatActivity
        implements YearMonthPickerDialogFragment.OnDateSelectedListener, ListItemClickListener {

    private MainViewModel viewModel;

    private final LogsAdapter logsAdapter = new LogsAdapter(
            new ArrayList<LogGroup>(Arrays.asList(
                    new LogGroup("Today", null),
                    new LogGroup("", new Log(
                            Calendar.getInstance(), null, "Normal Work", false
                    )),
                    new LogGroup("", new Log(
                            Calendar.getInstance(), null, "Normal Work", false
                    ))
            )), this);

    private YearMonthPickerDialogFragment yearMonthPickerDialog = new YearMonthPickerDialogFragment();

    private Button monthButton;
    private RecyclerView recyclerView;

    private Calendar calendar = Calendar.getInstance();

    private static String CALENDAR_KEY = "calendar";

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar_main));
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        auth = FirebaseAuth.getInstance();

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
    }

    @Override
    public void onListItemClick(int itemIndex) {

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
