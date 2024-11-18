package com.wlghost.sas.Domain;

public class AttendanceRecord {


    private String date;
    private String in_time;
    private String out_time;



    public AttendanceRecord(String date, String arrivedTime, String leftTime) {
        this.date = date;
        this.in_time = arrivedTime;
        this.out_time = leftTime;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrivedTime() {
        return in_time;
    }

    public void setArrivedTime(String arrivedTime) {
        this.in_time = arrivedTime;
    }

    public String getLeftTime() {
        return out_time;
    }

    public void setLeftTime(String leftTime) {
        this.out_time = leftTime;
    }
}

