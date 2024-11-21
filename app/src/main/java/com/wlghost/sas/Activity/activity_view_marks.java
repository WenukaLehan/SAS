package com.wlghost.sas.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

public class activity_view_marks extends AppCompatActivity {
    private SessionManager sessionManager;
    private MaterialButton mButton;
    private RadioGroup semesters;
    private String semester;
    private DocumentReference db;
    private dbCon DBcon;
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_marks);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main7), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sessionManager = new SessionManager(getApplicationContext());
        mButton = findViewById(R.id.MarksViewButton);
        semesters = findViewById(R.id.semesterRadioGroup);

        DBcon = new dbCon();
        db = DBcon.getDb();

        userId = sessionManager.getUserId();

        semesters.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Handle the checked change
                if (checkedId == R.id.firstSemester) {
                    semester = "1stSem";
                    mButton.setText("1st Semester Marks");
                } else if (checkedId == R.id.secondSemester) {
                    semester = "2ndSem";
                    mButton.setText("2nd Semester Marks");
                } else if (checkedId == R.id.thirdSemester) {
                    semester = "3rdSem";
                    mButton.setText("3rd Semester Marks");
                }
            }
        });

        mButton.setOnClickListener(v -> {
            checkSem();
        });

        //when user click backBtn2 then it will go to activity_teacher_class
        findViewById(R.id.backBtn3).setOnClickListener(v -> onBackPressed());
    }

    private void checkSem(){
        db.collection("semister").document("current")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String sem = document.getString("sem");
                                if(sem.equals(semester)){
                                    Intent intent = new Intent(activity_view_marks.this, activity_teacher_show_student.class);
                                    intent.putExtra("semester", semester);
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(activity_view_marks.this, "current ongoing semester is "+ sem, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(activity_view_marks.this, activity_teacher_show_student.class);
                                    intent.putExtra("semester", semester);
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}