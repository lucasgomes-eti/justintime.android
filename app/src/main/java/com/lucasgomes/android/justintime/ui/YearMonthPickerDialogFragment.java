package com.lucasgomes.android.justintime.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.lucasgomes.android.justintime.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class YearMonthPickerDialogFragment extends DialogFragment {

    public interface OnDateSelectedListener {
        void onDateSelected(Calendar calendar);
    }

    OnDateSelectedListener listener;

    Calendar calendar = Calendar.getInstance();

    public void setTime(Date date) {
        calendar.setTime(date);
    }

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_year_month_picker, null);
            builder.setView(contentView);

            Spinner yearSpinner = contentView.findViewById(R.id.spinner_year);
            Spinner monthSpinner = contentView.findViewById(R.id.spinner_month);

            List<String> years = new ArrayList<>();

            Calendar currentCalendar = Calendar.getInstance();

            for (int i = 0; i < 101; i++) {
                years.add(String.valueOf(currentCalendar.get(Calendar.YEAR) - i));
            }

            ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);

            yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            yearSpinner.setAdapter(yearsAdapter);

            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calendar.set(Calendar.YEAR, Integer.valueOf(years.get(position)));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calendar.set(Calendar.MONTH, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            yearSpinner.setSelection(years.indexOf(String.valueOf(calendar.get(Calendar.YEAR))));
            monthSpinner.setSelection(calendar.get(Calendar.MONTH));

            contentView.findViewById(R.id.btn_select).setOnClickListener((v) -> {
                listener.onDateSelected(calendar);
                dismiss();
            });

            contentView.findViewById(R.id.btn_cancel).setOnClickListener((v) -> {
                dismiss();
            });

            return builder.create();
        } else {
            return super.onCreateDialog(savedInstanceState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnDateSelectedListener) context;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }
}
