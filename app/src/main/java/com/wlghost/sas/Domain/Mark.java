package com.wlghost.sas.Domain;

public class Mark {
    private String subject;
    private String marks;

    public Mark(String subject, String marks) {
        this.subject = subject;
        this.marks = marks;
    }

    public String getSubject() {
        return subject;
    }

    public String getMarks() {
        return marks;
    }
}
