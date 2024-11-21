package com.wlghost.sas.Domain;

import java.util.Map;

public class ClassStudentMarks {
    private String studentName;
    private String studentId;
    private Map<String, Integer> marksMap;
    private int totalMarks;
    private double average;
    private int place;

    public ClassStudentMarks(String studentName, String studentId, Map<String, Integer> marksMap, int totalMarks, double average) {
        this.studentName = studentName;
        this.studentId = studentId;
        this.marksMap = marksMap;
        this.totalMarks = totalMarks;
        this.average = average;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Map<String, Integer> getMarksMap() {
        return marksMap;
    }

    public void setMarksMap(Map<String, Integer> marksMap) {
        this.marksMap = marksMap;
    }
}
