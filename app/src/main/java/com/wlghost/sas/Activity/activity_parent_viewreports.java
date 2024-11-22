package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import com.google.android.material.navigation.NavigationView;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.R;

public class activity_parent_viewreports extends AppCompatActivity {

    private RadioGroup semesterRadioGroup;
    private SessionManager sessionManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ImageView backbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_viewreports);



        drawerLayout = findViewById(R.id.main11);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_pareviewrepo);

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
                    Toast.makeText(activity_parent_viewreports.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {

                    if (sessionManager.isLoggedIn()) {
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_parent_viewreports.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_parent_viewreports.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(activity_parent_viewreports.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);






        // Initialize RadioGroup
        semesterRadioGroup = findViewById(R.id.semesterRadioGroup);


        // Retrieve student data passed from Parent Dashboard
        String studentId = getIntent().getStringExtra("studentId");
        String studentName = getIntent().getStringExtra("studentName");

        
        // Display student name
        TextView viewReportTitle = findViewById(R.id.viewreport);
        viewReportTitle.setText("Student Name: " + studentId);

        // Set listener for RadioGroup
        semesterRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.firstSemester) {
                navigateToSemesterPage(studentId, "1stSem");
            } else if(checkedId == R.id.secondSemester) {
                navigateToSemesterPage(studentId, "2ndSem");
            } else if(checkedId == R.id.thirdSemester) {
                navigateToSemesterPage(studentId, "3rdSem");
            }

        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main11), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }



    /**
     * Navigates to the semester page with the selected semester and student ID.
     */
    private void navigateToSemesterPage(String studentId, String semester) {
        if (studentId == null || semester == null) {
            Toast.makeText(this, "Missing data for navigation.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(activity_parent_viewreports.this, activity_semester.class);
        intent.putExtra("studentId", studentId);
        intent.putExtra("semester", semester);
        startActivity(intent);
    }
}