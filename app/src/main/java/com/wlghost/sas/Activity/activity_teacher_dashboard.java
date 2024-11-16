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

import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.R;

public class activity_teacher_dashboard extends AppCompatActivity {

    private ImageView profile;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(getApplicationContext());

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(v -> {
            // Handle profile image click
            if(sessionManager.isLoggedIn()){
                sessionManager.logoutUser();
                startActivity(new Intent(activity_teacher_dashboard.this, login_activity.class));
                finish();
                Toast.makeText(activity_teacher_dashboard.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });

        //when user click btnMyClass then activity_teacher_class will open
        findViewById(R.id.btnMyClass).setOnClickListener(v -> {
            startActivity(new Intent(activity_teacher_dashboard.this, activity_teacher_class.class));
        });

        //when user click btnMySubjects then activity_mysubjects will open
        findViewById(R.id.btnMySubjects).setOnClickListener(v -> {
            startActivity(new Intent(activity_teacher_dashboard.this, activity_mysubjects.class));
        });

    }
}