package com.wlghost.sas.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Activity.activity_show_subject_marks;
import com.wlghost.sas.Domain.ClassStudentMarks;
import com.wlghost.sas.R;

import java.util.List;

public class Adapter_student_list_mark extends RecyclerView.Adapter<Adapter_student_list_mark.ViewHolder> {
    private static List<ClassStudentMarks> Students;
    private Context aContext;
    public Adapter_student_list_mark(Context context, List<ClassStudentMarks> students) {
        Students = students;
        aContext = context;
    }

    @NonNull
    @Override
    public Adapter_student_list_mark.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        aContext = parent.getContext();
        return new Adapter_student_list_mark.ViewHolder(LayoutInflater.from(aContext).inflate(R.layout.item_student_mark, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_student_list_mark.ViewHolder holder, int position) {
        ClassStudentMarks student = Students.get(position);
        holder.name.setText(student.getStudentName());
        holder.total.setText(String.valueOf(student.getTotalMarks()));
        holder.place.setText(String.valueOf(student.getPlace()));
        holder.avg.setText(String.format("%.2f", student.getAverage()));
        holder.itemView.setOnClickListener(v -> {
            // Handle item click if needed
            Intent intent = new Intent(aContext, activity_show_subject_marks.class);
            intent.putExtra("studentId", student.getStudentId());
            intent.putExtra("studentName", student.getStudentName());
            intent.putExtra("totalMarks", student.getTotalMarks());
            intent.putExtra("place", student.getPlace());
            intent.putExtra("average", student.getAverage());
            intent.putExtra("marksMap", (java.io.Serializable) student.getMarksMap());
            aContext.startActivity(intent);
        });
        if(student.getAverage() > 70.0){
            holder.avg.setTextColor(aContext.getResources().getColor(R.color.green));
        }else if(student.getAverage() > 60.0){
            holder.avg.setTextColor(aContext.getResources().getColor(R.color.yellow));
        }else if(student.getAverage() > 50.0){
            holder.avg.setTextColor(aContext.getResources().getColor(R.color.orange));
        }else{
            holder.avg.setTextColor(aContext.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
       return Students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, total,place,avg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.StudentName);
            total = itemView.findViewById(R.id.totalMark);
            place = itemView.findViewById(R.id.place);
            avg = itemView.findViewById(R.id.avarage);
        }
    }
}
