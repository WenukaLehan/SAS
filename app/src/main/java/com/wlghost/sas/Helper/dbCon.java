package com.wlghost.sas.Helper;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class dbCon {
    FirebaseFirestore db = FirebaseFirestore .getInstance();
    // Add your Firestore instance here
    // db = FirebaseFirestore.getInstance();

    public DocumentReference getDb() {
        return db.collection("schools").document("pmv");
    }
    public DocumentReference getDefaultSchoolDocument() {
        return db.collection("schools").document("pmv");
    }
    public DocumentReference getSchoolDocument(String documentId) {
        return db.collection("schools").document(documentId);
    }

    public Query getStudentAttendance(String studentId, String date) {
        return db.collection("schools")
                .document("pmv") // Adjust this if "pmv" is dynamic
                .collection("attendance")
                .document(date)
                .collection("students")
                .whereEqualTo("studentId", studentId); // Adjust the field if "studentId" is named differently
    }
}
