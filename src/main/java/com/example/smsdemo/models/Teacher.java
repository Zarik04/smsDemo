package com.example.smsdemo.models;

import java.util.ArrayList;

public class Teacher extends User{
    private ArrayList<Group> groups;
    private ArrayList<Course> courses;

    public Teacher(){
        this.userType = "teacher";
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
