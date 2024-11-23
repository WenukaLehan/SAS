package com.wlghost.sas.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wlghost.sas.Adapter.AttendanceAdapter;
import com.wlghost.sas.Adapter.ChildAttendanceAdapter;
import com.wlghost.sas.Domain.AttendanceModel;
import com.wlghost.sas.Domain.AttendanceRecord;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class activity_attendance_parent extends AppCompatActivity {


    private ImageView backbtn;
    private RecyclerView recyclerView;
    private ChildAttendanceAdapter adapter;
    private dbCon DBCon = new dbCon();
    private SessionManager sessionManager;
    private static final String TAG = "activity_attendance_parent";
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance_parent);


        drawerLayout = findViewById(R.id.main10);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_atten);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sessionManager = new SessionManager(this);


        // Handle Navigation Item Clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Toast.makeText(activity_attendance_parent.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    if (sessionManager.isLoggedIn()) {
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_attendance_parent.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_attendance_parent.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_attendance_parent.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main10), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // Initialize Firestore and UI components
        recyclerView = findViewById(R.id.resultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get parent's email from intent
        String studentId = getIntent().getStringExtra("studentId");

        // Handle back button click
        findViewById(R.id.attcBack).setOnClickListener(v -> finish());

        // Load attendance data for parent's registered students
        loadAttendanceData(studentId);

        



    }

    public static ArrayList<String> getLastWeekDates() {
        ArrayList<String> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Check if today is a weekday
        int todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (todayDayOfWeek != Calendar.SATURDAY && todayDayOfWeek != Calendar.SUNDAY) {
            dates.add(sdf.format(calendar.getTime())); // Add today's date
        }

        // Iterate to get previous working days
        int workingDays = dates.size(); // Start with the current size (0 or 1 if today is added)
        while (workingDays < 5) {
            // Move one day back
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            // Check if it's not Saturday (7) or Sunday (1)
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                dates.add(sdf.format(calendar.getTime()));
                workingDays++;
            }
        }

        return dates;
    }

    private void loadAttendanceData(String studentId) {
        ArrayList<String> dates = getLastWeekDates();
        List<AttendanceRecord> attendanceList = new ArrayList<>();
        //String date = "17-11-2024";
        for (String date : dates) {
            try {
                // Fetch students associated with the parent's email
                DBCon.getDb().collection("attendence").document(date).collection("students")
                        .whereEqualTo("id", studentId) // Filter by parent email
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() ) {
                                for (DocumentSnapshot doc : task.getResult()) {
                                    String inTime = doc.getString("in_time");
                                    String outTime = doc.getString("out_time");
                                    attendanceList.add(new AttendanceRecord(date, inTime, outTime));
                                }
                                // Update the UI on the main thread
                                runOnUiThread(() -> {
                                    // Handle updating the adapter here
                                    adapter = new ChildAttendanceAdapter(attendanceList,activity_attendance_parent.this);
                                    recyclerView.setAdapter(adapter);
                                });
                            } else {
                                Log.e(TAG, "No documents found for date: " + date);
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error fetching students for date: " + date, e);
                            runOnUiThread(() -> Toast.makeText(activity_attendance_parent.this, "Error fetching attendance for date: " + date, Toast.LENGTH_SHORT).show());
                        });

            } catch (Exception e) {
                Log.e(TAG, "Error in loadAttendanceData: ", e);
                runOnUiThread(() -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }






}