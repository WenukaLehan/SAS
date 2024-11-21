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


        // Set a click listener to display the popup dialog
        holder.itemView.setOnClickListener(v -> {
            showPopupDialog(context, announcement); // Show popup dialog with the selected announcement
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

    /**
     * Show a popup dialog to display the announcement details.
     * @param context The context in which the dialog is shown.
     * @param announcement The announcement object containing the details to display.
     */
    private void showPopupDialog(Context context, Announcement announcement) {
        // Create an AlertDialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setCancelable(false); // Prevent dialog dismissal on outside touch

        // Inflate the custom layout (announcement_details.xml)
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_announcement_details, null);

        // Initialize UI components from the dialog layout
        TextView announcementType = dialogView.findViewById(R.id.announcementDetailType);
        TextView announcementMessage = dialogView.findViewById(R.id.announcementDetailMsg);
        TextView okButton = dialogView.findViewById(R.id.announcementDetailOK);

        // Set announcement data in the popup
        announcementType.setText(announcement.getType());
        announcementMessage.setText(announcement.getMsg());

        // Set the custom layout to the AlertDialog
        builder.setView(dialogView);

        // Create the dialog instance
        final androidx.appcompat.app.AlertDialog dialog = builder.create();

        // Set a click listener for the "OK" button to close the dialog
        okButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }
}