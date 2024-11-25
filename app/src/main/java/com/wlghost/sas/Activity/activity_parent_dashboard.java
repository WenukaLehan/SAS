package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.wlghost.sas.Helper.SessionManager;

import com.wlghost.sas.R;

public class activity_parent_dashboard extends AppCompatActivity {


    private  ImageView backbtn;
    private SessionManager sessionManager;

    MaterialButton attendanceButton;
    MaterialButton viewMarksButton;
    MaterialButton announcementsButton;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_dashboard);
        sessionManager = new SessionManager(getApplicationContext());

        drawerLayout = findViewById(R.id.main1);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_parent);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(activity_parent_dashboard.this, activity_choosechild.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_parent_dashboard.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_parent_dashboard.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_parent_dashboard.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);




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


        attendanceButton = findViewById(R.id.attendanceButton);
        viewMarksButton = findViewById(R.id.viewMarksButton);
        announcementsButton = findViewById(R.id.announcementsButton);


        // Set click listener for the attendance button
        attendanceButton.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, activity_attendance_parent.class);
            intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            intent.putExtra("studentId", studentId);
            startActivity(intent);

        });

        // Set click listener for the viewmark button
        viewMarksButton.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, activity_parent_viewreports.class);
            intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            intent.putExtra("studentId", studentId);
            startActivity(intent);

        });

        // Set click listener for the announcement button
        announcementsButton.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, Announcement_parent.class);
            intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            intent.putExtra("studentId", studentId);
            startActivity(intent);

        });




        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(v -> {

            Intent intent = new Intent(activity_parent_dashboard.this, activity_choosechild.class);
            //intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            startActivity(intent);

        });
    }
}