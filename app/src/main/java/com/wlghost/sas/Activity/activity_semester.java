package com.wlghost.sas.Activity;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.MarksAdapter;
import com.wlghost.sas.Domain.ClassStudentMarks;
import com.wlghost.sas.Domain.Mark;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class activity_semester extends AppCompatActivity {

    private static final String TAG = "activity_semester";
    private RecyclerView marksRecyclerView;
    private MarksAdapter marksAdapter;
    private dbCon DBCon;
    private DocumentReference db;
    private String year;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_semester);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main14), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DBCon = new dbCon();
        db = DBCon.getDb();

        // Initialize year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        year = Integer.toString(currentYear);

        // Retrieve data from Intent
        String studentId = getIntent().getStringExtra("studentId");
        String semester = getIntent().getStringExtra("semester");
        title = findViewById(R.id.semTitle);

        switch (semester){
            case "1stSem":
                title.setText("1st Semester");
                break;
            case "2ndSem":
                title.setText("2nd Semester");
                break;
            case "3rdSem":
                title.setText("3rd Semester");
                break;
        }

        // Initialize RecyclerView
        marksRecyclerView = findViewById(R.id.resultsRecyclerView);

        // Fetch and display marks
        fetchSemesterMarks(studentId, semester);
    }

    private void fetchSemesterMarks(String studentId, String semester) {
        db.collection("students").document(studentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, String> data = (Map<String, String>) documentSnapshot.get("subjects");
                        List<Mark> marksList = new ArrayList<>();
                        if (data!= null) {
                            for (Map.Entry<String, String> entry : data.entrySet()) {
                                String subjectId = entry.getKey();
                                String subject = entry.getValue();
                                getMarks(year, semester, entry.getKey(), studentId, new GetMarksCallback() {
                                    @Override
                                    public void onResult(int mark) {
                                        marksList.add(new Mark(subject, String.valueOf(mark)));
                                        if (marksList.size() == data.size()) {
                                            marksAdapter = new MarksAdapter(marksList, activity_semester.this);
                                            marksRecyclerView.setLayoutManager(new LinearLayoutManager(activity_semester.this));
                                            marksRecyclerView.setAdapter(marksAdapter);
                                            marksAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        Toast.makeText(activity_semester.this, "Student not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching semester marks", e);
                    Toast.makeText(activity_semester.this, "Error fetching semester marks", Toast.LENGTH_SHORT).show();
                });
    }


    private void getMarks(String year,String semester,String subId,String id, GetMarksCallback callback) {
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
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "get failed with ", e);
                    callback.onResult(0);
                    Toast.makeText(this, "adoo"+e.getMessage(), Toast.LENGTH_SHORT).show();// or handle accordingly
                });
    }

    private void updateRecyclerView(List<Mark> marksList) {
        marksAdapter = new MarksAdapter(marksList, this);
        marksRecyclerView.setAdapter(marksAdapter);
    }

    public interface GetMarksCallback {
        void onResult(int mark);
    }

}