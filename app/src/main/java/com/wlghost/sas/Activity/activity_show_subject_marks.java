package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.wlghost.sas.Adapter.StudentAdapterViewMarks;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.List;
import java.util.Map;

public class activity_show_subject_marks extends AppCompatActivity {

    private String Name,Id;
    private int place,totalMarks;
    private double avg;
    private Map<String, Integer> marks;
    private DocumentReference db;
    private List<StudentMarks> studentList;
    private StudentAdapterViewMarks adapter;
    private RecyclerView recyclerView;

    private TextView stName,Total,avg1,place1;


    private SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_subject_marks);

        sessionManager = new SessionManager(this);
        drawerLayout = findViewById(R.id.showSub);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teacchershowsubject);

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
                    Intent intent = new Intent(activity_show_subject_marks.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_show_subject_marks.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_show_subject_marks.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_show_subject_marks.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.showSub), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            dbCon DBcon = new dbCon();
            db = DBcon.getDb();
            Id = getIntent().getStringExtra("studentId");
            place = getIntent().getIntExtra("place", 0);
            totalMarks = getIntent().getIntExtra("totalMarks", 0);
            avg = getIntent().getDoubleExtra("average", 0);
            marks = (Map<String, Integer>) getIntent().getSerializableExtra("marksMap");

            stName = findViewById(R.id.stName);
            Total = findViewById(R.id.Total);
            avg1 = findViewById(R.id.avg);
            place1 = findViewById(R.id.place);
            recyclerView = findViewById(R.id.subjects_list);

            fetchMarks();
        }
        catch (Exception e){
            Toast.makeText(this, "Error fetching marks", Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchMarks() {
        //adapter.clear();
        studentList = new java.util.ArrayList<>();
        db.collection("students").document(Id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Name = documentSnapshot.getString("fullName");
                    stName.setText(Name);
                    Total.setText(String.valueOf(totalMarks));
                    avg1.setText(String.format("%.2f", avg));
                    place1.setText(String.valueOf(place));
                    for (Map.Entry<String, Integer> entry : marks.entrySet()) {
                        studentList.add(new StudentMarks("sd",entry.getKey(), entry.getValue()));
                    }
                    adapter = new StudentAdapterViewMarks(studentList, this);
                    recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching marks", Toast.LENGTH_SHORT).show());
    }
}