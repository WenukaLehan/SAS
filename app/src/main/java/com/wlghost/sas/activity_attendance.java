package com.wlghost.sas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wlghost.sas.Adapter.AttendanceAdapter;
import com.wlghost.sas.Modal.AttendanceModel;
import com.wlghost.sas.Helper.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class activity_attendance extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<AttendanceModel> attendanceList;
    private FirebaseFirestore db;
    private String teacherId = "200401"; // Replace with dynamic retrieval if needed
    private static final String TAG = "activity_attendance";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize Firestore and UI components
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList);
        recyclerView.setAdapter(adapter);

        // Handle back button click
        findViewById(R.id.backBtn1).setOnClickListener(v -> finish());

        // Load attendance data
        loadAttendanceData();
    }

    private void loadAttendanceData() {
        db.collection("schools").document("pmv").collection("teachers").document(teacherId)
                .get()
                .addOnSuccessListener(teacherDoc -> {
                    if (teacherDoc.exists()) {
                        String classId = teacherDoc.getString("classId");
                        fetchStudentsForClass(classId);
                    } else {
                        Log.e(TAG, "Teacher document not found.");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching teacher data: ", e));
    }

    private void fetchStudentsForClass(String classId) {
        db.collection("schools").document("pmv").collection("students")
                .whereEqualTo("classId", classId)
                .get()
                .addOnSuccessListener(studentDocs -> {
                    List<String> studentIds = new ArrayList<>();
                    for (DocumentSnapshot studentDoc : studentDocs) {
                        studentIds.add(studentDoc.getId());
                    }
                    fetchAttendanceStatus(studentIds);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching students: ", e));
    }

    private void fetchAttendanceStatus(List<String> studentIds) {
        db.collection("schools").document("pmv").collection("attendence").document("13-11-2024")
                .collection("students")
                .get()
                .addOnSuccessListener(attendanceDocs -> {
                    for (DocumentSnapshot attendanceDoc : attendanceDocs) {
                        String studentId = attendanceDoc.getId();
                        if (studentIds.contains(studentId)) {
                            String studentName = attendanceDoc.getString("name"); // Ensure 'name' field exists in Firestore
                            String inTime = attendanceDoc.getString("in_time");
                            String status = (inTime != null && !inTime.isEmpty()) ? "Present" : "Absent";
                            attendanceList.add(new AttendanceModel(studentName, status));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching attendance data: ", e));
    }
}
