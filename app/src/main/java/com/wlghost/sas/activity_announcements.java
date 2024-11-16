package com.wlghost.sas;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;
import java.util.Map;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;

public class activity_announcements extends AppCompatActivity {

    private SessionManager sessionManager;
    private dbCon DBCon = new dbCon();
    private DocumentReference db;

    private EditText announcementText;
    private RadioGroup announcementType;
    private MaterialButton submitButton;

    private static  String msg;
    private static  String type;
    private static String sender;
    private static String receiver;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcements);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(getApplicationContext());
        db = DBCon.getDb();
        announcementText = findViewById(R.id.announcementText);
        announcementType = findViewById(R.id.announcementType);
        submitButton = findViewById(R.id.submitButton);

        sender = sessionManager.getUserId();
        db.collection("teachers").document(sender).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        receiver = documentSnapshot.getString("classID");
                    }
                });

        announcementType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                type = button.getText().toString();
            }
        });

        submitButton.setOnClickListener(v -> {
            msg = announcementText.getText().toString();
            if (msg.isEmpty()) {
                announcementText.setError("Please enter announcement message");
                announcementText.requestFocus();
                return;
            }
            else {
                updateAnnouncement();
            }

        });

    }

    private void updateAnnouncement() {
        Map<String, Object> announcement = new HashMap<>();
        announcement.put("msg", msg);
        announcement.put("type", type);
        announcement.put("sender", sender);
        announcement.put("receiver", receiver);

        db.collection("announcements").add(announcement)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(activity_announcements.this, "Announcement sent successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(activity_announcements.this, "Failed to send announcement", Toast.LENGTH_SHORT).show());
    }

}