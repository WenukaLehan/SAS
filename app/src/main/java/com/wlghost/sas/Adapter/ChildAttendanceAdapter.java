package com.wlghost.sas.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.AttendanceRecord;
import com.wlghost.sas.R;

import java.util.List;

public class ChildAttendanceAdapter extends RecyclerView.Adapter<ChildAttendanceAdapter.ChildAttendanceAdapterViewHolder> {
    private List<AttendanceRecord> attendanceList;
    Context mContext;

    public ChildAttendanceAdapter(List<AttendanceRecord> attendanceList,Context context) {
        this.attendanceList = attendanceList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ChildAttendanceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.childattendance_cardview, parent, false);
        return new ChildAttendanceAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChildAttendanceAdapterViewHolder holder, int position) {
        AttendanceRecord record = attendanceList.get(position);
        holder.dateTextView.setText(record.getDate());
        holder.arrivedTimeTextView.setText(record.getArrivedTime());
        holder.leftTimeTextView.setText(record.getLeftTime());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class ChildAttendanceAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, arrivedTimeTextView, leftTimeTextView;

        public ChildAttendanceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.Date);
            arrivedTimeTextView = itemView.findViewById(R.id.ArivedTime);
            leftTimeTextView = itemView.findViewById(R.id.LeftTime);
        }
    }
}
