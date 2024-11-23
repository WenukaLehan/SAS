package com.wlghost.sas.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wlghost.sas.Adapter.AttendanceAdapter;
import com.wlghost.sas.Domain.AttendanceModel;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class activity_attendance extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    dbCon DBCon = new dbCon();
    private List<AttendanceModel> attendanceList;
    private DocumentReference db;
    private String teacherId = "200401"; // Replace with dynamic retrieval if needed
    private static final String TAG = "activity_attendance";
    private String currentDateg;
    private SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        drawerLayout = findViewById(R.id.main6);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teacheratten);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Intent intent = new Intent(activity_attendance.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_attendance.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_attendance.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_attendance.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main6), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        teacherId = sessionManager.getUserId();

        // Inside onCreate method
        TextView currentDate = findViewById(R.id.currentDate);

        // Get the current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDateg = sdf.format(new Date());

        // Set the date to the TextView
        currentDate.setText(currentDateg);

        // Initialize Firestore and UI components
        db = DBCon.getDb();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();

        // Handle back button click
        findViewById(R.id.backBtn1).setOnClickListener(v -> finish());

        // Load attendance data
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        try {
            db.collection("teachers").document(teacherId)
                    .get()
                    .addOnSuccessListener(teacherDoc -> {
                        if (teacherDoc.exists()) {
                            String classId = teacherDoc.getString("classID");
                            fetchStudentsForClass(classId);
                        } else {
                            Toast.makeText(this, "Teacher document not found.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Teacher document not found.");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching teacher data: ", e));
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error fetching teacher data: ", e);
        }
    }

    private void fetchStudentsForClass(String classId) {
        db.collection("students")
                .whereEqualTo("classId", classId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, String> studentIds = new HashMap<>();
                            for (DocumentSnapshot studentDoc : task.getResult()) {
                                studentIds.put(studentDoc.getId(), studentDoc.getString("disName"));
                            }
                            fetchAttendanceStatus(studentIds);
                        }
                        else{
                            Toast.makeText(activity_attendance.this, "Error fetching student data.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error fetching student data: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching students for class: ", e));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchAttendanceStatus(Map<String, String> studentIds) {
        // Clear the list to prevent duplicates if the method is called again
        attendanceList.clear();

        for (Map.Entry<String, String> entry : studentIds.entrySet()) {
            db.collection("attendence").document(currentDateg).collection("students")
                    .whereEqualTo("id", entry.getKey())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            if (!task.getResult().isEmpty()) {
                                for (DocumentSnapshot attendanceDoc : task.getResult()) {
                                    String inTime = attendanceDoc.getString("in_time");
                                    String outTime = attendanceDoc.getString("out_time");
                                    //String attendanceStatus = inTime != null && outTime != null ? "Present" : "Absent";

                                    attendanceList.add(new AttendanceModel(entry.getKey(), "Present", inTime, outTime, entry.getValue()));
                                }
                            }else{
                                attendanceList.add(new AttendanceModel(entry.getKey(), "Absent", null, null, entry.getValue()));
                            }
                        } else {
                            Log.e("wenuka", "Error fetching attendance data: ", task.getException());
                        }

                        // Notify the adapter after all data is fetcheddddddddd
                        if (adapter == null) {
                            adapter = new AttendanceAdapter(attendanceList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(e -> Log.e("wenuka", "Error fetching attendance data: ", e));
        }
    }



}
