package com.wlghost.sas.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wlghost.sas.Domain.Announcement;
import com.wlghost.sas.R;

public class Announcement_Details extends AppCompatActivity {

    private TextView announcementTitle, announcementMsg, announcementSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main13), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize UI components
        announcementTitle = findViewById(R.id.announcementDetailType);
        announcementMsg = findViewById(R.id.announcementDetailMsg);
        announcementSender = findViewById(R.id.announcementDetailSender);

        // Retrieve the Announcement object from Intent
        Announcement announcement = (Announcement) getIntent().getSerializableExtra("announcement");

        // Populate UI with Announcement details
        if (announcement != null) {
            announcementTitle.setText("Title: " + announcement.getMsg());
            announcementMsg.setText("Message: " + announcement.getMsg());
            announcementSender.setText("Sender: " + announcement.getSender());
        }
    }
}