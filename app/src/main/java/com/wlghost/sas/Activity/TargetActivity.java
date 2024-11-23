package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wlghost.sas.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TargetActivity extends AppCompatActivity {

    private static final String TAG = "TargetActivity";
    Button button;
    TextView tite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_target);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tite = findViewById(R.id.title);
        button = findViewById(R.id.submitButton);

        // Retrieve intent data
        String messageTitle = getIntent().getStringExtra("messageTitle");
        String messageBody = getIntent().getStringExtra("messageBody");

        // Log the data for debugging
        Log.d(TAG, "Received Title: " + messageTitle);
        Log.d(TAG, "Received Body: " + messageBody);

        // Display the data (optional)
        TextView titleTextView = findViewById(R.id.titleTextView); // Add these TextViews in activity_target.xml
        TextView bodyTextView = findViewById(R.id.bodyTextView);

        if (messageTitle != null) {
            titleTextView.setText(messageTitle);
            if(messageTitle.equals("Attendence marked")) {
                button.setText("View more attendance");
                tite.setText("Attendance");
            }else{
                TextView tite = findViewById(R.id.title);
                tite.setText("Announcement");
                button.setText("View more announcements");
            }
        }
        if (messageBody != null) {
            bodyTextView.setText(messageBody);
        }
        button.setOnClickListener(v -> {
            if(messageTitle.equals("Attendence marked")) {
                startActivity(new Intent(TargetActivity.this, activity_attendance_parent.class));
            }else{
                startActivity(new Intent(TargetActivity.this, Announcement_parent.class));
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Ensure the new intent is used
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        String messageTitle = intent.getStringExtra("messageTitle");
        String messageBody = intent.getStringExtra("messageBody");

        // Update UI or handle logic if needed
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView bodyTextView = findViewById(R.id.bodyTextView);
        tite = findViewById(R.id.title);
        button = findViewById(R.id.submitButton);

        if (messageTitle != null) {
            titleTextView.setText(messageTitle);
            if(messageTitle.equals("Attendence marked")) {
                button.setText("View more attendance");
                tite.setText("Attendance");
            }else{
                TextView tite = findViewById(R.id.title);
                tite.setText("Announcement");
                button.setText("View more announcements");
            }
        }
        if (messageBody != null) {
            bodyTextView.setText(messageBody);
        }
        button.setOnClickListener(v -> {
            if(messageTitle.equals("Attendence marked")) {
                startActivity(new Intent(TargetActivity.this, activity_attendance_parent.class));
            }else{
                startActivity(new Intent(TargetActivity.this, Announcement_parent.class));
            }
        });
    }
}