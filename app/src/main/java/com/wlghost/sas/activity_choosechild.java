package com.wlghost.sas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.StudentAdapter;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Domain.Student;
import com.wlghost.sas.Helper.dbCon;

import java.util.ArrayList;
import java.util.List;

public class activity_choosechild extends AppCompatActivity {

    private ImageView profileImage;
    private SessionManager sessionManager;

    private RecyclerView recyclerView;

    private StudentAdapter adapter;
    private List<Student> studentList = new ArrayList<>();

    // Use centralized database connection from dbCon
    private dbCon DBCon = new dbCon();
    private DocumentReference schoolDocRef = DBCon.getDefaultSchoolDocument();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choosechild);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(adapter);

        loadStudentData();

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
        sessionManager = new SessionManager(getApplicationContext());
        profileImage = findViewById(R.id.profile);
        profileImage.setOnClickListener(v -> {
            if(sessionManager.isLoggedIn()){
                sessionManager.logoutUser();
                startActivity(new Intent(activity_choosechild.this, login_activity.class));
                finish();
                Toast.makeText(activity_choosechild.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }

        });
    }
}