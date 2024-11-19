package com.wlghost.sas.Domain;

public class StudentMarks {
    private String id;
    private String name;

    public StudentMarks() {
        // Default constructor (required for Firestore)
    }

    public StudentMarks(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
