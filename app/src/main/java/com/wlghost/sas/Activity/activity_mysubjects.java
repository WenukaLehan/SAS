package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wlghost.sas.Adapter.TcClassAdapter;
import com.wlghost.sas.Adapter.TcSubjectAdapter;
import com.wlghost.sas.Domain.TcSubject;
import com.wlghost.sas.Helper.OnISubjectClickListener;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class activity_mysubjects extends AppCompatActivity implements OnISubjectClickListener {
    private RecyclerView recyclerView;
    private TcSubjectAdapter SubAdapter;
    private TcClassAdapter ClassAdapter;
    private MaterialButton uploadMarkBtn;
    private MaterialButton ViewMarksBtn;
    private RecyclerView recyclerView2;


    private dbCon DBcon = new dbCon();
    private DocumentReference db;
    private SessionManager sessionManager;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private static final String TAG = "activity_mysubjects";
    private String teacherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mysubjects);


        drawerLayout = findViewById(R.id.main9);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_teachersubject);

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
                    Intent intent = new Intent(activity_mysubjects.this, activity_teacher_dashboard.class);
                    startActivity(intent);
                }  else if (id == R.id.nav_logout) {
                    if(sessionManager.isLoggedIn()){
                        sessionManager.logoutUser();
                        startActivity(new Intent(activity_mysubjects.this, login_activity.class));
                        finish();
                        Toast.makeText(activity_mysubjects.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity_mysubjects.this, "Unknown item clicked", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer
                return true;
            }
        });

        // Set the navigation view initially hidden
        drawerLayout.closeDrawer(GravityCompat.START);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main9), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        db = DBcon.getDb();

        uploadMarkBtn = findViewById(R.id.uploadButton);
        recyclerView = findViewById(R.id.recyclerView_subjectsGrid);
        recyclerView2 = findViewById(R.id.classRecyclerView_classGrid);
        ViewMarksBtn = findViewById(R.id.submitButton);

        teacherId = sessionManager.getUserId();

        uploadMarkBtn.setOnClickListener(v -> {
            String subjectId = TcSubjectAdapter.getSelectedItem();
            String classId = TcClassAdapter.getSelectedItem();
            if (subjectId != null && classId != null) {
                Intent intent = new Intent(activity_mysubjects.this, activity_teacher_addmarks.class);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("classId", classId);
                startActivity(intent);

            } else
            {
                Toast.makeText(activity_mysubjects.this, "No item selected", Toast.LENGTH_SHORT).show();
            }
        });

        ViewMarksBtn.setOnClickListener(v -> {
            String subjectId = TcSubjectAdapter.getSelectedItem();
            String classId = TcClassAdapter.getSelectedItem();
            if (subjectId != null && classId != null) {
                Intent intent = new Intent(activity_mysubjects.this, activity_teacher_viewmarks.class);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("classId", classId);
                startActivity(intent);

            } else
            {
                Toast.makeText(activity_mysubjects.this, "No item selected", Toast.LENGTH_SHORT).show();
            }
        });

        //when user click backBtn4 then activity_teacher_dashboard will open
        findViewById(R.id.backBtn4).setOnClickListener(v -> {
            finish();
        });
        initSubjects();

    }

    private void initSubjects() {
        db.collection("teachers").document(teacherId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<TcSubject>  subjects = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = (Map<String, Object>) document.get("subjects");
                                if (data != null) {
                                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                                        String subjectId = entry.getKey();
                                        String subjectName = getName(subjectId);
                                        subjects.add(new TcSubject(subjectId, subjectName));
                                    }
                                    SubAdapter = new TcSubjectAdapter(activity_mysubjects.this,subjects, activity_mysubjects.this);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(activity_mysubjects.this));
                                    recyclerView.setAdapter(SubAdapter);
                                }

                            }
                            else {
                                Toast.makeText(activity_mysubjects.this, "No subjects found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(activity_mysubjects.this, "Error fetching subjects: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    private String getName(String subjectId) {

        String[] parts = subjectId.split("_");
        String grade = parts[0];
        if(grade.equals(" 12") || grade.equals(" 13")){
            return grade +" "+ parts[4];
        }else{
            return grade +" "+ parts[3];
        }
    }

    @Override
    public void initClasses(String subjectId){
        db.collection("teachers").document(teacherId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<TcSubject>  classes = new ArrayList<>();
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = (Map<String, Object>) document.get("subjects");
                                if (data != null) {
                                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                                        if(entry.getKey().equals(subjectId)){
                                            String[] classData = entry.getValue().toString().split(" - ");
                                            for (String classId : classData) {
                                                String className = classId.split("_")[3];
                                                classes.add(new TcSubject(classId, className));
                                            }
                                        }
                                    }
                                    ClassAdapter = new TcClassAdapter(activity_mysubjects.this,classes);
                                    recyclerView2.setLayoutManager(new LinearLayoutManager(activity_mysubjects.this));
                                    recyclerView2.setAdapter(ClassAdapter);
                                }

                            }
                            else {
                                Toast.makeText(activity_mysubjects.this, "No subjects found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(activity_mysubjects.this, "Error fetching subjects: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}