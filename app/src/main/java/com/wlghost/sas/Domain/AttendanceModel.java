package com.wlghost.sas.Domain;

public class AttendanceModel {
    private String studentId;
    private String attendanceStatus;

    // Constructor
    public AttendanceModel(String studentId, String attendanceStatus) {
        this.studentId = studentId;
        this.attendanceStatus = attendanceStatus;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    // Setters (if needed)
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
