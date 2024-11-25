package com.wlghost.sas.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

public class activity_announcements extends AppCompatActivity {

    private SessionManager sessionManager;
    private dbCon DBCon = new dbCon();
    private DocumentReference db;

    private EditText announcementText;
    private RadioGroup announcementType;
    private MaterialButton submitButton;

    private static  String msg;
    private static  String type;
    private static String sender;
    private static String receiver;


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcements);


        drawerLayout = findViewById(R.id.main8);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teacherannounce);

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
                    Intent intent = new Intent(activity_announcements.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_announcements.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_announcements.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_announcements.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main8), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        db = DBCon.getDb();
        announcementText = findViewById(R.id.announcementText);
        announcementType = findViewById(R.id.announcementType);
        submitButton = findViewById(R.id.submitButton);

        sender = sessionManager.getUserId();
        db.collection("teachers").document(sender).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        receiver = documentSnapshot.getString("classID");
                    }
                });

        announcementType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                type = button.getText().toString();
            }
        });

        submitButton.setOnClickListener(v -> {
            msg = announcementText.getText().toString();
            if (msg.isEmpty()) {
                announcementText.setError("Please enter announcement message");
                announcementText.requestFocus();
                return;
            }
            else {
                updateAnnouncement();
            }

        });

    }

    private void updateAnnouncement() {
        Map<String, Object> announcement = new HashMap<>();
        announcement.put("msg", msg);
        announcement.put("type", type);
        announcement.put("sender", sender);
        announcement.put("receiver", receiver);

        db.collection("announcements").add(announcement)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(activity_announcements.this, "Announcement sent successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(activity_announcements.this, "Failed to send announcement", Toast.LENGTH_SHORT).show());
    }

}