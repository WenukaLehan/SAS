package com.wlghost.sas.Activity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wlghost.sas.Adapter.TcSubjectAdapter;
import com.wlghost.sas.Domain.TcSubject;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class activity_mysubjects extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TcSubjectAdapter SubAdapter;
    private MaterialButton uploadMarkBtn;

    private dbCon DBcon = new dbCon();
    private DocumentReference db;
    private SessionManager sessionManager;

    private static final String TAG = "activity_mysubjects";
    private String teacherId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mysubjects);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main9), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(this);
        db = DBcon.getDb();

        uploadMarkBtn = findViewById(R.id.uploadButton);
        recyclerView = findViewById(R.id.recyclerView_subjectsGrid);

        teacherId = sessionManager.getUserId();

        uploadMarkBtn.setOnClickListener(v -> {
            String selectedItem = TcSubjectAdapter.getSelectedItem();
            if (selectedItem != null) {
                Toast.makeText(activity_mysubjects.this, "Selected item: " + selectedItem, Toast.LENGTH_SHORT).show();
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
                                    SubAdapter = new TcSubjectAdapter(activity_mysubjects.this,subjects);
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


}