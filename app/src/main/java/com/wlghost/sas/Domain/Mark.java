package com.wlghost.sas.Domain;

public class Mark {
    private String subject;
    private String marks;
    private String grade;

    public Mark(String subject, String marks, String grade) {
        this.subject = subject;
        this.marks = marks;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public String getMarks() {
        return marks;
    }

    public String getGrade() {
        return grade;
    }
}
