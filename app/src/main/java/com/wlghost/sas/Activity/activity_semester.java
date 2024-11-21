package com.wlghost.sas.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.MarksAdapter;
import com.wlghost.sas.Domain.Mark;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.List;

public class activity_semester extends AppCompatActivity {

    private static final String TAG = "activity_semester";
    private RecyclerView marksRecyclerView;
    private MarksAdapter marksAdapter;
    private dbCon DBCon = new dbCon();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_semester);


        // Retrieve data from Intent
        String studentId = getIntent().getStringExtra("studentId");
        String semester = getIntent().getStringExtra("semester");

        // Initialize RecyclerView
        marksRecyclerView = findViewById(R.id.resultsRecyclerView);
        marksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch and display marks
        fetchSemesterMarks(studentId, semester);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main14), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchSemesterMarks(String studentId, String semester) {
        DBCon.getDb().collection("marks")
                .document("studentId")
                .collection(semester)
                .whereEqualTo("semester", studentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Mark> marksList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String subject = doc.getString("subject");
                            String mark = doc.getString("marks");
                            String grade = doc.getString("grade");
                            marksList.add(new Mark(subject, mark, grade));
                        }
                        updateRecyclerView(marksList);
                    } else {
                        Log.e(TAG, "Error fetching marks: ", task.getException());
                        Toast.makeText(this, "Error fetching marks.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Firestore query failed: ", e);
                    Toast.makeText(this, "Error fetching semester data.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateRecyclerView(List<Mark> marksList) {
        marksAdapter = new MarksAdapter(marksList, this);
        marksRecyclerView.setAdapter(marksAdapter);
    }

    private String getSemesterPath(String semester) {
        switch (semester) {
            case "1stSem":
                return "1stSem";
            case "2ndsem":
                return "2ndSem";
            case "3rdsem":
                return "semester3";
            default:
                return "3rdSem";
        }
    }
}