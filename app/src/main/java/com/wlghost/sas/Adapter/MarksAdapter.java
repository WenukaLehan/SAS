package com.wlghost.sas.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.Mark;
import com.wlghost.sas.R;

import java.util.List;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MarkViewHolder> {

    private final List<Mark> marksList;
    private final Context context;

    public MarksAdapter(List<Mark> marksList, Context context) {
        this.marksList = marksList;
        this.context = context;
    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewmark_cardview, parent, false);
        return new MarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder holder, int position) {
        Mark mark = marksList.get(position);
        holder.subjectTextView.setText(mark.getSubject());
        holder.markTextView.setText(mark.getMarks());
        holder.gradeTextView.setText(mark.getGrade());
    }

    @Override
    public int getItemCount() {
        return marksList.size();
    }

    static class MarkViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView, markTextView, gradeTextView;

        public MarkViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.Subject);
            markTextView = itemView.findViewById(R.id.Mark);
            gradeTextView = itemView.findViewById(R.id.Grade);
        }
    }
}
