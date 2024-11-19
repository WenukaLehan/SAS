package com.wlghost.sas.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.OnMarksAddListener;
import com.wlghost.sas.R;

import java.util.List;

public class StudentAdapterViewMarks extends RecyclerView.Adapter<StudentAdapterViewMarks.ViewHolder> {
    private static List<StudentMarks> studentList;
    Context mContext;
    public StudentAdapterViewMarks(List<StudentMarks> studentList, Context context) {
        StudentAdapterViewMarks.studentList = studentList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public StudentAdapterViewMarks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_viewmarks, parent, false);
        return new StudentAdapterViewMarks.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapterViewMarks.ViewHolder holder, int position) {
        StudentMarks student = studentList.get(position);
        holder.name.setText(student.getName());
        if(student.getMarks() > 0 ){
            holder.marks.setText(String.valueOf(student.getMarks()));
        }
        holder.grade.setText(getGrade(student.getMarks()));
        switch (getGrade(student.getMarks())) {
            case "A":
                holder.grade.setTextColor(mContext.getResources().getColor(R.color.green));
                break;
            case "B":
                holder.grade.setTextColor(mContext.getResources().getColor(R.color.yellow));
                break;
            case "C":
                holder.grade.setTextColor(mContext.getResources().getColor(R.color.orange));
                break;
            case "S":
                holder.grade.setTextColor(mContext.getResources().getColor(R.color.orangeDarck));
                break;
            case "F":
                holder.grade.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
        }



    }

    private String getGrade(int marks) {
        if (marks >= 75) {
            return "A";
        } else if (marks >= 65) {
            return "B";
        } else if (marks >= 55) {
            return "C";
        } else if (marks >= 35) {
            return "S";
        } else {
            return "F";
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        studentList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, marks,grade;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentNameTextView);
            marks = itemView.findViewById(R.id.MarksTextView);
            grade = itemView.findViewById(R.id.grade);
        }
    }
}
