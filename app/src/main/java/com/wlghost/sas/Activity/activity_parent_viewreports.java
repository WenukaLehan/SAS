package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.wlghost.sas.R;

public class activity_parent_viewreports extends AppCompatActivity {

    private RadioGroup semesterRadioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_viewreports);

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