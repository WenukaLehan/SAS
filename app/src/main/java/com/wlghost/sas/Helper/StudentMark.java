package com.wlghost.sas.Helper;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.wlghost.sas.Activity.activity_teacher_addmarks;
import com.wlghost.sas.Domain.ClassStudentMarks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentMark {

    public static List<ClassStudentMarks> studentList;
    dbCon DBcon = new dbCon();
    public DocumentReference db = DBcon.getDb();

    public void initStudents(String clzId, String semister, String year, StudentListCallback callback) {
        studentList = new ArrayList<>();
        db.collection("students")
                .whereEqualTo("classId", clzId) // Filter by class ID
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
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
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", e.getMessage());
                    callback.onError(e); // Handle error with callback
                });
    }

    // Callback interface to handle asynchronous results
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
                });
    }

    public interface GetMarksCallback {
        void onResult(int mark);
    }


}
