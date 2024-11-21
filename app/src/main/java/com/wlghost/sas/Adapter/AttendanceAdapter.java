package com.wlghost.sas.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.AttendanceModel;
import com.wlghost.sas.R;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<AttendanceModel> attendanceList;

    // Constructor
    public AttendanceAdapter(List<AttendanceModel> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceModel attendance = attendanceList.get(position);
        holder.studentIdTextView.setText(attendance.getStudentId());
        holder.attendanceStatusTextView.setText(attendance.getAttendanceStatus());
        if (attendance.getAttendanceStatus().equals("Present")) {
            holder.attendanceStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
        } else {
            holder.attendanceStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    // ViewHolder class
    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView studentIdTextView;
        TextView attendanceStatusTextView;



        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            studentIdTextView = itemView.findViewById(R.id.studentIdTextView);
            attendanceStatusTextView = itemView.findViewById(R.id.attendanceStatusTextView);


        }
    }
}
