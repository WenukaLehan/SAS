package com.wlghost.sas.Domain;

public class AttendanceModel {
    private String studentId;
    private String attendanceStatus;

    //parentattendance######################################//
    private String id;
    private String in_time;
    private String out_time;


    // Empty constructor for Firestore
    public AttendanceModel() {}
    // Constructor
    public AttendanceModel(String studentId, String attendanceStatus) {
        this.studentId = studentId;
        this.attendanceStatus = attendanceStatus;

        //parentdashbord
        this.id = id;
        this.in_time = in_time;
        this.out_time = out_time;

    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }


    //parentdashbord
    public String getId() {
        return id;
    }

    public String getIn_time() {
        return in_time;
    }

    public String getOut_time() {
        return out_time;
    }



    // Setters (if needed)
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
