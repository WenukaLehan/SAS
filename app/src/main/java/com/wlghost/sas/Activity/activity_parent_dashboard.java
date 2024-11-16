package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.wlghost.sas.Helper.SessionManager;

import com.wlghost.sas.R;

public class activity_parent_dashboard extends AppCompatActivity {

    private ImageView profileImage;
    private SessionManager sessionManager;

    MaterialButton attendanceButton;
    MaterialButton viewMarksButton;
    MaterialButton announcementsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        attendanceButton = findViewById(R.id.attendanceButton);
        // Set click listener for the attendance button
        attendanceButton.setOnClickListener(v -> {

        });
        sessionManager = new SessionManager(getApplicationContext());

         attendanceButton = findViewById(R.id.attendanceButton);
         viewMarksButton = findViewById(R.id.viewMarksButton);
         announcementsButton = findViewById(R.id.announcementsButton);

        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> {
            if(sessionManager.isLoggedIn()){
                sessionManager.logoutUser();
                startActivity(new Intent(activity_parent_dashboard.this, login_activity.class));
                finish();
                Toast.makeText(activity_parent_dashboard.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }

        });
    }
}