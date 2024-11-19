package com.wlghost.sas.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.Student;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.OnISubjectClickListener;
import com.wlghost.sas.Helper.OnMarksAddListener;
import com.wlghost.sas.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentAdapterMarks extends RecyclerView.Adapter<StudentAdapterMarks.MarksViewHolder> {
    private static List<StudentMarks> studentList;
    Context mContext;
    private static final Map<String, String> marksMap = new HashMap<>();

    public StudentAdapterMarks(List<StudentMarks> studentList, Context context) {
        StudentAdapterMarks.studentList = studentList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_addmarks, parent, false);
        return new MarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksViewHolder holder, int position) {
        StudentMarks student = studentList.get(position);
        holder.nameTextView.setText(student.getName());
        if(student.getMarks() > 0 ){
            holder.marksEditText.setText(String.valueOf(student.getMarks()));
        }

        if (marksMap.containsKey(student.getId())) {
            holder.marksEditText.setText(marksMap.get(student.getId()));
        }
        // Add logic to handle marks (optional, for example save to a local list)
        holder.marksEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Handle marks input for the student
                String marks = s.toString();
                Log.d("MarksInput", "Student: " + student.getName() + " Marks: " + marks);
            }

            @Override
            public void afterTextChanged(Editable s) {
                OnMarksAddListener listener = (OnMarksAddListener) mContext;
                String marks = s.toString();
                marksMap.put(student.getId(), marks);
                listener.getStudentList(marksMap);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        studentList.clear();
        marksMap.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class MarksViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        EditText marksEditText;
        public MarksViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.studentNameTextView);
            marksEditText = itemView.findViewById(R.id.MarksTextView);
        }
    }
}
