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

public class activity_choosechild extends AppCompatActivity {

    private ImageView profileImage;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choosechild);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main4), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(getApplicationContext());
        profileImage = findViewById(R.id.profile);
        profileImage.setOnClickListener(v -> {
            if(sessionManager.isLoggedIn()){
                //sessionManager.logoutUser();
                sessionManager.logoutUser();
                startActivity(new Intent(activity_choosechild.this, login_activity.class));
                finish();
                Toast.makeText(activity_choosechild.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }

        });
    }
}