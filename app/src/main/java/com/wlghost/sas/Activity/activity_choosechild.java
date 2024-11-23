package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.StudentAdapter;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Domain.Student;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.List;

public class activity_choosechild extends AppCompatActivity {


    private SessionManager sessionManager;

    private RecyclerView recyclerView;

    private StudentAdapter adapter;
    private List<Student> studentList = new ArrayList<>();

    // Use centralized database connection from dbCon
    private dbCon DBCon = new dbCon();
    private DocumentReference schoolDocRef = DBCon.getDefaultSchoolDocument();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choosechild);

        sessionManager = new SessionManager(this);
        drawerLayout = findViewById(R.id.main4);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_choose);

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
                    Toast.makeText(activity_choosechild.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    if (sessionManager.isLoggedIn()) {
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_choosechild.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_choosechild.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_choosechild.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize student list and adapter
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList);

        adapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(adapter);

        loadStudentData();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main4), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }




        private void loadStudentData() {
            String parentEmail = mAuth.getCurrentUser().getEmail();

            // Access students collection within the school document, filtered by parent email
            schoolDocRef.collection("students")
                    .whereEqualTo("email", parentEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            studentList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Student student = document.toObject(Student.class);
                                student.setStId(document.getId());
                                studentList.add(student);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the error here
                        }
                    });






    }
}