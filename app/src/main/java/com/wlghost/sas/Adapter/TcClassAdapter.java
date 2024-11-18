package com.wlghost.sas.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Domain.TcSubject;
import com.wlghost.sas.R;

import java.util.List;

public class TcClassAdapter extends RecyclerView.Adapter<TcClassAdapter.ViewModel> {
    private static List<TcSubject> classtList;
    private Context mContext;
    private static int selectedItem = -1;

    public TcClassAdapter(Context context, List<TcSubject> classtList) {
        TcClassAdapter.classtList = classtList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public TcClassAdapter.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewModel(LayoutInflater.from(mContext).inflate(R.layout.item_subject_gridview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TcClassAdapter.ViewModel holder, int position) {
        TcSubject subject = classtList.get(position);
        holder.radio_button.setText(subject.getName());
        holder.item_text.setText(subject.getId());
        holder.radio_button.setChecked(position == selectedItem);
        holder.radio_button.setOnClickListener(v -> {
            selectedItem = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return classtList.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        public RadioButton radio_button;
        public TextView item_text;

        public ViewModel(@NonNull View itemView) {
            super(itemView);
            radio_button = itemView.findViewById(R.id.radio_button);
            item_text = itemView.findViewById(R.id.item_Id);
        }
    }

    public static String getSelectedItem() {
        if (selectedItem >= 0 && selectedItem < classtList.size()) {
            return classtList.get(selectedItem).getId();
        } return null;
    }
}
