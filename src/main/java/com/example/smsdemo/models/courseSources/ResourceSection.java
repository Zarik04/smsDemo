package com.example.smsdemo.models.courseSources;

import com.example.smsdemo.models.Teacher;
import com.example.smsdemo.models.User;

import java.util.ArrayList;

public class ResourceSection {
    private String ID, topic, description, date;
    private Teacher author;

    private ArrayList<Attachment> attachments;

    public ResourceSection() {
    }

    public String getID() {
        String result = ID;
        while (result.length()<7){
            result = "0"+result;
        }
        return result;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Teacher getAuthor() {
        return author;
    }

    public void setAuthor(Teacher author) {
        this.author = author;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }
}
