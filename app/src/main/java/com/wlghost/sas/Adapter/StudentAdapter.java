package com.wlghost.sas.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Activity.activity_parent_dashboard;
import com.wlghost.sas.Activity.activity_view_marks;
import com.wlghost.sas.Domain.Student;
import com.wlghost.sas.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final List<Student> studentList;

    public StudentAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }


    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_gridview, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        Student student = studentList.get(position);
        holder.childName.setText(student.getDisName());
        holder.childGrade.setText("Grade " + student.getGrade());
        holder.childId.setText(student.getStId());
        holder.childDob.setText(student.getDob());

        holder.constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), activity_parent_dashboard.class);
            intent.putExtra("studentId", student.getStId());
            intent.putExtra("studentName", student.getDisName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView childName, childGrade, childId, childDob;
        ConstraintLayout constraintLayout;
        public StudentViewHolder (@NonNull View itemView) {
            super(itemView);
            childName = itemView.findViewById(R.id.ChildName);
            childGrade = itemView.findViewById(R.id.childGrade);
            childId = itemView.findViewById(R.id.ChildId);
            childDob = itemView.findViewById(R.id.childDOB);
            constraintLayout = itemView.findViewById(R.id.child_gridview);
        }
    }
}
