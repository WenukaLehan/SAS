package com.wlghost.sas.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.AttendanceRecord;
import com.wlghost.sas.R;

import java.text.SimpleDateFormat;
import java.util.Date;
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


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ChildAttendanceAdapterViewHolder holder, int position) {
        AttendanceRecord record = attendanceList.get(position);
        holder.dateTextView.setText(record.getDate());
        holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.grey_background));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date inTime, compareTime;
        try {
            if(record.getArrivedTime() != null) {
                holder.arrivedTimeTextView.setText(record.getArrivedTime());
                holder.leftTimeTextView.setText(record.getLeftTime());
                 inTime = sdf.parse(record.getArrivedTime());
                 compareTime = sdf.parse("07:30:00");

                if (inTime.after(compareTime)) {
                    holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.error_bg));
                    holder.attendanceCard.setBackground(mContext.getResources().getDrawable(R.drawable.error_bg));
                   // holder.dateTextView.setTextColor(mContext.getResources().getColor(R.color.red));
                   // holder.arrivedTimeTextView.setTextColor(mContext.getResources().getColor(R.color.red));
                    //holder.leftTimeTextView.setTextColor(mContext.getResources().getColor(R.color.red));
                }
            }
            else {
                holder.arrivedTimeTextView.setText("Absent");
                holder.leftTimeTextView.setText("Absent");
                holder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.grey_background));
                holder.attendanceCard.setBackground(mContext.getResources().getDrawable(R.drawable.grey_background));
                holder.leftTimeTextView.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.dateTextView.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.arrivedTimeTextView.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class ChildAttendanceAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, arrivedTimeTextView, leftTimeTextView;
        RelativeLayout attendanceCard;

        public ChildAttendanceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.Date);
            arrivedTimeTextView = itemView.findViewById(R.id.ArivedTime);
            leftTimeTextView = itemView.findViewById(R.id.LeftTime);
            attendanceCard = itemView.findViewById(R.id.attendanceCard);
        }
    }
}
