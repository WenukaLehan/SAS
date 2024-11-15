package com.wlghost.sas;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_teacher_class extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main5), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       //when user clicks backBtn then activity_teacher_class will be open
        findViewById(R.id.backBtn).setOnClickListener(v -> onBackPressed());

        //when user clicks attendanceButton then activity_attendance will be open
                findViewById(R.id.attendanceButton).setOnClickListener(v -> startActivity(new Intent(this, activity_attendance.class)));


        //when user clicks viewMarksButton then activity_view_marks will be open
        findViewById(R.id.viewMarksButton).setOnClickListener(v -> startActivity(new Intent(this, activity_view_marks.class)));

        //when user clicks announcementsButton then activity_announcements will be open
        findViewById(R.id.announcementsButton).setOnClickListener(v -> startActivity(new Intent(this, activity_announcements.class)));



    }

}