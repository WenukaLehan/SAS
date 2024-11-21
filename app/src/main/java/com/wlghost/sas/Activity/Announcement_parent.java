package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.AnnouncementAdapter;
import com.wlghost.sas.Domain.Announcement;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Announcement_parent extends AppCompatActivity {


    private ImageView accnbtn;

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcements;
    private dbCon dbConnection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement_parent);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Initialize RecyclerView
        recyclerView = findViewById(R.id.resultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcements = new ArrayList<>();
        adapter = new AnnouncementAdapter(this, announcements);
        recyclerView.setAdapter(adapter);

        // Initialize dbCon
        dbConnection = new dbCon();

        // Fetch current student's class ID and load announcements
        fetchClassIdAndAnnouncements();


    }

    private void fetchClassIdAndAnnouncements() {
        // Assuming `SessionManager` stores current student ID
        String studentId = new SessionManager(getApplicationContext()).getUserId();

        // Access the "students" collection via dbCon
        DocumentReference schoolDoc = dbConnection.getDefaultSchoolDocument();

        schoolDoc.collection("students").document(studentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String classId = documentSnapshot.getString("classId");
                        if (classId != null) {
                            loadAnnouncements(classId);
                        } else {
                            Toast.makeText(this, "Class ID not found for student", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch class ID", Toast.LENGTH_SHORT).show());
    }

    private void loadAnnouncements(String classId) {
        // Query announcements specific to the student's class ID
        DocumentReference schoolDoc = dbConnection.getDefaultSchoolDocument();

        schoolDoc.collection("announcements").whereEqualTo("receiver", classId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    announcements.clear();
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Announcement> fetchedAnnouncements = queryDocumentSnapshots.toObjects(Announcement.class);
                        announcements.addAll(fetchedAnnouncements);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "No announcements found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load announcements", Toast.LENGTH_SHORT).show());

        accnbtn = findViewById(R.id.anncBack);
        accnbtn.setOnClickListener(v -> {
            String studentName = getIntent().getStringExtra("studentName");
            Intent intent = new Intent(Announcement_parent.this, activity_parent_dashboard.class);
            intent.putExtra("studentName", studentName); // Pass student ID for Attendance
            startActivity(intent);

        });
    }

}