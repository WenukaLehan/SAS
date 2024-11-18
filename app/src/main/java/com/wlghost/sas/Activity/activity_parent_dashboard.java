package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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
    private  ImageView backbtn;
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
        //get intet extra data
        String studentName = getIntent().getStringExtra("studentName");
        String studentId = getIntent().getStringExtra("studentId");
        TextView studentNameTextView = findViewById(R.id.myClass);
        studentNameTextView.setText(studentName);

        // Initialize Session Manager
        sessionManager = new SessionManager(getApplicationContext());

        attendanceButton = findViewById(R.id.attendanceButton);
        viewMarksButton = findViewById(R.id.viewMarksButton);
        announcementsButton = findViewById(R.id.announcementsButton);


        // Set click listener for the attendance button
        attendanceButton.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, activity_attendance_parent.class);
            intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            startActivity(intent);

        });

        // Set click listener for the attendance button
        viewMarksButton.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, activity_parent_viewreports.class);
            intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            startActivity(intent);

        });


        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(v -> {
            if(sessionManager.isLoggedIn()){
                sessionManager.logoutUser();
                startActivity(new Intent(activity_parent_dashboard.this, login_activity.class));
                finish();
                Toast.makeText(activity_parent_dashboard.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }

        });

        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, activity_choosechild.class);
            //intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            startActivity(intent);

        });
    }
}