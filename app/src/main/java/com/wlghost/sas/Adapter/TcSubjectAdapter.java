package com.wlghost.sas.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GnssAntennaInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Activity.activity_mysubjects;
import com.wlghost.sas.Domain.TcSubject;
import com.wlghost.sas.Helper.OnISubjectClickListener;
import com.wlghost.sas.R;

import java.util.List;

public class TcSubjectAdapter extends RecyclerView.Adapter<TcSubjectAdapter.ViewHolder> {
    private static List<TcSubject> subjectList;
    private Context mContext;
    private static int selectedItem = -1;
    private OnISubjectClickListener OnSubjectClickListener;

    public TcSubjectAdapter(Context context,List<TcSubject> subjectList, OnISubjectClickListener listener) {
        TcSubjectAdapter.subjectList = subjectList;
        this.mContext = context;
        this.OnSubjectClickListener = listener;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TcSubjectAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the data model based on position
        TcSubject subject = subjectList.get(position);
        holder.radio_button.setText(subject.getName());
        holder.item_text.setText(subject.getId());
        holder.radio_button.setChecked(position == selectedItem);
        holder.radio_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                selectedItem = position;
                OnSubjectClickListener.initClasses(subject.getId());
                notifyDataSetChanged();
            }
        });
    }



    @NonNull
    @Override
    public TcSubjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_subject_gridview, parent, false));
    }



    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RadioButton radio_button;
        public TextView item_text;

        public ViewHolder(View inflate) {
            super(inflate);
            radio_button = itemView.findViewById(R.id.radio_button);
            item_text = itemView.findViewById(R.id.item_Id);
        }
    }

    //Method to get selected item text
    public static String getSelectedItem() {
        if (selectedItem >= 0 && selectedItem < subjectList.size()) {
            return subjectList.get(selectedItem).getId();
        } return null;
    }
}
