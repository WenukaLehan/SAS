package com.wlghost.sas.Helper;

public class Student {
    private String classId;
    private String className;
    private String disName;
    private String dob;
    private String email;
    private String fullName;
    private String gender;
    private int grade;
    private String gurdName;
    private String guardianType;
    private String nic;
    private String rfid;
    private String streamId;
    private String setStID;


    public void setStId(String id) { this.setStID = id;   }



    // Empty constructor for Firestore


    // Getters and setters for each field
    // Add constructor if needed
    public String getClassId() { return classId; }

    public String getStId() { return setStID; }
    public String getClassName() { return className; }
    public String getDisName() { return disName; }
    public String getDob() { return dob; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getGender() { return gender; }
    public int getGrade() { return grade; }
    public String getGuardianName() { return gurdName; }
    public String getGuardianType() { return guardianType; }
    public String getNic() { return nic; }
    public String getRfid() { return rfid; }
    public String getStreamId() { return streamId; }
}
