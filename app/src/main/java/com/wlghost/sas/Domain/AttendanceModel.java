package com.wlghost.sas.Domain;

public class AttendanceModel {
    private String studentId;
    private String attendanceStatus;
    private String inTime;
    private String outTime;




    // Empty constructor for Firestore

    // Constructor
    public AttendanceModel(String studentId, String attendanceStatus, String inTime, String outTime) {
        this.studentId = studentId;
        this.attendanceStatus = attendanceStatus;
        this.inTime = inTime;
        this.outTime = outTime;
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

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
