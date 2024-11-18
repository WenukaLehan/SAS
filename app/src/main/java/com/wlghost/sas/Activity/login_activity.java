package com.wlghost.sas.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wlghost.sas.Helper.SessionManager;
import com.wlghost.sas.Helper.dbCon;
import com.wlghost.sas.R;

public class login_activity extends AppCompatActivity {

    dbCon DBCon = new dbCon();
    private final DocumentReference db = DBCon.getDb();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    MaterialButton loginbtn;
    EditText emailEditText, passwordEditText;
    TextView frogot;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        frogot = findViewById(R.id.forgotPassword);
        loginbtn = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailTxt);
        passwordEditText = findViewById(R.id.passTxt);

        sessionManager = new SessionManager(getApplicationContext());

        // If user is already logged in, redirect to MainActivity
        if (sessionManager.isLoggedIn()) {
            if(sessionManager.getUserType().equals("st")) {
                startActivity(new Intent(login_activity.this, activity_choosechild.class));
                finish();
            }else {
                startActivity(new Intent(login_activity.this, activity_teacher_dashboard.class));
                finish();
            }
        }

        frogot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();

                if (email.isEmpty()) {
                    emailEditText.setError("Please enter your email");
                    emailEditText.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login_activity.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Toast.makeText(login_activity.this, email +" \n "+ password, Toast.LENGTH_SHORT).show();
                if (email.isEmpty()) {
                    emailEditText.setError("Please enter your email");
                    emailEditText.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Please enter your password");
                    passwordEditText.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                    if (firebaseUser != null) {
                                        firebaseUser.reload().addOnCompleteListener(taskd -> {
                                            if (taskd.isSuccessful()) {
                                                if (firebaseUser.isEmailVerified()) {

                                                    getUserData("students",email, "st", new FirestoreCallback() {
                                                        @Override
                                                        public void onCallback(String result) {
                                                            if (result.equals("true")) {
                                                                startActivity(new Intent(login_activity.this, activity_choosechild.class));
                                                                finish();
                                                                Toast.makeText(login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                                return;

                                                            }else {
                                                                getUserData("teachers",email, "tc", new FirestoreCallback() {
                                                                    @Override
                                                                    public void onCallback(String result) {
                                                                        if (result.equals("true")) {
                                                                            startActivity(new Intent(login_activity.this, activity_teacher_dashboard.class));
                                                                            finish();
                                                                            Toast.makeText(login_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                                            return;

                                                                        }else {
                                                                            Log.d("Firestore", "Error occurred");
                                                                            Toast.makeText(login_activity.this, "Error on loging", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }
                                                                });

                                                            }

                                                        }
                                                    });



                                                } else {
                                                    Toast.makeText(login_activity.this, "Please verify your email first.", Toast.LENGTH_SHORT).show();
                                                    firebaseUser.sendEmailVerification();
                                                }
                                            } else {
                                                Toast.makeText(login_activity.this, "Failed to reload user.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } else {
                                    Toast.makeText(login_activity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login_activity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    public interface FirestoreCallback {
        void onCallback(String result);
    }


    public void getUserData(String table1,String email, String type, final FirestoreCallback callback) {
        db.collection(table1)
                .whereEqualTo("email", email)
                .whereEqualTo("type", type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("disName");
                                String type1 = document.getString("type");
                                sessionManager.createLoginSession(email, name, id, type1);

                                // Use the callback to return the result
                                callback.onCallback("true");
                                return ;
                            }
                            callback.onCallback("No document found");
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                            callback.onCallback("Error");
                        }
                    }
                });
    }



}