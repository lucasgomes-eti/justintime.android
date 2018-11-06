package com.lucasgomes.android.justintime.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
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

import com.lucasgomes.android.justintime.R;

import java.util.Calendar;

public class NewLogDialogFragment extends DialogFragment implements TimePickerDialogFragment.OnTimeSelectedListener {

    TextInputEditText tietStartTime;

    @Nullable
    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_log, null);

            Toolbar toolbar = contentView.findViewById(R.id.toolbar_new_log);
            tietStartTime = contentView.findViewById(R.id.tiet_start_time);

            tietStartTime.setRawInputType(InputType.TYPE_NULL);
            tietStartTime.setTextIsSelectable(true);

            TimePickerDialogFragment tpdfStartTime = new TimePickerDialogFragment();
            tpdfStartTime.setListener(this);

            Calendar calendar = Calendar.getInstance();
            setTietStartTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));

            tietStartTime.setOnFocusChangeListener((view, visible) -> {
                if (!tpdfStartTime.isAdded() && visible) {
                    tpdfStartTime.show(getActivity().getSupportFragmentManager(), TimePickerDialogFragment.class.getName());
                }
            });

            tietStartTime.setOnClickListener((view) -> {
                if (!tpdfStartTime.isAdded()) {
                    tpdfStartTime.show(getActivity().getSupportFragmentManager(), TimePickerDialogFragment.class.getName());
                }
            });

            toolbar.setTitle(R.string.new_log);

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_clear);
            }

            setHasOptionsMenu(true);

            return contentView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        setTietStartTime(hour, minute);
    }

    private void setTietStartTime(int hour, int minute) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hour);
        stringBuilder.append(':');
        stringBuilder.append(minute);
        stringBuilder.append(' ');

        if (hour < 12) {
            stringBuilder.append("AM");
        } else {
            stringBuilder.append("PM");
        }

        tietStartTime.setText(stringBuilder);
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
