package com.wlghost.sas.Domain;

public class StudentMarks {
    private String id;
    private String name;
    private int marks;

    public StudentMarks() {
        // Default constructor (required for Firestore)
    }

    public StudentMarks(String id, String name, int marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
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

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
