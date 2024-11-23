package com.wlghost.sas.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Activity.teacher_show_all_attendence;
import com.wlghost.sas.Domain.AttendanceModel;
import com.wlghost.sas.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Date compareTime;
        Date time;
            AttendanceModel attendance = attendanceList.get(position);
            holder.studentIdTextView.setText(attendance.getName());
            holder.attendanceStatusTextView.setText(attendance.getAttendanceStatus());

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            try {
                if(attendance.getInTime()!=null) {
                    compareTime = sdf.parse("07:30:00");
                    time = sdf.parse(attendance.getInTime());
                    if (attendance.getAttendanceStatus().equals("Present")) {
                        holder.attendanceStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
                        if (attendance.getInTime() != null) {
                            if (time.after(compareTime)) {
                                holder.layout.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.grey_background));
                            }
                        }
                    } else {
                        holder.attendanceStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
                        holder.layout.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.error_bg));
                    }
                }
                else{
                    if (attendance.getAttendanceStatus().equals("Present")) {
                        holder.attendanceStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));

                    } else {
                        holder.attendanceStatusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
                        holder.layout.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.error_bg));
                    }
                }
            }
            catch (Exception e){
                Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), teacher_show_all_attendence.class);
                intent.putExtra("studentId", attendance.getStudentId());
                intent.putExtra("studentName", attendance.getName());
                holder.itemView.getContext().startActivity(intent);
            });

    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    // ViewHolder class
    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView studentIdTextView;
        TextView attendanceStatusTextView;
        RelativeLayout layout;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            studentIdTextView = itemView.findViewById(R.id.studentIdTextView);
            attendanceStatusTextView = itemView.findViewById(R.id.attendanceStatusTextView);
            layout = itemView.findViewById(R.id.relativeLayout2);
        }
    }
}
