package com.example.smsdemo.models;

import java.util.ArrayList;

public class Student extends User {
    private int counter;
    private String studentID, groupID, logStatus, regStatus;
    private ArrayList<Course> courses;
    private Group group;

    public Student() {
        this.userType = "student";
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus() {
        if (isOnline) logStatus = "Online";
        else logStatus = "Offline";
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus() {
        if (isRegistered) regStatus = "Confirmed";
        else regStatus = "Not Confirmed";
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
