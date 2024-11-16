package com.wlghost.sas.Helper;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
