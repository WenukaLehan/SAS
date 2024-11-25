package com.wlghost.sas.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.StudentAdapterMarks;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.OnMarksAddListener;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class activity_teacher_addmarks extends AppCompatActivity implements OnMarksAddListener {

    RecyclerView recyclerView;
    MaterialButton submitButton;
    StudentAdapterMarks adapter;
    TextView selectSemester;

    dbCon DBcon = new dbCon();
    private DocumentReference db;
    private Map<String, String> marksMap ;

    private static final String TAG = "activity_teacher_addmarks";
    private String clzId;
    private String subId;
    private String semester;


    private SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    // Progress dialog
    private CustomPrograssDialog dialog;



    String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_addmarks);

        sessionManager = new SessionManager(this);

        // Initialize the progress dialog
        dialog = new CustomPrograssDialog(activity_teacher_addmarks.this);



        drawerLayout = findViewById(R.id.main10);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_addmark);

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
                    Intent intent = new Intent(activity_teacher_addmarks.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_teacher_addmarks.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_teacher_addmarks.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_teacher_addmarks.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
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
        db = DBcon.getDb();
        recyclerView = findViewById(R.id.studentlistAddMark);
        submitButton = findViewById(R.id.submitButton);
        selectSemester = findViewById(R.id.selectSemester);

        clzId = getIntent().getStringExtra("classId");
        subId = getIntent().getStringExtra("subjectId");
        //get current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        year = Integer.toString(currentYear);
        marksMap = new HashMap<>();
        checkSem(); // Fetch students for the specified class and subject



        submitButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to submit the marks to? " + year + " " + semester);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle positive button click
                        for (Map.Entry<String, String> entry : marksMap.entrySet()) {
                            String studentId = entry.getKey();
                            String marks = entry.getValue();
                            saveMarksToFirestore(studentId, marks);
                        }
                        Toast.makeText(activity_teacher_addmarks.this, "Marks saved successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle negative button click
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


        });

    }

    private void checkSem(){
        dialog.show();
        db.collection("semister").document("current")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if (task.isSuccessful()) {
                             DocumentSnapshot document = task.getResult();
                             if (document.exists()) {
                                 String sem = document.getString("sem");
                                 semester = sem;
                                 selectSemester.setText("Add marks to the " + year + " " + semester );
                                 initStudents();
                             } else {
                                 Log.d(TAG, "No such document");
                             }
                         } else {
                             Log.d(TAG, "get failed with ", task.getException());
                         }
                         dialog.dismiss();
                     }

                 });
    }

    private void clearRecyclerView() {

        // Ensure adapter is not null before clearing it
        if (adapter != null) {
            adapter.clear();
            Toast.makeText(this, "RecyclerView cleared", Toast.LENGTH_SHORT).show();
        }
    }

    List<StudentMarks> studentList1 = new ArrayList<>();

    private void initStudents() {
        dialog.show();
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
                            getMarks(id, new GetMarksCallback() {
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

                                            Toast.makeText(activity_teacher_addmarks.this, "Students fetched successfully: " + studentList1.size(), Toast.LENGTH_SHORT).show();
                                            adapter = new StudentAdapterMarks(studentList1, activity_teacher_addmarks.this);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(activity_teacher_addmarks.this));
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

                                    Toast.makeText(activity_teacher_addmarks.this, "Students fetched successfully: " + studentList1.size(), Toast.LENGTH_SHORT).show();
                                    adapter = new StudentAdapterMarks(studentList1, activity_teacher_addmarks.this);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(activity_teacher_addmarks.this));
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                });
                            }
                        }
                    }
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", e.getMessage()));
    }


    private void getMarks(String id, GetMarksCallback callback) {
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



    List<StudentMarks> studentList = new ArrayList<>();
    public void fetchStudents(String clazid, String subjectKey) {
        clearRecyclerView();

        db.collection("students")
                .whereEqualTo("classId", clazid) // Filter by class ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId(); // Document ID
                        String name = document.getString("disName"); // Assuming "name" field exists

                        // Extract the "subject" map
                        Map<String, Object> subjectMap = (Map<String, Object>) document.get("subjects");

                        if (subjectMap != null && subjectMap.containsKey(subjectKey)) {
                            // Add student to the list if the subject map contains the key
                            studentList.add(new StudentMarks(id, name,0));
                        }
                    }

                    // Create an adapter and set it to the RecyclerView

                    adapter = new StudentAdapterMarks( studentList, this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", e.getMessage()));
    }

    public void getStudentList(Map<String, String> marksMap1 ) {
        marksMap = marksMap1;
    }

    public void saveMarksToFirestore( String studentId, String marks) {
        db.collection("marks")
                .document(year)
                .collection(semester)
                .document(subId)
                .collection("students")
                .document(studentId)
                .set(Collections.singletonMap("marks", Integer.parseInt(marks)))
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUpdate", "Marks saved for student ID: " + studentId))
                .addOnFailureListener(e -> Log.e("FirestoreUpdateError", "Error saving marks: " + e.getMessage()));
    }

    public interface GetMarksCallback {
        void onResult(int mark);
    }

}

