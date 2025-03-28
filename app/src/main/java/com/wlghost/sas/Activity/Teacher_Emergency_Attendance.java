package com.wlghost.sas.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Teacher_Emergency_Attendance extends AppCompatActivity {

    private static final String TAG = "Teacher_Emergency_Attendance";
    private String teacherId;
    EditText stId,reson;
    TextView Parent_name,Parent_phone;
    MaterialButton readButton,seach_btn;
    private DocumentReference db;
    dbCon con = new dbCon();
    private String studentId,token,date;

    private static final String SERVER_KEY = "YOUR_SERVER_KEY_HERE"; // Add your Firebase server key
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_emergency_attendance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main15), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        teacherId = getIntent().getStringExtra("userId");
        stId = findViewById(R.id.StudentIndex2);
        reson = findViewById(R.id.Reson);
        Parent_name = findViewById(R.id.Parent_name);
        Parent_phone = findViewById(R.id.Parent_phone);
        readButton = findViewById(R.id.readButton);
        seach_btn = findViewById(R.id.seach_btn);
        db = con.getDb();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        date = dateFormat.format(new Date());

        seach_btn.setOnClickListener(v -> {
            studentId = stId.getText().toString();
            if (!studentId.isEmpty()) {
                loadDetails(studentId);
            } else {
                stId.setError("Please enter a student ID");
                stId.requestFocus();
            }
        });
        readButton.setOnClickListener(v -> {

            if (!studentId.isEmpty() && !reson.getText().toString().isEmpty()) {
                String time = timeFormat.format(new Date());
                sendEmergencyAttendance(teacherId,studentId,reson.getText().toString(),token,time);
            } else {
                stId.setError("Please enter a student ID");
                stId.requestFocus();
            }
        });

    }

    private void sendEmergencyAttendance(String teacherId, String studentId, String reson1, String token1,String outTime) {
        try {
            db.collection("attendence").document(date).collection("students")
                    .whereEqualTo("id", studentId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!task.getResult().isEmpty()) {
                                    String studentd = task.getResult().getDocuments().get(0).getId();
                                    updateAttendance(teacherId, studentd, reson1, token1, outTime);
                                }
                                else{
                                    Toast.makeText(Teacher_Emergency_Attendance.this, "No student found with this ID", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Teacher_Emergency_Attendance.this, "Error 3: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Teacher_Emergency_Attendance.this, "Error 6: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        catch (Exception e){
            Toast.makeText(Teacher_Emergency_Attendance.this, "Error 5: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void updateAttendance(String teacherId, String studentd, String reson1, String token1, String outTime) {
        db.collection("attendence").document(date).collection("students")
                .document(studentd)
                .update("out_time", outTime, "reson", reson1,  "teacher", teacherId)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Teacher_Emergency_Attendance.this, "Attendance updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    reson.setText(e.getMessage());
                    Toast.makeText(Teacher_Emergency_Attendance.this, "Error 6: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("SetTextI18n")
    private void loadDetails(String id) {
        db.collection("students").document(id)
                .get()
               .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       if (task.getResult().exists()) {
                           token = task.getResult().getString("token");
                           Parent_name.setText("gurdian name: "+task.getResult().getString("gurdName"));
                           Parent_phone.setText("gurdian phone: "+task.getResult().getString("phone"));
                       } else {
                           Parent_name.setText("No Parent Found");
                           Parent_phone.setText("No Parent Found");
                       }
                   } else {
                       Parent_name.setText("No Parent Found");
                       Parent_phone.setText("No Parent Found");
                   }
               });
    }


}