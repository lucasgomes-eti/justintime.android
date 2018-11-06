package com.lucasgomes.android.justintime.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;

import com.lucasgomes.android.justintime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewLogDialogFragment
        extends DialogFragment
        implements TimePickerDialogFragment.OnTimeSelectedListener,
        DatePickerDialogFragment.OnDateSelectedListener {

    TextInputEditText tietStartTime;
    CheckBox cbFromPast;
    TextInputEditText tietDay;
    TextInputEditText tiedEndTime;

    Date dateStartTime = new Date();
    Calendar calendarDay = Calendar.getInstance();
    Date dateEndTime = new Date();

    private static final String START_TIME_FRAGMENT_TAG = "start_time_tag";
    private static final String END_TIME_FRAGMENT_TAG = "end_time_tag";

    private static final String START_TIME_DATE_KEY = "start_time_key";
    private static final String CALENDAR_DAY_KEY = "calendar_day_key";
    private static final String END_TIME_DATE_KEY = "end_time_key";

    @Nullable
    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_log, null);

            Toolbar toolbar = contentView.findViewById(R.id.toolbar_new_log);
            toolbar.setTitle(R.string.new_log);

            tietStartTime = contentView.findViewById(R.id.tiet_start_time);
            cbFromPast = contentView.findViewById(R.id.cb_from_past);
            tietDay = contentView.findViewById(R.id.tiet_day);
            tiedEndTime = contentView.findViewById(R.id.tiet_end_time);

            tietStartTime.setRawInputType(InputType.TYPE_NULL);
            tietStartTime.setTextIsSelectable(true);

            tietDay.setRawInputType(InputType.TYPE_NULL);
            tietDay.setTextIsSelectable(true);

            tiedEndTime.setRawInputType(InputType.TYPE_NULL);
            tiedEndTime.setTextIsSelectable(true);

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_clear);
            }


            TimePickerDialogFragment tpdfStartTime = new TimePickerDialogFragment();
            tpdfStartTime.setListener(this);

            Date date = new Date();
            tietStartTime.setText(buildTimeText(date.getHours(), date.getMinutes()));

            tietStartTime.setOnClickListener((view) -> {
                if (!tpdfStartTime.isAdded()) {
                    tpdfStartTime.show(getActivity().getSupportFragmentManager(), START_TIME_FRAGMENT_TAG);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        tpdfStartTime.setTimePicker(dateStartTime);
                    }, 100);
                }
            });

            cbFromPast.setOnCheckedChangeListener((view, checked) -> {
                tietDay.setEnabled(checked);
                tiedEndTime.setEnabled(checked);
            });

            DatePickerDialogFragment dpdfDay = new DatePickerDialogFragment();
            dpdfDay.setListener(this);

            tietDay.setOnClickListener((visible) -> {
                if (!dpdfDay.isAdded()) {
                    dpdfDay.show(getActivity().getSupportFragmentManager(), DatePickerDialogFragment.class.getName());
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        dpdfDay.setDate(calendarDay);
                    }, 100);
                }
            });

            TimePickerDialogFragment tpdfEndTime = new TimePickerDialogFragment();
            tpdfEndTime.setListener(this);

            tiedEndTime.setOnClickListener((view) -> {
                if (!tpdfEndTime.isAdded()) {
                    tpdfEndTime.show(getActivity().getSupportFragmentManager(), END_TIME_FRAGMENT_TAG);
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        tpdfEndTime.setTimePicker(dateEndTime);
                    }, 100);
                }
            });

            setHasOptionsMenu(true);
            return contentView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(START_TIME_DATE_KEY, dateStartTime);
        outState.putSerializable(CALENDAR_DAY_KEY, calendarDay);
        outState.putSerializable(END_TIME_DATE_KEY, dateEndTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            dateStartTime = (Date) savedInstanceState.getSerializable(START_TIME_DATE_KEY);
            calendarDay = (Calendar) savedInstanceState.getSerializable(CALENDAR_DAY_KEY);
            dateEndTime = (Date) savedInstanceState.getSerializable(END_TIME_DATE_KEY);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onTimeSelected(String tag, int hour, int minute) {
        if (tag.equals(START_TIME_FRAGMENT_TAG)) {
            tietStartTime.setText(buildTimeText(hour, minute));
            dateStartTime.setHours(hour);
            dateStartTime.setMinutes(minute);
        } else if (tag.equals(END_TIME_FRAGMENT_TAG)) {
            tiedEndTime.setText(buildTimeText(hour, minute));
            dateEndTime.setHours(hour);
            dateEndTime.setMinutes(minute);
        }
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        tietDay.setText(buildDateText(year, month, dayOfMonth));
        calendarDay.set(Calendar.YEAR, year);
        calendarDay.set(Calendar.MONTH, month);
        calendarDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private String buildDateText(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return new SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault()).format(calendar.getTime());
    }

    private String buildTimeText(int hour, int minute) {
        return String.valueOf(hour < 10 ? "0" + hour : hour) +
                ':' +
                (minute < 10 ? "0" + minute : minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() != null) {
            menu.clear();
            getActivity().getMenuInflater().inflate(R.menu.menu_new_log, menu);
        } else {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                dismiss();
                return true;
            }
            case android.R.id.home: {
                dismiss();
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
