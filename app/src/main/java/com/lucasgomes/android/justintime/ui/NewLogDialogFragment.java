package com.lucasgomes.android.justintime.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.lucasgomes.android.justintime.R;

public class NewLogDialogFragment extends DialogFragment {

    @Nullable
    @Override
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getActivity() != null) {
            View contentView = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_log, null);

            Toolbar toolbar = contentView.findViewById(R.id.toolbar_new_log);
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
