package com.lucasgomes.android.justintime.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucasgomes.android.justintime.R;
import com.lucasgomes.android.justintime.model.Log;
import com.lucasgomes.android.justintime.model.LogGroup;

import java.util.ArrayList;
import java.util.Calendar;

public class LogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final ArrayList<LogGroup> mLogs;
    final private ListItemClickListener mOnListItemClickListener;

    public LogsAdapter(ArrayList<LogGroup> logs, ListItemClickListener mOnListItemClickListener) {
        this.mLogs = logs;
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

    private static final int GROUP_HEADER_TYPE = 0;
    private static final int NORMAL_ITEM_TYPE = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case GROUP_HEADER_TYPE: {
                return new LogsGroupViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_group_log, viewGroup, false));
            }
            case NORMAL_ITEM_TYPE: {
                return new LogsViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_log, viewGroup, false));
            }
            default: {
                return new LogsViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_log, viewGroup, false));
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Log log = mLogs.get(viewHolder.getAdapterPosition()).getLog();

        if (viewHolder instanceof LogsViewHolder) {
            if (log != null) {
                ((LogsViewHolder) viewHolder).time.setText(String.valueOf(
                        (log.getStartTime().get(Calendar.HOUR) < 10 ?
                                "0" + log.getStartTime().get(Calendar.HOUR):
                                log.getStartTime().get(Calendar.HOUR)) + ":" +
                                (log.getStartTime().get(Calendar.MINUTE) < 10 ?
                                        "0" + log.getStartTime().get(Calendar.MINUTE):
                                        log.getStartTime().get(Calendar.MINUTE)) + " " +
                                (log.getStartTime().get(Calendar.AM_PM) == 0 ? "AM" : "PM") + " - " +

                                (log.getEndTime() == null ? viewHolder.itemView.getContext().getString(R.string.tap_to_complete) : (
                                        (log.getEndTime().get(Calendar.HOUR) < 10 ?
                                                "0" + log.getEndTime().get(Calendar.HOUR):
                                                log.getEndTime().get(Calendar.HOUR)) + ":" +
                                                (log.getEndTime().get(Calendar.MINUTE) < 10 ?
                                                        "0" + log.getEndTime().get(Calendar.MINUTE):
                                                        log.getEndTime().get(Calendar.MINUTE)) + " " +
                                                (log.getEndTime().get(Calendar.AM_PM) == 0 ? "AM" : "PM")
                                        ))
                ));
                ((LogsViewHolder) viewHolder).typeLog.setText(log.getLogType());
            }
        }
        if (viewHolder instanceof LogsGroupViewHolder) {
            ((LogsGroupViewHolder) viewHolder).title.setText(mLogs.get(viewHolder.getAdapterPosition()).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mLogs.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mLogs.get(position).getLog() != null) {
            return NORMAL_ITEM_TYPE;
        } else {
            return GROUP_HEADER_TYPE;
        }
    }

    private class LogsGroupViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public LogsGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
        }
    }

    private class LogsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView time;
        final TextView typeLog;

        public LogsViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_time);
            typeLog = itemView.findViewById(R.id.tv_type_log);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
