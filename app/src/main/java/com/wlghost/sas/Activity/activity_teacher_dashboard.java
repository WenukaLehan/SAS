package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentReference;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

public class activity_teacher_dashboard extends AppCompatActivity {

    SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DocumentReference db;
    dbCon DbCon = new dbCon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_dashboard);
        drawerLayout = findViewById(R.id.main2);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teacher);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        sessionManager = new SessionManager(getApplicationContext());

        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Toast.makeText(activity_teacher_dashboard.this, "Home clicked", Toast.LENGTH_SHORT).show();
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_teacher_dashboard.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_teacher_dashboard.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_teacher_dashboard.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        db = DbCon.getDb();
        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnDisable(sessionManager.getUserId());

        //when user click btnMyClass then activity_teacher_class will open
        findViewById(R.id.btnMyClass).setOnClickListener(v -> {
            startActivity(new Intent(activity_teacher_dashboard.this, activity_teacher_class.class));
        });

        //when user click btnMySubjects then activity_mysubjects will open
        findViewById(R.id.btnMySubjects).setOnClickListener(v -> {
            startActivity(new Intent(activity_teacher_dashboard.this, activity_mysubjects.class));
        });

        findViewById(R.id.emAttendence).setOnClickListener(v -> {
            Intent intent = new Intent(this,Teacher_Emergency_Attendance.class);
            intent.putExtra("userId",sessionManager.getUserId());
            startActivity(intent);
        });


    }

    private void btnDisable(String userId) {
        db.collection("teachers").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String classId = documentSnapshot.getString("classID");
                        if (classId != null) {
                            findViewById(R.id.btnMyClass).setEnabled(true);
                            findViewById(R.id.btnMyClass).setAlpha(1.0f);
                        } else {
                            findViewById(R.id.btnMyClass).setEnabled(false);
                            findViewById(R.id.btnMyClass).setAlpha(0.5f);
                        }
                    }
                });
    }
}