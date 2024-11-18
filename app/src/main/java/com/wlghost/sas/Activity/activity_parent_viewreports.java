package com.wlghost.sas.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wlghost.sas.R;

public class activity_parent_viewreports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_viewreports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main11), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get student data passed from Parent Dashboard
        String studentId = getIntent().getStringExtra("studentId");
        String studentName = getIntent().getStringExtra("studentName");

        // Display student ID (replace with real data fetch logic if needed)
        TextView viewreportTitle = findViewById(R.id.viewreport);
        viewreportTitle.setText("Student Name: " + studentName);
    }
}