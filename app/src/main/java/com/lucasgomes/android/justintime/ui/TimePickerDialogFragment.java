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
import android.widget.Button;
import android.widget.TimePicker;

import com.lucasgomes.android.justintime.R;

public class TimePickerDialogFragment extends DialogFragment {

    interface OnTimeSelectedListener {
        void onTimeSelected(int hour, int minute);
    }

    OnTimeSelectedListener listener;

    @NonNull
    @Override
    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_time_picker, null);
            builder.setView(contentView);

            TimePicker tpStartTime = contentView.findViewById(R.id.tp_start_time);
            Button btnSelect = contentView.findViewById(R.id.btn_select);
            Button btnCancel = contentView.findViewById(R.id.btn_cancel);

            btnSelect.setOnClickListener((v) -> {
                listener.onTimeSelected(tpStartTime.getCurrentHour(), tpStartTime.getCurrentMinute());
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

    void setListener(OnTimeSelectedListener listener) {
        this.listener = listener;
    }
}
