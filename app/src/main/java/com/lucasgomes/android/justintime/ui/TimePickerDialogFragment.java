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
import android.widget.TimePicker;

import com.lucasgomes.android.justintime.R;

import java.util.Date;

public class TimePickerDialogFragment extends DialogFragment {

    interface OnTimeSelectedListener {
        void onTimeSelected(String tag, int hour, int minute);
    }

    OnTimeSelectedListener listener;

    TimePicker timePicker;

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
            builder.setView(contentView);

            timePicker = contentView.findViewById(R.id.time_picker);
            Button btnSelect = contentView.findViewById(R.id.btn_select);
            Button btnCancel = contentView.findViewById(R.id.btn_cancel);

            timePicker.setIs24HourView(true);

            btnSelect.setOnClickListener((v) -> {
                listener.onTimeSelected(getTag(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
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

    void setTimePicker(Date date) {
        timePicker.setCurrentHour(date.getHours());
        timePicker.setCurrentMinute(date.getMinutes());
        timePicker.setIs24HourView(true);
    }

    void setListener(OnTimeSelectedListener listener) {
        this.listener = listener;
    }
}
