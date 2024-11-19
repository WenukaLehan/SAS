package com.wlghost.sas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wlghost.sas.Activity.Announcement_Details;
import com.wlghost.sas.Activity.Announcement_parent;
import com.wlghost.sas.Domain.Announcement;
import com.wlghost.sas.R;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private final List<Announcement> announcements;
    private final Context context; // Needed for Intent navigation

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {

        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_announcement_recyclerview, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.announcementTitle.setText(announcement.getType());
        holder.message.setText(announcement.getMsg());


        // Set click listener to navigate to details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Announcement_Details.class);
            intent.putExtra("announcement", announcement); // Pass Announcement object
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView announcementTitle, message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementTitle = itemView.findViewById(R.id.announcementTitle);
            message = itemView.findViewById(R.id.msg);
        }
    }
}