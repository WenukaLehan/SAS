package com.wlghost.sas.Domain;

import java.io.Serializable;

public class Announcement implements Serializable {

    private String msg;
    private String type;
    private String sender;
    private String receiver;

    // Default constructor for Firestore
    public Announcement() {}

    // Getters and setters
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
}