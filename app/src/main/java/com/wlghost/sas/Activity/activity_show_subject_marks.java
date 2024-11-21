package com.wlghost.sas.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.wlghost.sas.Adapter.StudentAdapterViewMarks;
import com.wlghost.sas.Domain.StudentMarks;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.List;
import java.util.Map;

public class activity_show_subject_marks extends AppCompatActivity {

    private String Name,Id;
    private int place,totalMarks;
    private double avg;
    private Map<String, Integer> marks;
    private DocumentReference db;
    private List<StudentMarks> studentList;
    private StudentAdapterViewMarks adapter;
    private RecyclerView recyclerView;

    private TextView stName,Total,avg1,place1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_subject_marks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.showSub), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            dbCon DBcon = new dbCon();
            db = DBcon.getDb();
            Id = getIntent().getStringExtra("studentId");
            place = getIntent().getIntExtra("place", 0);
            totalMarks = getIntent().getIntExtra("totalMarks", 0);
            avg = getIntent().getDoubleExtra("average", 0);
            marks = (Map<String, Integer>) getIntent().getSerializableExtra("marksMap");

            stName = findViewById(R.id.stName);
            Total = findViewById(R.id.Total);
            avg1 = findViewById(R.id.avg);
            place1 = findViewById(R.id.place);
            recyclerView = findViewById(R.id.subjects_list);

            fetchMarks();
        }
        catch (Exception e){
            Toast.makeText(this, "Error fetching marks", Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchMarks() {
        //adapter.clear();
        studentList = new java.util.ArrayList<>();
        db.collection("students").document(Id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Name = documentSnapshot.getString("fullName");
                    stName.setText(Name);
                    Total.setText(String.valueOf(totalMarks));
                    avg1.setText(String.format("%.2f", avg));
                    place1.setText(String.valueOf(place));
                    for (Map.Entry<String, Integer> entry : marks.entrySet()) {
                        studentList.add(new StudentMarks("sd",entry.getKey(), entry.getValue()));
                    }
                    adapter = new StudentAdapterViewMarks(studentList, this);
                    recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching marks", Toast.LENGTH_SHORT).show());
    }
}