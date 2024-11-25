package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

public class activity_teacher_class extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;
    private SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_class);

        sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.main5);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teacherclass);

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
                    Intent intent = new Intent(activity_teacher_class.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_teacher_class.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_teacher_class.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_teacher_class.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main5), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.teaccherName);

        textView1 = findViewById(R.id.emailAdd);

       //when user clicks backBtn then activity_teacher_class will be open
        findViewById(R.id.backBtn).setOnClickListener(v -> onBackPressed());

        //when user clicks attendanceButton then activity_attendance will be open
        findViewById(R.id.attendanceButton).setOnClickListener(v -> startActivity(new Intent(this, activity_attendance.class)));


        //when user clicks viewMarksButton then activity_view_marks will be open
        findViewById(R.id.viewMarksButton).setOnClickListener(v -> startActivity(new Intent(this, activity_view_marks.class)));

        //when user clicks announcementsButton then activity_announcements will be open
        findViewById(R.id.announcementsButton).setOnClickListener(v -> startActivity(new Intent(this, activity_announcements.class)));

        textView.setText(sessionManager.getUserName());
        textView1.setText(sessionManager.getUserEmail());

    }

}