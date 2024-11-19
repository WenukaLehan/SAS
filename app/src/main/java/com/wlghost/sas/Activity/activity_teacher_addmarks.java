package com.wlghost.sas.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Adapter.StudentAdapterMarks;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.OnMarksAddListener;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_teacher_addmarks extends AppCompatActivity implements OnMarksAddListener {

    RecyclerView recyclerView;
    MaterialButton submitButton;
    RadioGroup semesters;

    dbCon DBcon = new dbCon();
    private DocumentReference db;
    private Map<String, String> marksMap ;

    private static final String TAG = "activity_teacher_addmarks";
    private String clzId;
    private String subId;
    private String semester;
    String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_addmarks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main10), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = DBcon.getDb();
        recyclerView = findViewById(R.id.studentlistAddMark);
        submitButton = findViewById(R.id.submitButton);
        semesters = findViewById(R.id.semesters);

        clzId = getIntent().getStringExtra("classId");
        subId = getIntent().getStringExtra("subjectId");
        //get current year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        year = Integer.toString(currentYear);
        marksMap = new HashMap<>();
        fetchStudents(clzId, subId); // Fetch students for the specified class and subject

        semesters.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.firstSemester) {
                semester = "1stSem";
            } else if (checkedId == R.id.secondSemester) {
                semester = "2ndSem";
            } else if (checkedId == R.id.thirdSemester) {
                semester = "3rdSem";
            }
        });

        submitButton.setOnClickListener(v -> {
            if (semester != null) {
                if(showConfirmDialog("Are you sure you want to submit the marks to? "+year+" "+semester, "Confirm")) {
                    for (Map.Entry<String, String> entry : marksMap.entrySet()) {
                        String studentId = entry.getKey();
                        String marks = entry.getValue();
                        saveMarksToFirestore(studentId, marks);
                    }
                    Toast.makeText(this, "Marks saved successfully", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Please select semester", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean showConfirmDialog(String message, String title) {
        final boolean[] result = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle positive button click
                result[0] = true;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                // Handle negative button click
                result[0] = false;
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return result[0];

    }


    List<StudentMarks> studentList = new ArrayList<>();

    public void fetchStudents(String clazid, String subjectKey) {
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
                            studentList.add(new StudentMarks(id, name));
                        }
                    }

                    // Create an adapter and set it to the RecyclerView
                    StudentAdapterMarks adapter = new StudentAdapterMarks( studentList, this);
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


}