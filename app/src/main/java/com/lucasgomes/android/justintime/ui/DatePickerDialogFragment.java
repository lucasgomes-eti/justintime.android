package com.lucasgomes.android.justintime.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.lucasgomes.android.justintime.R;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment {

    interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int dayOfMonth);
    }

    OnDateSelectedListener listener;

    int year, month, dayOfMonth;

    Calendar calendar = Calendar.getInstance();

    CalendarView cvDate;

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_date_picker, null);
            builder.setView(contentView);

            cvDate = contentView.findViewById(R.id.cv_date);
            Button btnSelect = contentView.findViewById(R.id.btn_select);
            Button btnCancel = contentView.findViewById(R.id.btn_cancel);

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            cvDate.setDate(calendar.getTime().getTime());

            cvDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                this.year = year;
                this.month = month;
                this.dayOfMonth = dayOfMonth;
            });

            btnSelect.setOnClickListener((v) -> {
                listener.onDateSelected(year, month, dayOfMonth);
                dismiss();
            });

            btnCancel.setOnClickListener((v) -> {
                dismiss();
            });

            return builder.create();
        } else {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    void setDate(Calendar calendar) {
        this.calendar = calendar;
        cvDate.setDate(calendar.getTime().getTime());
    }

    void setListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }
}
