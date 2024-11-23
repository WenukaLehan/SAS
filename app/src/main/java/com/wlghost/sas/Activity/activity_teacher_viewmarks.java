package com.wlghost.sas.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.StudentAdapterMarks;
import com.wlghost.sas.Adapter.StudentAdapterViewMarks;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class activity_teacher_viewmarks extends AppCompatActivity {

    dbCon DBcon = new dbCon();
    private DocumentReference db;
    RecyclerView recyclerView;
    private StudentAdapterViewMarks adapter;

    private RadioGroup semesters;
    private String semester;
    private String year;
    private String clzId;
    private String subId;

    private SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_viewmarks);

        drawerLayout = findViewById(R.id.main11);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teacherviewmark);

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
                    Intent intent = new Intent(activity_teacher_viewmarks.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_teacher_viewmarks.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_teacher_viewmarks.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_teacher_viewmarks.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main11), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = DBcon.getDb();
        recyclerView = findViewById(R.id.studentlistViewMark);

        semesters = findViewById(R.id.semesters);

        clzId = getIntent().getStringExtra("classId");
        subId = getIntent().getStringExtra("subjectId");

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        year = Integer.toString(currentYear);

        semesters.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.firstSemester) {
                semester = "1stSem";
            } else if (checkedId == R.id.secondSemester) {
                semester = "2ndSem";
            } else if (checkedId == R.id.thirdSemester) {
                semester = "3rdSem";
            }
            checkSem();
        });
    }

    private void checkSem(){
        db.collection("semister").document("current")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String sem = document.getString("sem");
                                if(sem.equals(semester)){
                                    initStudents(); // Fetch students for the specified class and subject
                                }else{
                                    Toast.makeText(activity_teacher_viewmarks.this, "incorrect semester \n currently ongoing semester is "+ sem, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void clearRecyclerView() {
        // Ensure adapter is not null before clearing it
        if (adapter != null) {
            adapter.clear();
        }
    }

    private void getMarks(String id, activity_teacher_addmarks.GetMarksCallback callback) {
        db.collection("marks").document(year).collection(semester)
                .document(subId)
                .collection("students")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                int mark = Objects.requireNonNull(document.getLong("marks")).intValue();
                                callback.onResult(mark);
                            } else {
                                Log.d(TAG, "No such document");
                                callback.onResult(0); // or handle accordingly
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            callback.onResult(0); // or handle accordingly
                        }
                    }
                });
    }

    List<StudentMarks> studentList1 = new ArrayList<>();
    private void initStudents() {
        clearRecyclerView();

        db.collection("students")
                .whereEqualTo("classId", clzId) // Filter by class ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalDocuments = queryDocumentSnapshots.size();
                    final int[] processedDocuments = {0};

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId(); // Document ID
                        String name = document.getString("disName"); // Assuming "disName" field exists

                        // Extract the "subjects" map
                        Map<String, Object> subjectMap = (Map<String, Object>) document.get("subjects");

                        if (subjectMap != null && subjectMap.containsKey(subId)) {
                            // Add student to the list if the subject map contains the key
                            getMarks(id, new activity_teacher_addmarks.GetMarksCallback() {
                                @Override
                                public void onResult(int mark) {
                                    StudentMarks student = new StudentMarks();
                                    student.setId(id);
                                    student.setName(name);
                                    student.setMarks(mark);
                                    studentList1.add(student);

                                    processedDocuments[0]++;

                                    // Check if all documents have been processed
                                    if (processedDocuments[0] == totalDocuments) {
                                        // Create an adapter and set it to the RecyclerView
                                        runOnUiThread(() -> {

                                            Toast.makeText(activity_teacher_viewmarks.this, "Students fetched successfully: " + studentList1.size(), Toast.LENGTH_SHORT).show();
                                            adapter = new StudentAdapterViewMarks(studentList1, activity_teacher_viewmarks.this);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(activity_teacher_viewmarks.this));
                                            recyclerView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        });
                                    }
                                }
                            });
                        } else {
                            processedDocuments[0]++;

                            // Check if all documents have been processed
                            if (processedDocuments[0] == totalDocuments) {
                                // Create an adapter and set it to the RecyclerView
                                runOnUiThread(() -> {

                                    Toast.makeText(activity_teacher_viewmarks.this, "Students fetched successfully: " + studentList1.size(), Toast.LENGTH_SHORT).show();
                                    adapter = new StudentAdapterViewMarks(studentList1, activity_teacher_viewmarks.this);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(activity_teacher_viewmarks.this));
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", e.getMessage()));
    }

}