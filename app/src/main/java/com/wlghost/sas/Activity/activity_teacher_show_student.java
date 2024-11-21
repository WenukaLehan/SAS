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
import com.google.firebase.firestore.QuerySnapshot;
import com.wlghost.sas.Adapter.Adapter_student_list_mark;
import com.wlghost.sas.Domain.ClassStudentMarks;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.StudentMark;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class activity_teacher_show_student extends AppCompatActivity {

    private String userId,semester,clzId,year;
    private TextView title;
    public static List<ClassStudentMarks> studentList;
    private DocumentReference db;
    private RecyclerView recyclerView;
    private Adapter_student_list_mark adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_show_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbCon DBcon = new dbCon();
        db = DBcon.getDb();
        recyclerView = findViewById(R.id.studentRecyclerView2);
        userId = getIntent().getStringExtra("userId");
        setName(userId);
        semester = getIntent().getStringExtra("semester");
        title = findViewById(R.id.title2);
        //get year current
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        year = Integer.toString(currentYear);

    }

    private void setName(String userId) {
        db.collection("teachers").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            clzId = documentSnapshot.getString("classID");
            initStudents(clzId, semester,year, new StudentListCallback() {

                @Override
                public void onResult(List<ClassStudentMarks> studentList) {
                    // Handle the student list here
                    adapter = new Adapter_student_list_mark(activity_teacher_show_student.this, studentList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity_teacher_show_student.this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onError(Exception e) {
                    // Handle error here
                    Toast.makeText(activity_teacher_show_student.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void initStudents(String clzId, String semister, String year, StudentListCallback callback) {

        studentList = new ArrayList<>();
        db.collection("students")
                .whereEqualTo("classId", clzId) // Filter by class ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String id = document.getId(); // Document ID
                            String name = document.getString("disName"); // Assuming "disName" field exists
                            Map<String, Object> subjectMap = (Map<String, Object>) document.get("subjects");
                            if (subjectMap != null) {
                                Map<String, Integer> marksMap = new HashMap<>(); // Initialize inside the loop
                                final int[] totalMarks = {0}; // Use array to modify within inner class

                                for (Map.Entry<String, Object> entry : subjectMap.entrySet()) {
                                    String subName = entry.getValue().toString();
                                    getMarks(year, semister, entry.getKey(), id, new GetMarksCallback() {
                                        @Override
                                        public void onResult(int mark) {
                                            marksMap.put(subName, mark);
                                            totalMarks[0] += mark;
                                            if (marksMap.size() == subjectMap.size()) {

                                                double avgMarks = (double) totalMarks[0] / marksMap.size();
                                                studentList.add(new ClassStudentMarks(name, id, marksMap, totalMarks[0], avgMarks));
                                                //Toast.makeText(activity_teacher_show_student.this, totalMarks, Toast.LENGTH_SHORT).show();
                                                if (studentList.size() == queryDocumentSnapshots.size()) {
                                                    Collections.sort(studentList, Comparator.comparingInt(ClassStudentMarks::getTotalMarks).reversed());
                                                    for (int i = 0; i < studentList.size(); i++) {
                                                        studentList.get(i).setPlace(i + 1);
                                                    }
                                                    callback.onResult(studentList);
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }else{
                        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", e.getMessage());
                    callback.onError(e); // Handle error with callback
                });
    }

    public interface StudentListCallback {
        void onResult(List<ClassStudentMarks> studentList);
        void onError(Exception e);
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

    public interface GetMarksCallback {
        void onResult(int mark);
    }


}