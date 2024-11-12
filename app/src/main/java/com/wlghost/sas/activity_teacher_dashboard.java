package com.wlghost.sas;

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

    }
}